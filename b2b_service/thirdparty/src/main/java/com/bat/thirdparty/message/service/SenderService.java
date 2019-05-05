package com.bat.thirdparty.message.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.message.api.Source;
import com.bat.thirdparty.tenant.TenantContext;

/**
 * 沙漠
 */
@Service
public class SenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderService.class);

    @Resource
    private Source source;

    public void send(String msg) {
        source.output()
            .send(MessageBuilder.withPayload(msg).setHeader("tenantNo", TenantContext.getTenantNo()).build());
    }

    /**
     * 非延迟消息发送
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
        source.output().send(message);
    }

    /**
     * 延迟消息发送
     * 
     * @param msg
     * @param tag
     * @param key
     * @param delayLevel
     * @param <T>
     */
    public <T> void sendObject(T msg, String tag, String key, Integer delayLevel) {
        Message message = MessageBuilder.withPayload(msg).setHeader(MessageConst.PROPERTY_TAGS, tag)
            .setHeader(MessageConst.PROPERTY_KEYS, key).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .setHeader("tenantNo", TenantContext.getTenantNo()).build();
        source.output().send(message);
    }

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public Response query(String key) {
        QueryResult queryResult = null;
        try {
            queryResult = defaultMQProducer.getDefaultMQProducerImpl().queryMessage("order-service-topic", key, 10,
                new Date().getTime() - (152 * 60 * 60 * 1000), new Date().getTime() + (2 * 60 * 60 * 1000));
            LOGGER.info("队列查询回来{}", JSON.toJSONString(queryResult));
            if (queryResult == null) {
                return Response.buildSuccess();
            }
            List<MessageExt> messageList = queryResult.getMessageList();
            MessageExt messageExt = messageList.get(0);
            LOGGER.info(JSON.toJSONString(messageExt));
            LOGGER.info(messageExt.getMsgId());
            byte[] bytes = messageExt.getBody();
            LOGGER.info(new String(bytes));

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Response.of(JSON.toJSONString(queryResult));
    }
}
