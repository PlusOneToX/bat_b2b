package com.bat.system.mq.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.Tenant.TenantContext;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.bat.system.mq.api.Source;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/3 21:41
 */
@Service
public class SenderService {

    @Resource
    private Source source;

    public void send(String msg) {
        source.systemOutput()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    public <T> boolean sendObject(T msg, String tag) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.systemOutput().send(message);
    }

    public <T> boolean sendObject(T msg, MessageHeaderAccessor accessor) {
        Message message = MessageBuilder.withPayload(msg).setHeaders(accessor)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.systemOutput().send(message);
    }

    public <T> boolean sendObject(T msg, List<String> tags) {
        MessageBuilder<T> message = MessageBuilder.withPayload(msg);
        tags.forEach(tag -> message.setHeader(MessageConst.PROPERTY_TAGS, tag));
        Message<T> build = message.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.systemOutput().send(build);
    }

    public <T> void sendObject(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.systemOutput().send(message);
    }
}
