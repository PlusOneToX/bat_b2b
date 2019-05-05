package com.bat.promotion.service.message;

import javax.annotation.Resource;

import com.bat.promotion.api.message.MessageSendServiceI;
import com.bat.promotion.mq.dto.CommonLogDTO;
import com.bat.promotion.service.common.PromotionConfig;
import org.springframework.stereotype.Component;

import com.bat.promotion.mq.service.SenderService;

import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/4 9:07
 */
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;
    @Resource
    private PromotionConfig promotionConfig;

    /**
     * 发送通用日志消息
     */
    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
                          Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
                operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

}
