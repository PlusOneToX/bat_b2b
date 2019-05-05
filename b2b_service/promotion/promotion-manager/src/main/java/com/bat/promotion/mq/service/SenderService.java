package com.bat.promotion.mq.service;

import javax.annotation.Resource;

import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.bat.promotion.Tenant.TenantContext;
import com.bat.promotion.mq.api.Source;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/3 21:41
 */
@Service
public class SenderService {

    @Resource
    private Source source;

    public void send(String msg) {
        source.promotionOutput()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    public <T> void sendObject(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.promotionOutput().send(message);
    }

    /**
     * 延迟消息发送(订单一般服务性消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObject(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.promotionOutput().send(message);
    }
}
