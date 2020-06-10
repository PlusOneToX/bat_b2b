package com.bat.flexible.mq.service;

import javax.annotation.Resource;

import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.mq.api.Source;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/3 21:41
 */
@Service
public class SenderService {

    @Resource
    private Source source;

    public void send(String msg) {
        source.output()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    public <T> void sendObject(T msg, String tag, String key) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.output().send(message);
    }
}
