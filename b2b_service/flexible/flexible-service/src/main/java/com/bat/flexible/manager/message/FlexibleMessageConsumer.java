package com.bat.flexible.manager.message;

import java.util.Map;

import javax.annotation.Resource;

import com.bat.flexible.manager.message.executor.MessageCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.mq.api.Sink;

/**
 * 柔性服务消息消费者
 */
@Service
public class FlexibleMessageConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(FlexibleMessageConsumer.class);

    @Autowired
    private MessageCmdExe messageCmdExe;

    @Resource
    private FlexibleConfig config;

    /**
     * 生成标签
     * 
     * @param orderLableCmd
     */
    @StreamListener(value = Sink.FLEXIBLE_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderGoodsDiyLabel'")
    public void orderGoodsDiyLabel(@Headers Map headers, @Payload OrderLableCmd orderLableCmd) {
        LOGGER.info("消费消息、开始向定制订单生成标签：{}", JSON.toJSONString(orderLableCmd));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        messageCmdExe.createDiyLabel(orderLableCmd);
        TenantContext.removeTenantNo();
    }

    /**
     * C端订单取消订单、还原兑换码状态
     * 
     * @param orderId
     */
    @StreamListener(value = Sink.FLEXIBLE_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderCancelExchangeCode'")
    public void orderCancelExchangeCode(@Headers Map headers, @Payload Integer orderId) {
        LOGGER.info("消费消息、核销订单取消、还原兑换码状态：{}", orderId);
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        messageCmdExe.orderCancelExchangeCode(orderId);
        TenantContext.removeTenantNo();
    }

}
