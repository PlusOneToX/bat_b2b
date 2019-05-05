package com.bat.thirdparty.erp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.flexible.FactoryConstant;
import com.bat.thirdparty.common.order.OrderInfoConstant;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.api.request.*;
import com.bat.thirdparty.erp.api.response.*;
import com.bat.thirdparty.erp.convertor.ErpOrderConvertor;
import com.bat.thirdparty.erp.manager.ErpDataManager;
import com.bat.thirdparty.factory.maike.common.MaikeConfigQry;
import com.bat.thirdparty.factory.maike.common.MaikeFactoryConfig;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.order.order.api.OrderInfoServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;
import com.bat.thirdparty.erp.api.request.*;
import com.bat.thirdparty.erp.api.request.dto.order.CreateSaleOrderQry;
import com.bat.thirdparty.erp.api.response.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService
public class ThirdPartyOrderServiceErpRpcImpl implements ThirdPartyOrderServiceErpRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyOrderServiceErpRpcImpl.class);

    @Autowired
    private ErpDataManager erpDataManager;

    @Autowired
    private ErpOrderConvertor erpOrderConvertor;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderInfoServiceRpc orderInfoServiceRpc;

    @CreateCache(name = ThirdKeyConstant.SALE_ORDER_SYNC_ERP_PRE)
    private Cache<String, Integer> orderSyncErpCache;

    @Autowired
    private MessageSendService messageSendService;

    @Resource
    MaikeConfigQry maikeConfigQry;

    @Resource
    private ConfigQry factoryConfig;

    @Override
    public Response<String> syncOrderToErp(Integer orderId) {
        LOGGER.info("订单号" + orderId + "同步erp");
        if (orderId == null) {
            throw ThirdPartyException.buildException("订单id或者订单对象不能同时为空");
        }
        // 分布式锁
        AutoReleaseLock autoReleaseLock = orderSyncErpCache
            .tryLock(TenantContext.getTenantNo() + ":" + String.valueOf(orderId), 15, TimeUnit.MINUTES);
        if (autoReleaseLock == null) {
            // 加锁
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        try {
            CreateSaleOrderQry createSaleOrderQry = erpOrderConvertor.toCreateSaleOrderQry(orderId);
            createSaleOrderQry.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            LOGGER.info("同步销售单到ERP、参数为：{}", JSON.toJSONString(createSaleOrderQry));
            CreateSaleOrderResponse saleOrderResponse = erpDataManager.createSaleOrder(createSaleOrderQry);
            LOGGER.info("同步销售单到ERP、响应为：{}", JSON.toJSONString(saleOrderResponse));
            if (saleOrderResponse == null) {
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION);
            }
            if (saleOrderResponse != null && BaseResponse.INVALID_TOKEN.equals(saleOrderResponse.getCode())
                || BaseResponse.INVALID_TOKEN_MSG.equals(saleOrderResponse.getMessage())) {
                LOGGER.info("erp登陆token失效，同步销售订单失败");
                erpDataManager.deleteCachedToken("AXI-token");
                createSaleOrderQry.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
                saleOrderResponse = erpDataManager.createSaleOrder(createSaleOrderQry);
            }
            if (saleOrderResponse == null) {
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION);
            }
            if (saleOrderResponse.isSuccess()) {
                LOGGER.info("订单号为：" + createSaleOrderQry.getFB2B_BILLNO() + "同步erp成功，开始设置ERP单号和推送收款单"
                    + createSaleOrderQry.getSyncVoucherFlag());
                messageSendService.oredrLogPackage(orderId, "订单同步到erp",
                    "同步成功:" + JSONObject.toJSONString(saleOrderResponse), JSONObject.toJSONString(createSaleOrderQry));
                // 设置ERP单号和行序号、处理标品直发订单库存
                return setErpOrderMsgAndDealWithStock(orderId, createSaleOrderQry, saleOrderResponse);
            } else if (saleOrderResponse.getCode().equals(BaseResponse.ERP_CHECK_ERROR_CODE)
                || saleOrderResponse.getCode().equals(BaseResponse.ERP_UNDEFINED_ERROR_CODE)) {
                if ("1".equals(createSaleOrderQry.getWhetherAudit())) {
                    LOGGER.error("信用档案问题、不再推到ERP，订单id{}", orderId);
                    throw ThirdPartyException.buildException(saleOrderResponse.getCode(),
                        saleOrderResponse.getMessage());
                }
                createSaleOrderQry.setWhetherAudit("1");
                LOGGER.info("信用问题、参数改为不自动审核、重推参数:{}", JSON.toJSONString(createSaleOrderQry));
                saleOrderResponse = erpDataManager.createSaleOrder(createSaleOrderQry);
                LOGGER.info("信用问题、参数改为不自动审核、响应:{}", JSON.toJSONString(saleOrderResponse));
                if (saleOrderResponse == null) {
                    throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION);
                }
                if (saleOrderResponse.isSuccess()) {// 再次同步订单成功后需变更订单状态
                    LOGGER.info("订单号为：" + createSaleOrderQry.getFB2B_BILLNO() + "同步erp成功(信用档案重推)，开始设置ERP单号和推送收款单"
                        + createSaleOrderQry.getSyncVoucherFlag());
                    messageSendService.oredrLogPackage(orderId, "订单同步到erp",
                        "同步成功:" + JSONObject.toJSONString(saleOrderResponse),
                        JSONObject.toJSONString(createSaleOrderQry));
                    // 设置ERP单号和行序号、处理标品直发订单库存
                    return setErpOrderMsgAndDealWithStock(orderId, createSaleOrderQry, saleOrderResponse);
                } else {
                    messageSendService.oredrLogPackage(orderId, "订单同步到erp",
                        "同步失败:" + JSONObject.toJSONString(saleOrderResponse),
                        JSONObject.toJSONString(createSaleOrderQry));
                    throw ThirdPartyException.buildException(saleOrderResponse.getCode(),
                        saleOrderResponse.getMessage());
                }
            } else {
                messageSendService.oredrLogPackage(orderId, "订单同步到erp",
                    "同步失败:" + JSONObject.toJSONString(saleOrderResponse), JSONObject.toJSONString(createSaleOrderQry));
                // 推送失败
                throw ThirdPartyException.buildException(saleOrderResponse.getCode(), saleOrderResponse.getMessage());
            }
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            LOGGER.error(orderId + "同步销售单到ERP失败{}", e.getMsg());
            throw ThirdPartyException.buildException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(orderId + "同步销售单到ERP系统异常{}", e.getMessage());
            messageSendService.oredrLogPackage(orderId, "订单同步到erp", "同步失败", orderId.toString());
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        } finally {
            orderSyncErpCache.remove(TenantContext.getTenantNo() + ":" + String.valueOf(orderId));
            // 释放锁
            autoReleaseLock.close();
        }

    }

    private Response<String> setErpOrderMsgAndDealWithStock(Integer orderId, CreateSaleOrderQry createSaleOrderQry,
        CreateSaleOrderResponse saleOrderResponse) {
        com.bat.dubboapi.order.common.Response<Boolean> setOrderErpMsgResponse =
            orderInfoServiceRpc.setOrderErpMsg(saleOrderResponse.getEntryIds(), saleOrderResponse.getData(), orderId,
                createSaleOrderQry.getFBILLTYPEID(), createSaleOrderQry.getSyncVoucherFlag());
        LOGGER.info(orderId + "设置ERP单号和行序号信息、返回{}", JSON.toJSONString(setOrderErpMsgResponse));
        if ("True".equals(createSaleOrderQry.getF_SOZF()) && !createSaleOrderQry.getCustomDiyFlag()) {
            // 标品直发订单、需要同步处理库存解锁
            Boolean syncErpFirstFlag = setOrderErpMsgResponse.getData();
            if (!syncErpFirstFlag) {
                return Response.of(saleOrderResponse.getData());
            }
            SyncGeneralOutboundCmd syncGeneralOutboundCmd = new SyncGeneralOutboundCmd();
            // 跟同步ERP的true和false不一样、这里1、是直发、0、不是
            syncGeneralOutboundCmd.setF_SOZF("1");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            String time = ft.format(new Date());
            syncGeneralOutboundCmd.setFDATE(time);
            syncGeneralOutboundCmd.setFSOURCEBILLNO(saleOrderResponse.getData());
            syncGeneralOutboundCmd.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            LOGGER.info(orderId + "标品直发订单、同步erp触发生成出库单和库存变更、请求参数{}", JSON.toJSONString(syncGeneralOutboundCmd));
            SysPUSHOUTSTOCKResponse sysPUSHOUTSTOCKResponse =
                erpDataManager.syncGeneralOrderOutbound(syncGeneralOutboundCmd);
            LOGGER.info(orderId + "标品直发订单、同步erp触发生成出库单和库存变更、返回{}", JSON.toJSONString(sysPUSHOUTSTOCKResponse));
            if (sysPUSHOUTSTOCKResponse != null && (BaseResponse.INVALID_TOKEN.equals(sysPUSHOUTSTOCKResponse.getCode())
                || BaseResponse.INVALID_TOKEN_MSG.equals(sysPUSHOUTSTOCKResponse.getMessage()))) {
                LOGGER.info("erp登陆token失效，标品直发订单、同步erp触发生成出库单和库存变更");
                erpDataManager.deleteCachedToken("AXI-token");
                syncGeneralOutboundCmd.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
                sysPUSHOUTSTOCKResponse = erpDataManager.syncGeneralOrderOutbound(syncGeneralOutboundCmd);
                LOGGER.info(orderId + "标品直发订单、同步erp触发生成出库单和库存变更、重试返回{}", JSON.toJSONString(sysPUSHOUTSTOCKResponse));
            }
            if (sysPUSHOUTSTOCKResponse == null) {
                throw ThirdPartyException.buildException(orderId + "直发订单同步ERP成功、但触发出库单生成和库存同步无响应、请在ERP处理");
            }
            if (!sysPUSHOUTSTOCKResponse.isSuccess()) {
                throw ThirdPartyException.buildException(orderId + "直发订单同步ERP成功、但触发出库单生成和库存同步失败、原因是{}",
                    sysPUSHOUTSTOCKResponse.getMessage());
            }
        }
        return Response.of(saleOrderResponse.getData());
    }

    @Override
    public Response<String> syncErpPurchase(String orderErpNo, String factoryNo,
        List<OrderGoodsDetailDTO> detailDTOList, String time) {
        if (StringUtils.isBlank(orderErpNo)) {
            return Response.of(null);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time = simpleDateFormat.format(new Date());
        SysPushtSTKInStockRequest sysPushtSTKInStockRequest = new SysPushtSTKInStockRequest();
        sysPushtSTKInStockRequest.setFSOURCEBILLNO(orderErpNo);
        sysPushtSTKInStockRequest.setFDATE(time);
        // request.setFStockId(FSTOCKID);
        for (OrderGoodsDetailDTO orderGoodsDetailDTO : detailDTOList) {
            SysPushtSTKInStockRequest.STKINSTOCKENTRY stkinstockentry = new SysPushtSTKInStockRequest.STKINSTOCKENTRY();
            stkinstockentry.setFMATERIALID(orderGoodsDetailDTO.getItemCode());
            stkinstockentry.setFREALQTY(orderGoodsDetailDTO.getCount().longValue());
            stkinstockentry.setFNOTE("定制订单" + orderErpNo + "下推采购入库");
            sysPushtSTKInStockRequest.getSTK_INSTOCKENTR().add(stkinstockentry);
        }
        // 麦客 海星 联辉王 多鸿 欧丽科 飞快 壳大师
        PlatformTenantDiyFactoryRpcDTO rpcDTO = factoryConfig.getTenantFactoryConfig(factoryNo);
        sysPushtSTKInStockRequest.setFSupplierId(rpcDTO.getSupplierNo());
        sysPushtSTKInStockRequest.setFStockId(rpcDTO.getWarehouseNo());
        sysPushtSTKInStockRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        LOGGER.info("下推采购销售单参数：" + JSON.toJSONString(sysPushtSTKInStockRequest));
        SysPushtSTKInStockResponse sysPushtSTKInStockResponse =
            erpDataManager.sysPushtSTKInStock(sysPushtSTKInStockRequest);
        if (sysPushtSTKInStockResponse == null) {
            return Response.buildFailure("下推ERP销售采购订单异常", "下推ERP销售采购订单无响应");
        }
        if (sysPushtSTKInStockResponse != null
            && (BaseResponse.INVALID_TOKEN.equals(sysPushtSTKInStockResponse.getCode())
                || BaseResponse.INVALID_TOKEN_MSG.equals(sysPushtSTKInStockResponse.getMessage()))) {
            // 重新登录
            LOGGER.info("ERP登录TOKEN失效、下推采购销售单失败、重新登录");
            erpDataManager.deleteCachedToken("AXI-token");
            sysPushtSTKInStockRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            sysPushtSTKInStockResponse = erpDataManager.sysPushtSTKInStock(sysPushtSTKInStockRequest);
        }
        LOGGER.info("下推采购销售单回应：{}" + JSON.toJSONString(sysPushtSTKInStockResponse));
        if (sysPushtSTKInStockResponse == null) {
            return Response.buildFailure("下推ERP销售采购订单异常", "下推ERP销售采购订单无响应");
        }
        if (!sysPushtSTKInStockResponse.isSuccess()) {
            return Response.buildFailure("下推ERP销售采购订单异常", sysPushtSTKInStockResponse.getMessage());
        }
        return Response.of(sysPushtSTKInStockResponse.getData());
    }

    @Override
    public Response<String> syncOutStockToERP(String orderErpNo, String manufactor,
        List<OrderGoodsDetailDTO> orderGoodsDetailDTOS, String deliverTime, BigDecimal distributionAmount,
        String distributionName, String logisticNo, String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time = simpleDateFormat.format(new Date());

        SysPUSHOUTSTOCKRequest request = new SysPUSHOUTSTOCKRequest();
        request.setFSOURCEBILLNO(orderErpNo);
        request.setFDATE(time);
        request.setFWLGS(distributionName);
        request.setFWLDH(logisticNo);
        request.setFFHRQ(deliverTime);
        for (OrderGoodsDetailDTO orderGoodsDetailDTO : orderGoodsDetailDTOS) {
            SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY outstocknentry = new SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY();
            outstocknentry.setFMATERIALID(orderGoodsDetailDTO.getItemCode());
            outstocknentry.setFREALQTY(orderGoodsDetailDTO.getCount().longValue());
            request.getOUTSTOCKNENTRY().add(outstocknentry);
        }
        if (distributionAmount != null && distributionAmount.compareTo(BigDecimal.ZERO) == 1) {
            SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY outstocknentry = new SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY();
            outstocknentry.setFMATERIALID("wlfy");
            outstocknentry.setFREALQTY((long)1);
            request.getOUTSTOCKNENTRY().add(outstocknentry);
        }
        request.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        LOGGER.info("B2B下推销售出库单到ERP，参数为{}", JSON.toJSONString(request));
        SysPUSHOUTSTOCKResponse sysPUSHOUTSTOCKResponse = erpDataManager.sysPUSHOUTSTOCK(request);
        LOGGER.info("B2B下推销售出库单到ERP，响应为{}", JSON.toJSONString(sysPUSHOUTSTOCKResponse));
        if (sysPUSHOUTSTOCKResponse == null) {
            return Response.buildFailure("1122", "同步ERP超时");
        }
        if (sysPUSHOUTSTOCKResponse != null && (BaseResponse.INVALID_TOKEN.equals(sysPUSHOUTSTOCKResponse.getCode())
            || BaseResponse.INVALID_TOKEN_MSG.equals(sysPUSHOUTSTOCKResponse.getMessage()))) {
            // 重新登录
            LOGGER.info("ERP登录TOKEN失效、下推采购销售单失败、重新登录");
            erpDataManager.deleteCachedToken("AXI-token");
            request.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            sysPUSHOUTSTOCKResponse = erpDataManager.sysPUSHOUTSTOCK(request);
            if (sysPUSHOUTSTOCKResponse == null) {
                return Response.buildFailure("1122", "同步ERP超时");
            }
        }
        if (!sysPUSHOUTSTOCKResponse.isSuccess()) {
            return Response.buildFailure(sysPUSHOUTSTOCKResponse.getCode(), sysPUSHOUTSTOCKResponse.getMessage());
        }
        // 返回ERP出库单号
        String data = sysPUSHOUTSTOCKResponse.getData();
        return Response.of(data);
    }

    /**
     * B2B-->ERP取消订单
     * 
     * @param orderErpNo
     * @param operateType
     *            类型 1、作废 2、取消
     * @return
     */
    @Override
    public Response cancelOrder(String orderErpNo, Short operateType, Integer orderId,String remark) {
        return operateType.equals(OrderInfoConstant.ORDER_OPERATE_TYPE_CANCEL) ? cancelOrderToERP(orderErpNo, orderId,remark)
            : erpInvalidSaleOrder(orderErpNo, orderId);
    }

    /**
     * ERP 关闭订单 需要传关闭原因
     *
     * @param orderErpNo
     * @param remark
     * @return
     */
    private Response cancelOrderToERP(String orderErpNo, Integer orderId, String remark) {
        LOGGER.info("B2B->ERP取消订单,{}", orderErpNo);
        CloseSaleOrderRequest closeSaleOrderRequest = new CloseSaleOrderRequest();
        closeSaleOrderRequest.setFBILLNO(orderErpNo);
        closeSaleOrderRequest.setNote(remark);
        closeSaleOrderRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        CloseSaleOrderResponse response = erpDataManager.closeSaleOrder(closeSaleOrderRequest);
        if (response == null) {
            return Response.buildFailure(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION,
                MessageUtils.get(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION));
        }
        if (BaseResponse.INVALID_TOKEN.equals(response.getCode()) || BaseResponse.INVALID_TOKEN_MSG.equals(response.getMessage())) {
            erpDataManager.deleteCachedToken("AXI-token");
            closeSaleOrderRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            response = erpDataManager.closeSaleOrder(closeSaleOrderRequest);
            if (response == null) {
                return Response.buildFailure(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION,
                    MessageUtils.get(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION));
            }
        }
        if (!response.isSuccess()) {
            if (!response.getMessage().contains("单据整单已关闭") && !response.getMessage().contains("单据整单已作废")
                && !response.getMessage().contains("订单已经处于作废状态") && !response.getMessage().contains("订单已经处于关闭状态")) {
                messageSendService.oredrLogPackage(orderId, "订单取消", "取消失败：" + response.getMessage(),
                    JSONObject.toJSONString(closeSaleOrderRequest));
                return Response.buildFailure(ThirdOrderErrorCode.T_ORDER_CANCEL_TO_ERP_FAIL,
                    MessageUtils.get(ThirdOrderErrorCode.T_ORDER_CANCEL_TO_ERP_FAIL) + response.getMessage());
            } else {
                LOGGER.info("ERP订单已经处于关闭状态，无需重复关闭");
                return Response.buildSuccess();
            }

        }
        messageSendService.oredrLogPackage(orderId, "订单取消", "取消成功：" + response.getMessage(),
            JSONObject.toJSONString(closeSaleOrderRequest));
        return Response.buildSuccess();
    }

    /**
     * ERP作废订单
     *
     * @param orderNo
     * @return
     */
    private Response erpInvalidSaleOrder(String orderNo, Integer orderId) {
        LOGGER.info("B2B->ERP作废订单,{}", orderNo);
        InvalidSaleOrderRequest invalidSaleOrderRequest = new InvalidSaleOrderRequest();
        invalidSaleOrderRequest.setFBILLNO(orderNo);
        invalidSaleOrderRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        InvalidSaleOrderResponse response = erpDataManager.invalidSaleOrder(invalidSaleOrderRequest);
        if (response == null) {
            return Response.buildFailure(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION,
                MessageUtils.get(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION));
        }
        if (BaseResponse.INVALID_TOKEN.equals(response.getCode()) || BaseResponse.INVALID_TOKEN_MSG.equals(response.getMessage())) {
            erpDataManager.deleteCachedToken("AXI-token");
            invalidSaleOrderRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            response = erpDataManager.invalidSaleOrder(invalidSaleOrderRequest);
        }
        if (response == null) {
            return Response.buildFailure(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION,
                MessageUtils.get(ThirdDubboServiceErrorCode.VISIT_ERP_SERVICE_EXCEPTION));
        }
        if (!response.isSuccess()) {
            if (!response.getMessage().contains("单据整单已关闭") && !response.getMessage().contains("订单已经处于作废状态")
                && !response.getMessage().contains("订单已经处于关闭状态") && !response.getMessage().contains("单据整单已作废")) {
                messageSendService.oredrLogPackage(orderId, "作废订单", "作废失败：" + response.getMessage(),
                    JSONObject.toJSONString(invalidSaleOrderRequest));
                return Response.buildFailure(ThirdOrderErrorCode.T_ORDER_INVALID_TO_ERP_FAIL,
                    MessageUtils.get(ThirdOrderErrorCode.T_ORDER_INVALID_TO_ERP_FAIL) + response.getMessage());
            } else {
                LOGGER.info("ERP单据整单已关闭，无需重复关闭");
                return Response.buildSuccess();
            }
        }
        messageSendService.oredrLogPackage(orderId, "作废订单", "作废成功：" + response.getMessage(),
            JSONObject.toJSONString(invalidSaleOrderRequest));
        return Response.buildSuccess();
    }
}
