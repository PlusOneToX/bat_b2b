package com.bat.thirdparty.order.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.thirdparty.common.CommonErrorCode;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ThirdPartyConfig;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.order.OrderDeliveryConstant;
import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.common.util.CommonUtils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.factory.api.FactoryServiceHolder;
import com.bat.thirdparty.factory.feikuai.service.FeiKuaiFactoryServiceImpl;
import com.bat.thirdparty.factory.haixing.service.HaixingFactoryServiceImpl;
import com.bat.thirdparty.factory.maike.api.MaikeServiceI;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.order.service.convertor.OrderOpenConvertor;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverSyncErpParam;
import com.bat.dubboapi.order.delivery.dto.OrderGoodsDetailCountDTO;
import com.bat.dubboapi.order.delivery.dto.OrderLogisticsSyncParam;
import com.bat.dubboapi.order.order.api.OrderExtendDataServiceRpc;
import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDiyRpcDTO;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;
import com.bat.dubboapi.thirdparty.order.ThirdPartyOrderDeliverServiceRpc;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.erp.config.ErpConfig;
import com.bat.thirdparty.erp.service.executor.OrderDeliverBillExe;
import com.bat.thirdparty.order.api.AdminOrderServiceI;

import static com.bat.thirdparty.factory.executor.ErrorCode.B_THIRDPARTY_SYNCHRONIZED_LOGISTICS_UNABLE_ERROR;

@Service
public class AdminOrderServiceImpl implements AdminOrderServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrderServiceImpl.class);

    @Autowired
    private ThirdPartyOrderServiceErpRpc thirdPartyOrderServiceErpRpc;

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    @Autowired
    private ThirdPartyOrderDeliverServiceRpc thirdPartyOrderDeliverServiceRpc;

    @Autowired
    private MaikeServiceI maikeServiceI;

    @DubboReference(check = false, timeout = 200000, retries = 0)
    private OrderServiceRpc orderServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    @CreateCache(name = ThirdKeyConstant.SALE_ORDER_SYNC_FACTORY_PRE)
    private Cache<String, Integer> orderSyncFactoryCache;

    @CreateCache(name = ThirdKeyConstant.FLEXIBLE_CUSTOM_DELIBERY_SYNC_ERP_PRE)
    private Cache<String, Integer> syncErpOutboundCache;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @Resource
    private ErpConfig erpConfig;

    @Resource
    private ThirdPartyConfig thirdPartyConfig;

    @Autowired
    private MessageSendService messageSendService;

    @Resource
    private OrderDeliverBillExe orderDeliverBillExe;

    @Resource
    private FactoryServiceHolder holder;

    @Resource
    private HaixingFactoryServiceImpl haixingFactoryService;

    @Resource
    private FeiKuaiFactoryServiceImpl feiKuaiFactoryService;

    @CreateCache(name = "thirdparty.service.", cacheType = CacheType.BOTH)
    private Cache<String, Object> cache;

    @Transactional
    @Override
    public Response syncOrderToERP(Integer orderId) {
        com.bat.dubboapi.thirdparty.common.Response response = thirdPartyOrderServiceErpRpc.syncOrderToErp(orderId);
        return BeanUtils.copy(response, Response.class);
    }

    /**
     * 同步订单到工厂
     *
     * @param orderId
     * @return
     */
    @Override
    public Response syncOrderToFactory(Integer orderId) {
        // 分布式锁
        AutoReleaseLock autoReleaseLock = orderSyncFactoryCache
            .tryLock(TenantContext.getTenantNo() + ":" + String.valueOf(orderId), 10, TimeUnit.MINUTES);
        if (autoReleaseLock == null) {
            // 加锁
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        try {
            com.bat.dubboapi.order.common.Response<FactoryOrderAddCmd> response =
                orderServiceRpc.assemblyParamToFactoryV2(orderId);
            if (response == null) {
                messageSendService.oredrLogPackage(orderId, "订单同步工厂", "查询回来定制的参数失败:获取不到响应", orderId.toString());
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            if (!response.isSuccess()) {
                if (response.getErrCode().equals(CommonErrorCode.ORDER_OTHER_FACTORYNO_EXIST)) {
                    return Response.buildSuccess();
                }
                messageSendService.oredrLogPackage(orderId, "订单同步工厂", "查询回来定制的参数失败:" + response.getErrMessage(),
                    orderId.toString());
                throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
            }
            FactoryOrderAddCmd data = response.getData();
            String productNo = holder.getService(data.getFactoryEnum()).syncOrder(data);
            com.bat.dubboapi.order.common.Response otherFactoryNoResp = orderExtendDataServiceRpc
                .setOtherFactoryNo(orderId, productNo, response.getData().getFactoryEnum().name().toUpperCase());
            if (otherFactoryNoResp == null) {
                LOGGER.info("已经同步了工厂、但设置工厂单号失败，orderId,{},工厂单号{}", orderId, productNo);
                messageSendService.oredrLogPackage(orderId, "订单同步工厂", "同步失败:获取不到订单服响应信息",
                    JSONObject.toJSONString(response.getData()));
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            if (!otherFactoryNoResp.isSuccess()) {
                LOGGER.info("已经同步了工厂、但设置工厂单号失败，orderId,{},工厂单号{}", orderId, productNo);
                messageSendService.oredrLogPackage(orderId, "订单同步工厂", "同步失败:" + otherFactoryNoResp.getErrMessage(),
                    JSONObject.toJSONString(response.getData()));
                throw ThirdPartyException.buildException(otherFactoryNoResp.getErrCode(),
                    otherFactoryNoResp.getErrMessage());
            }
            messageSendService.oredrLogPackage(orderId, "订单同步工厂", "同步成功:" + productNo,
                JSONObject.toJSONString(response.getData()));
            return Response.buildSuccess();
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            messageSendService.oredrLogPackage(orderId, "订单同步工厂", "同步出现异常，异常信息:" + e.getMessage(), orderId.toString());
            throw ThirdPartyException.buildException(e.getCode(), e.getMsg());
        } finally {
            // 释放锁
            orderSyncFactoryCache.remove(TenantContext.getTenantNo() + ":" + orderId);
            autoReleaseLock.close();
        }

    }

    /**
     * 重试触发第三方物流信息同步
     *
     * @param id
     *            订单发货流水id
     * @return
     */
    @Override
    public Response syncLogisticsToThird(Integer id) {
        com.bat.dubboapi.order.common.Response<OrderLogisticsSyncParam> syncParamResponse =
            orderDeliveryServiceRpc.queryLogisticsParamByOrderDeliveryBillId(id);
        if (syncParamResponse == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!syncParamResponse.isSuccess()) {
            if (syncParamResponse.getErrCode().equals(CommonErrorCode.ORDER_DELIVER_SYNC_REPEAT)) {
                return Response.buildSuccess();
            }
            throw ThirdPartyException.buildException(syncParamResponse.getErrCode(), syncParamResponse.getErrMessage());
        }
        OrderLogisticsSyncParam syncParam = syncParamResponse.getData();
        OrderLogistics orderLogistics = OrderOpenConvertor.toOrderLogistics(syncParam);
        com.bat.dubboapi.thirdparty.common.Response<Short> response =
            thirdPartyOrderDeliverServiceRpc.syncLogisticsToThirdParty(syncParam.getOrderId(),
                syncParam.getOtherOrderNo(), syncParam.getDistributionName(), orderLogistics,
                syncParam.getDistributionId(), syncParam.getPlatform());
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        Short pushStatus = response.getData();
        LOGGER.info(syncParam.getOrderId() + "同步三方物流信息、返回" + pushStatus);
        if (pushStatus != null && pushStatus == OrderDeliveryConstant.ORDER_DELIVERY_STATUS_SUCCESS.shortValue()) {
            messageSendService.orderDeliverBillLogPackage(id, null, "同步物流到第三方", "同步成功",
                JSONObject.toJSONString(syncParam));
        }
        if (pushStatus != null && pushStatus == OrderDeliveryConstant.ORDER_DELIVERY_STATUS_FAIL.shortValue()) {
            messageSendService.orderDeliverBillLogPackage(id, null, "同步物流到第三方", "同步失败",
                JSONObject.toJSONString(syncParam));
        }
        if (pushStatus != null && pushStatus == OrderDeliveryConstant.ORDER_DELIVERY_STATUS_REJECT.shortValue()) {
            messageSendService.orderDeliverBillLogPackage(id, null, "同步物流到第三方", "同步失败:不允许修改订单物流单号",
                JSONObject.toJSONString(syncParam));
        }
        com.bat.dubboapi.order.common.Response updatePushStatusResp =
            orderDeliveryServiceRpc.updatePushStatus(id, pushStatus);
        return BeanUtils.copy(updatePushStatusResp, Response.class);
    }

    /**
     * 同步柔性发货单到ERP(销售单、采购单)
     *
     * @param id
     * @return
     */
    @Override
    public Response syncDiyDeliveryOrderToERP(Integer id) {
        // 分布式锁(单个单个单同步)
        AutoReleaseLock autoReleaseLock = getSyncErpPurchaseAndOutboundOrderLock();
        try {
            com.bat.dubboapi.order.common.Response<OrderDeliverSyncErpParam> erpParamResponse =
                orderDeliveryServiceRpc.querySyncErpParamByOrderDeliverId(id);
            if (erpParamResponse == null) {
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            if (!erpParamResponse.isSuccess()) {
                if (erpParamResponse.getErrCode().equals(CommonErrorCode.ORDER_DELIVER_SYNC_REPEAT)) {
                    return Response.buildSuccess();
                }
                throw ThirdPartyException.buildException(erpParamResponse.getErrCode(),
                    erpParamResponse.getErrMessage());
            }
            messageSendService.orderDeliverBillLogPackage(id, null, "同步柔性发货单到ERP(销售单、采购单)",
                "同步成功:" + JSONObject.toJSONString(erpParamResponse), id.toString());
            LOGGER.info("同步柔性发货单到ERP(销售单、采购单)，订单服务查询返回组装参数,{}", JSON.toJSONString(erpParamResponse));
            OrderDeliverSyncErpParam paramResponseData = erpParamResponse.getData();
            List<OrderGoodsDetailCountDTO> orderGoodsDetailDTOS = paramResponseData.getOrderGoodsDetailDTOS();
            List<OrderGoodsDetailDTO> detailDTOS = BeanUtils.copyList(orderGoodsDetailDTOS, OrderGoodsDetailDTO.class);
            String purchaseNo = null;
            if (paramResponseData.getSyncPurchaseFlag()) {
                // 采购单
                com.bat.dubboapi.thirdparty.common.Response<String> response =
                    thirdPartyOrderServiceErpRpc.syncErpPurchase(paramResponseData.getOrderErpNo(),
                        paramResponseData.getManufactors(), detailDTOS, paramResponseData.getTime());
                if (!response.isSuccess()) {
                    throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
                }
                purchaseNo = response.getData();
                String outBoundNo = null;
                if (paramResponseData.getSyncOutbountFlag()) {
                    // 出库单
                    com.bat.dubboapi.thirdparty.common.Response<String> response2 =
                        thirdPartyOrderServiceErpRpc.syncOutStockToERP(paramResponseData.getOrderErpNo(),
                            paramResponseData.getManufactors(), detailDTOS, paramResponseData.getDeliverTime(),
                            paramResponseData.getDistributionAmount(), paramResponseData.getDistributionName(),
                            paramResponseData.getLogisticNo(), paramResponseData.getTime());
                    if (!response2.isSuccess()) {
                        throw ThirdPartyException.buildException(response2.getErrCode(), response2.getErrMessage());
                    }
                    outBoundNo = response2.getData();
                }
                // 设置ERP出库单和采购单号
                orderDeliveryServiceRpc.setErpPurchaseNoAndOutBoundNo(id, outBoundNo, purchaseNo);
            }
        } finally {
            autoReleaseLock.close();
        }
        return Response.buildSuccess();
    }

    /**
     * 循环获取委外出库单同步erp锁
     *
     * @return
     */
    public AutoReleaseLock getSyncErpPurchaseAndOutboundOrderLock() {
        // 分布式锁(单个单个单同步,且每个单同步间隔5秒)
        AutoReleaseLock autoReleaseLock =
            syncErpOutboundCache.tryLock(TenantContext.getTenantNo() + ":" + "syncErpPurchaseAndOutboundOrder",
                erpConfig.getSyncErpPurchaseAndOutboundOrderTime(), TimeUnit.SECONDS);
        if (autoReleaseLock == null) {
            // 加锁，等待5秒循环
            try {
                Thread.sleep(erpConfig.getSyncErpPurchaseAndOutboundOrderTime() * 1000);
                autoReleaseLock = getSyncErpPurchaseAndOutboundOrderLock();
            } catch (Exception e) {
                autoReleaseLock = getSyncErpPurchaseAndOutboundOrderLock();
            }
        }
        return autoReleaseLock;
    }

    /**
     * 手动触发生成标签
     *
     * @param orderGoodsDiyId
     * @return
     */
    @Override
    public com.bat.dubboapi.order.common.Response createLable(Integer orderGoodsDiyId) {
        return orderGoodsDiyServiceRpc.createLable(orderGoodsDiyId);
    }

    @Override
    public void createOrderLatelyNullLabel() {
        com.bat.dubboapi.order.common.Response<List<OrderGoodsDiyRpcDTO>> orderGoodsDiyResponse =
            orderGoodsDiyServiceRpc.findLatelyNullLabel();
        if (!orderGoodsDiyResponse.isSuccess() || orderGoodsDiyResponse.getData() == null
            || orderGoodsDiyResponse.getData().size() == 0) {
            return;
        }
        List<OrderGoodsDiyRpcDTO> orderGoodsDiyRpcDTOS = orderGoodsDiyResponse.getData();
        for (OrderGoodsDiyRpcDTO orderGoodsDiyRpcDTO : orderGoodsDiyRpcDTOS) {
            try {
                orderGoodsDiyServiceRpc.createLable(orderGoodsDiyRpcDTO.getId());
            } catch (Exception e) {
                LOGGER.info("标签生成失败:{}", e);
            }
        }
    }

    @Override
    public Response syncErpExpressNo() {
        orderDeliverBillExe.syncErpExpressNo();
        return Response.buildSuccess();
    }

    /**
     * 批量同步订单到erp
     */
    @Override
    public void orderSyncToErp(String startTime) {
        com.bat.dubboapi.order.common.Response<List<Integer>> response = orderServiceRpc.getNotErpOrderIds(CommonUtil.stringToDateTime(startTime));
        if (response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            List<Integer> orderIds = response.getData();
            orderIds.forEach(orderId -> {
                try {
                    adminOrderServiceI.syncOrderToERP(orderId);
                } catch (Exception e) {
                    LOGGER.info("同步订单到erp失败,{}", orderId + e.getMessage());
                }
            });
        }
    }

    /**
     * 批量同步erp柔性订单出库单
     */
    @Override
    public void orderPurchaseAndOutboundSyncToErp(String startTime) {
        String orderPurchaseAndOutboundSyncToErp =
            (String)cache.get(TenantContext.getTenantNo() + ":" + "orderPurchaseAndOutboundSyncToErp");
        if (StringUtils.isNotBlank(orderPurchaseAndOutboundSyncToErp)) {
            return;
        }
        cache.put(TenantContext.getTenantNo() + ":" + "orderPurchaseAndOutboundSyncToErp",
            "orderPurchaseAndOutboundSyncToErp", thirdPartyConfig.getOrderPurchaseAndOutboundSyncToErpTime(),
            TimeUnit.HOURS);
        try {
            com.bat.dubboapi.order.common.Response<List<Integer>> response =
                orderDeliveryServiceRpc.getNotErpOrderDeliverBillIds(CommonUtil.stringToDateTime(startTime));
            if (response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
                List<Integer> ids = response.getData();
                ids.forEach(id -> {
                    try {
                        adminOrderServiceI.syncDiyDeliveryOrderToERP(id);
                    } catch (Exception e) {
                        LOGGER.error("同步订单发货单到erp失败,{}", id + e.getMessage());
                    }
                });
            }
        } finally {
            // 不管是否成功都结束同步标识
            if (StringUtils.isBlank(orderPurchaseAndOutboundSyncToErp)) {
                cache.remove(TenantContext.getTenantNo() + ":" + "orderPurchaseAndOutboundSyncToErp");
            }
        }
    }

    @Override
    public void autoCloseOrderJobHandler() {
        com.bat.dubboapi.order.common.Response response = orderServiceRpc.orderCancelByXXLJob();
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * <h2>按订单 ID 同步物流</h2>
     *
     * @param orderId
     * @return
     */
    @Override
    public Response synchronizedLogisticsByOrderID(Integer orderId) {
        // 根据订单id查询订单工厂信息
        com.bat.dubboapi.order.common.Response<FactoryOrderQueCmd> queCmd =
            orderServiceRpc.orderFactoryInformationByOrderId(orderId);
        if(queCmd.isSuccess()){
            FactoryOrderQueCmd data = queCmd.getData();
            if(data.getFactoryEnum().name().equals(FactoryEnum.KDS_FK.name())){
                return feiKuaiFactoryService.synchronizedLogisticsByOrderID(data);
            }else if (data.getFactoryEnum().equals(FactoryEnum.LHW.name())){
                return haixingFactoryService.synchronizedLogisticsByOrderID(data);
            }else {
                throw  ThirdPartyException.buildException(B_THIRDPARTY_SYNCHRONIZED_LOGISTICS_UNABLE_ERROR, MessageUtils.get(B_THIRDPARTY_SYNCHRONIZED_LOGISTICS_UNABLE_ERROR));
            }
        }else {
            throw  ThirdPartyException.buildException(queCmd.getErrCode(),queCmd.getErrMessage());
        }
    }

    /**
     * <h2>根据工厂编码 同步物流</h2>
     *
     * @param factoryCode
     * @return
     */
    @Override
    public Response synchronizedLogisticsByFactoryCode(String factoryCode,String startTime) {
        // 根据订单id查询订单工厂信息
        com.bat.dubboapi.order.common.Response<List<FactoryOrderQueCmd>> response =
                orderServiceRpc.orderFactoryInformationByFactoryCode(factoryCode, CommonUtil.stringToDateTime(startTime));
        if(response.isSuccess() && !CollectionUtils.isEmpty(response.getData())){
            List<FactoryOrderQueCmd> queCmds = response.getData();
            queCmds.forEach(queCmd ->{
                try {
                    if (queCmd.getFactoryEnum().name().equals(FactoryEnum.KDS_FK.name())) {
                        feiKuaiFactoryService.synchronizedLogisticsByOrderID(queCmd);
                    } else if (queCmd.getFactoryEnum().equals(FactoryEnum.LHW.name())) {
                        haixingFactoryService.synchronizedLogisticsByOrderID(queCmd);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return Response.buildSuccess();
    }

    /**
     * 
     * <h2>根据订单号查询工厂单号是否匹配</h2>
     * 
     * @param orderNo
     */
    public Response factoryTrackingNumber(String orderNo, String expressNo) {
        // 根据订单号查询订单id
        Integer orderId = orderDeliveryServiceRpc.queryFactoryTrackingNumber(orderNo).getData();
        // 根据订单id查询订单工厂信息
        com.bat.dubboapi.order.common.Response<FactoryOrderQueCmd> queCmd =
            orderServiceRpc.orderFactoryInformationByOrderId(orderId);

        return haixingFactoryService.factoryTrackingNumber(queCmd.getData(), expressNo);
    }
}
