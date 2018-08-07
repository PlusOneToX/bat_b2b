package com.bat.order.mq.service;

import javax.annotation.Resource;

import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.bat.order.mq.api.Source;
import com.bat.order.tenant.TenantContext;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/3 21:41
 */
@Service
public class SenderService {

    @Resource
    private Source source;

    public void send(String msg) {
        source.orderOutput()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    /**
     * 非延迟消息发送(订单一般服务性消息)
     * 
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObject(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderOutput().send(message);
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
        source.orderOutput().send(message);
    }

    /**
     * 非延迟消息发送(订单服务erp同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObjectErp(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderErpOutput().send(message);
    }

    /**
     * 延迟消息发送(订单服务erp同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObjectErp(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderErpOutput().send(message);
    }

    /**
     * 非延迟消息发送(订单服务erp同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObjectErpPurchase(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderErpPurchaseOutput().send(message);
    }

    /**
     * 延迟消息发送(订单服务erp同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObjectErpPurchase(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderErpPurchaseOutput().send(message);
    }

    /**
     * 非延迟消息发送(订单服务工厂同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObjectFactory(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderFactoryOutput().send(message);
    }

    /**
     * 延迟消息发送(订单服务工厂同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObjectFactory(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderFactoryOutput().send(message);
    }

    /**
     * 非延迟消息发送(订单服务客户系统同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObjectCustomer(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderCustomerOutput().send(message);
    }

    /**
     * 延迟消息发送(订单服务客户系统同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObjectCustomer(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderCustomerOutput().send(message);
    }

    /**
     * 非延迟消息发送(订单服务订阅消息（例如微信小程序消息推送）同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param <T>
     */
    public <T> void sendObjectSubMsg(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageConst.PROPERTY_KEYS, key)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderSubOutput().send(message);
    }

    /**
     * 延迟消息发送(订单服务订阅消息（例如微信小程序消息推送）同步消息)
     *
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObjectSubMsg(T msg, String tag, String key, String delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.orderSubOutput().send(message);
    }
}
