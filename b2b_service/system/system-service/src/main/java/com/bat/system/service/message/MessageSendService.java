package com.bat.system.service.message;

import java.util.Date;

import javax.annotation.Resource;

import com.bat.system.api.message.MessageSendServiceI;
import com.bat.system.mq.dto.SalesmanDTO;
import org.springframework.stereotype.Component;

import com.bat.system.mq.dto.CommonLogDTO;
import com.bat.system.mq.service.SenderService;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 9:07
 */
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;

    /**
     * 根据业务员更新分销商可视范围消息
     */
    public void updateDistributorVisibleBySalesId(SalesmanDTO salesmanDTO) {
        service.sendObject(salesmanDTO, "updateDistributorVisibleBySalesId", "updateDV-" + salesmanDTO.getSalesId());
    }

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
