package com.bat.order.api.message;

import java.util.Date;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/10 22:07
 */
public interface MessageSendServiceI {
    void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time);
}
