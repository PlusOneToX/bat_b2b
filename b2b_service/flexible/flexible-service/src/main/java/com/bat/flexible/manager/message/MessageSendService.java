package com.bat.flexible.manager.message;

import com.bat.flexible.api.message.MessageSendServiceI;
import com.bat.flexible.mq.dto.CommonLogDTO;
import com.bat.flexible.mq.service.SenderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 沙漠
 */
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;


    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
                          Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
                operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

}
