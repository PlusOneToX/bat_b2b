package com.bat.system.api.base;

import lombok.Data;
/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 17:05
 */
@Data
public class SystemException extends RuntimeException {
    private String code;
    private String msg;

    public static SystemException buildException(String errCode) {
        SystemException exception = new SystemException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static SystemException buildException(String errCode, String msg) {
        SystemException exception = new SystemException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }
}
