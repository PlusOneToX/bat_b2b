package com.bat.warehouse.mq.service;

import javax.annotation.Resource;

import com.bat.warehouse.Tenant.TenantContext;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.bat.warehouse.mq.api.Source;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/3 21:41
 */
@Service
public class SenderService {

    @Resource
    private Source source;

    public void send(String msg) {
        source.warehouseOutput()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    public <T> void sendObject(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.warehouseOutput().send(message);
    }
}
