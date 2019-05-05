package com.bat.promotion.service.message;

import com.bat.promotion.mq.dto.CommonLogDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/7/1 11:55
 */
public class MessageConvertor {

    public static CommonLogDTO toCommonLogDTOList(String businessModule, String businessFunction, Integer businessId,
                                                  String operateSource, Integer operateId, String operator, String operateType, String operateDes,
                                                  String operateData, Date time) {
        if(StringUtils.isBlank(operator)){
            operator="系统";
        }
        CommonLogDTO dto = new CommonLogDTO();
        dto.setBusinessModule(businessModule);
        dto.setBusinessFunction(businessFunction);
        dto.setBusinessId(businessId);
        dto.setOperateSource(operateSource);
        dto.setOperateId(operateId);
        dto.setOperator(operator);
        dto.setOperateType(operateType);
        dto.setOperateDes(operateDes);
        dto.setOperateData(operateData);
        dto.setOperateTime(time);
        return dto;
    }

}
