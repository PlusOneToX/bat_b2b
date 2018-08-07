package com.bat.financial.api.message;

import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Date;

public interface MessageSendServiceI {
    void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time);

    boolean test(String request, MessageHeaderAccessor accessor);
}
