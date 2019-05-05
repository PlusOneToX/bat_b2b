package com.bat.thirdparty.message;

import java.util.Map;

import javax.annotation.Resource;

import com.bat.thirdparty.message.api.Sink;
import com.bat.thirdparty.message.dto.order.OrderCancelDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.thirdparty.common.config.ThirdPartyConfig;
import com.bat.thirdparty.erp.api.request.CreateReceiveBillEntryRequest;
import com.bat.thirdparty.order.api.AdminOrderServiceI;
import com.bat.thirdparty.tenant.TenantContext;

/**
 * 第三方订单服务消息消费者
 */
@Service
public class OrderMessageConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderMessageConsumer.class);

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    @Autowired
    private ThirdPartyOrderServiceErpRpc thirdPartyOrderServiceErpRpc;
    @Resource
    private ThirdPartyConfig config;

    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'syncErpFlexbileOrderDelivery'")
    public void rechargeSuccessCallback(@Headers Map headers, @Payload CreateReceiveBillEntryRequest request) {
        LOGGER.info("来啦：" + JSON.toJSONString(request));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        // thirdPartyFinancialServiceErpRpc.syncVoucherAutoMqResp(request);
        TenantContext.removeTenantNo();
    }

    /**
     * 同步销售单到ERP
     * 
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderAsynErp'")
    public void orderAsynErpOld(@Headers Map headers, @Payload Integer orderId) {
        LOGGER.info("消费消息、开始同步ERP：" + orderId);
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.syncOrderToERP(orderId);
        TenantContext.removeTenantNo();
    }

    /**
     * 同步销售单到ERP
     *
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_ERP_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderAsynErp'")
    public void orderAsynErpNew(@Headers Map headers, @Payload Integer orderId) {
        LOGGER.info("消费消息、开始同步ERP：" + orderId);
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.syncOrderToERP(orderId);
        TenantContext.removeTenantNo();
    }

    /**
     * B2B->>ERP取消订单
     * 
     * @param orderCancelDTO
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderCancelErp'")
    public void orderCancelOld(@Headers Map headers, @Payload OrderCancelDTO orderCancelDTO) {
        LOGGER.info("消费消息、开始向ERP取消订单/作废：{}", JSON.toJSONString(orderCancelDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        thirdPartyOrderServiceErpRpc.cancelOrder(orderCancelDTO.getOrderErpNo(), orderCancelDTO.getErpCancelType(),
            null,orderCancelDTO.getRemark());
        TenantContext.removeTenantNo();
    }

    /**
     * B2B->>ERP取消订单
     *
     * @param orderCancelDTO
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_ERP_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderCancelErp'")
    public void orderCancelNew(@Headers Map headers, @Payload OrderCancelDTO orderCancelDTO) {
        LOGGER.info("消费消息、开始向ERP取消订单/作废：{}", JSON.toJSONString(orderCancelDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        thirdPartyOrderServiceErpRpc.cancelOrder(orderCancelDTO.getOrderErpNo(), orderCancelDTO.getErpCancelType(),
            null,orderCancelDTO.getRemark());
        TenantContext.removeTenantNo();
    }

}
