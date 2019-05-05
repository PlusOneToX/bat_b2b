package com.bat.thirdparty.order.service.executor;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.distributor.ThirdDistributorPlatformApiConstant;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.order.dto.OrderCancelSyncParam;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyOutboundSyncErpCmd;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyPurchaseOrderDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.ErpPurchaseOrderOutboundCmd;
import com.bat.thirdparty.erp.config.ErpConfig;
import com.bat.thirdparty.order.api.AdminOrderServiceI;
import com.bat.thirdparty.order.api.dto.third.CancelOrderNoPushCmd;

@Component
public class OrderServiceCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceCmdExe.class);

    @DubboReference(check = false, timeout = 6000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Autowired
    private ThirdPartyOrderServiceErpRpc thirdPartyOrderServiceErpRpc;

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    @CreateCache(name = ThirdKeyConstant.FLEXIBLE_CUSTOM_DELIBERY_SYNC_ERP_PRE)
    private Cache<String, Integer> syncErpOutboundCache;
    @Resource
    private ErpConfig erpConfig;

    @Resource
    private HttpUtil httpUtil;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorPlatformApiServiceRpc distributorPlatformApiServiceRpc;

    /**
     * 推送采购销售单同步ERP
     * 
     * @param diyPurchaseOrderDTO
     */
    public void syncErpPurchase(DiyPurchaseOrderDTO diyPurchaseOrderDTO) {
        if (StringUtils.isBlank(diyPurchaseOrderDTO.getOrderErpNo())) {
            return;
        }
        Response<String> response = thirdPartyOrderServiceErpRpc.syncErpPurchase(diyPurchaseOrderDTO.getOrderErpNo(),
            diyPurchaseOrderDTO.getFactoryNo(), diyPurchaseOrderDTO.getDetailDTOList(), diyPurchaseOrderDTO.getTime());
        if (!response.isSuccess()) {
            LOGGER.error("推送采购销售单同步ERP异常,退出程序，{}", response.getErrMessage());
            return;
        }
        orderDeliveryServiceRpc.setErpPurchaseNoAndOutBoundNo(diyPurchaseOrderDTO.getId(), null, response.getData());

    }

    /**
     * 推送出库单到ERP(柔性)
     * 
     * @param diyOutboundSyncErpCmd
     */
    public void syncOutStockToERP(DiyOutboundSyncErpCmd diyOutboundSyncErpCmd) {
        Response<String> response = thirdPartyOrderServiceErpRpc.syncOutStockToERP(
            diyOutboundSyncErpCmd.getOrderErpNo(), diyOutboundSyncErpCmd.getFactoryNo(),
            diyOutboundSyncErpCmd.getDetailDTOList(), diyOutboundSyncErpCmd.getDeliverTime(),
            diyOutboundSyncErpCmd.getDistributionAmount(), diyOutboundSyncErpCmd.getDistributionName(),
            diyOutboundSyncErpCmd.getLogisticsNo(), diyOutboundSyncErpCmd.getTime());
        if (!response.isSuccess()) {
            LOGGER.error("推送出库单单同步ERP异常，{}", response.getErrMessage());
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        orderDeliveryServiceRpc.setErpPurchaseNoAndOutBoundNo(diyOutboundSyncErpCmd.getId(), response.getData(), null);
    }

    public void orderAsynFactory(Integer orderId) {
        adminOrderServiceI.syncOrderToFactory(orderId);
    }

    public void orderLogistictToThird(Integer id) {
        adminOrderServiceI.syncLogisticsToThird(id);
    }

    /**
     * 下推销售采购出库单（柔性）
     * 
     * @param erpPurchaseOrderOutboundCmd
     */
    @Async
    public void syncErpPurchaseAndOutboundOrder(String tenantNo,
        ErpPurchaseOrderOutboundCmd erpPurchaseOrderOutboundCmd) {
        TenantContext.setTenantNo(tenantNo);
        // 分布式锁(单个单个单同步)
        AutoReleaseLock syncReleaseLock = getSyncErpPurchaseAndOutboundOrderLock();
        LOGGER.error("柔性出库单开始同步时间：{}", String.valueOf(System.currentTimeMillis()));
        try {
            DiyPurchaseOrderDTO diyPurchaseOrderDTO = erpPurchaseOrderOutboundCmd.getDiyPurchaseOrderDTO();
            if (StringUtils.isBlank(diyPurchaseOrderDTO.getOrderErpNo())) {
                return;
            }
            Response<String> response = thirdPartyOrderServiceErpRpc.syncErpPurchase(
                diyPurchaseOrderDTO.getOrderErpNo(), diyPurchaseOrderDTO.getFactoryNo(),
                diyPurchaseOrderDTO.getDetailDTOList(), diyPurchaseOrderDTO.getTime());
            if (!response.isSuccess()) {
                LOGGER.error("推送采购销售单同步ERP异常,退出程序，{}", response.getErrMessage());
                return;
            }
            // 下推销售采购单成功、再下推销售出库单
            DiyOutboundSyncErpCmd diyOutboundSyncErpCmd = erpPurchaseOrderOutboundCmd.getDiyOutboundSyncErpCmd();
            Response<String> response2 = thirdPartyOrderServiceErpRpc.syncOutStockToERP(
                diyOutboundSyncErpCmd.getOrderErpNo(), diyOutboundSyncErpCmd.getFactoryNo(),
                diyOutboundSyncErpCmd.getDetailDTOList(), diyOutboundSyncErpCmd.getDeliverTime(),
                diyOutboundSyncErpCmd.getDistributionAmount(), diyOutboundSyncErpCmd.getDistributionName(),
                diyOutboundSyncErpCmd.getLogisticsNo(), diyOutboundSyncErpCmd.getTime());
            if (!response2.isSuccess()) {
                LOGGER.error("推送出库单单同步ERP异常，{}", response2.getErrMessage());
                throw ThirdPartyException.buildException(response2.getErrCode(), response2.getErrMessage());
            }
            orderDeliveryServiceRpc.setErpPurchaseNoAndOutBoundNo(diyOutboundSyncErpCmd.getId(), response2.getData(),
                response.getData());
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            LOGGER.error("柔性订单同步销售采购单和出库单到ERP异常{}", e.getMsg());
            throw ThirdPartyException.buildException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("柔性订单同步销售采购单和出库单到ERP系统异常{}", e.getMessage());
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        } finally {
            syncReleaseLock.close();
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 循环获取委外出库单同步erp锁
     * 
     * @return
     */
    public AutoReleaseLock getSyncErpPurchaseAndOutboundOrderLock() {
        // 分布式锁(单个单个单同步)
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
     * 同步取消订单到第三方
     */
    public void pushCancelOrderNoToThird(Integer orderId) {
        com.bat.dubboapi.order.common.Response<OrderCancelSyncParam> syncParamResponse =
            orderDeliveryServiceRpc.queryCancelOrderParamByOrderId(orderId);
        if (syncParamResponse == null) {
            LOGGER.info("订单取消回传第三方-访问订单服务异常");
            return;
        }
        if (!syncParamResponse.isSuccess()) {
            LOGGER.info("订单取消回传第三方-订单服务返回失败");
            return;
        }
        OrderCancelSyncParam syncParam = syncParamResponse.getData();
        com.bat.dubboapi.distributor.common.Response<DistributorPlatformApiRpcDTO> distributorApiResponse =
            distributorPlatformApiServiceRpc.getByDistributorIdAndApiTypeAndPlatform(syncParam.getDistributorId(),
                ThirdDistributorPlatformApiConstant.Distributor_SYS_PLATFORM_API_TYPE_ORDER_CANCEL,
                syncParam.getPlatform());
        if (!distributorApiResponse.isSuccess()) {
            LOGGER.error("订单取消回传第三方-找不到分销商配置的取消订单url");
            return;
        }
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO = distributorApiResponse.getData();
        if (distributorPlatformApiRpcDTO == null) {
            LOGGER.error("订单取消回传第三方-该分销商尚未配置订单取消，直接结束程序，需要开启请与客服沟通处理");
            return;
        }
        String url = distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri();

        LOGGER.error("订单取消回传第三方-请求地址:{}", url);

        CancelOrderNoPushCmd cancelOrderNoPushCmd = new CancelOrderNoPushCmd();

        cancelOrderNoPushCmd.setOtherOrderNo(syncParam.getOtherOrderNo());

        cancelOrderNoPushCmd.setOrderNo(syncParam.getOrderNo());
        cancelOrderNoPushCmd.setCancelFlag(ThirdCommonConstant.COMMON_FLAG_YES);
        LOGGER.info("订单取消回传第三方-订单编号回传第三方接口参数：{}", JSON.toJSONString(cancelOrderNoPushCmd));

        ResponseBaseBean responseBaseBean =
            httpUtil.requestFor(url, HttpMethod.POST, cancelOrderNoPushCmd, ResponseBaseBean.class);
        LOGGER.info("订单取消回传第三方-订单编号回传返回字符串{}", JSON.toJSONString(responseBaseBean));

        if (responseBaseBean == null || responseBaseBean.getCode() != 0) {
            String error = "订单取消回传第三方-回传到第三方异常:" + (responseBaseBean == null ? "对方系统无反应" : responseBaseBean.getMsg());
            LOGGER.error(error);
            return;
        } else {
            LOGGER.info("订单取消回传第三方-成功");
        }

    }

}
