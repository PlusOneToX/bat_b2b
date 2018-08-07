package com.bat.financial.mq.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.Tenant.TenantContext;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.bat.financial.mq.api.Source;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/3 21:41
 */
@Service
@Slf4j
public class SenderService {

    public static Integer reTryCount = 5;

    @Resource
    private Source source;

    public void send(String msg) {
        source.financialOutput()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    public <T> boolean sendObject(T msg, String tag) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.financialOutput().send(message);
    }

    public <T> boolean sendObject(T msg, MessageHeaderAccessor accessor) {
        Message message = MessageBuilder.withPayload(msg).setHeaders(accessor)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.financialOutput().send(message);
    }

    public <T> boolean sendObject(T msg, List<String> tags) {
        MessageBuilder<T> message = MessageBuilder.withPayload(msg);
        tags.forEach(tag -> message.setHeader(MessageConst.PROPERTY_TAGS, tag));
        Message<T> build = message.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        return source.financialOutput().send(build);
    }

    public <T> boolean sendObject(T msg, String tag, String key) {
        log.info("租户编码:{}", TenantContext.getTenantNo());
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        boolean flag = false;
        int i = 0;
        while (!flag && i++ < reTryCount) {
            log.info("第{}次发送消息，Key:{}", i, key);
            flag = source.financialOutput().send(message);
        }
        return flag;
    }
}
