package com.bat.platform.api.base;

import com.bat.platform.api.utils.MessageUtils;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/2 10:32
 */
@Data
public class PlatformException extends RuntimeException {

    private String code;
    private String msg;

    public static PlatformException buildException(String errCode) {
        PlatformException exception = new PlatformException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static PlatformException buildException(String errCode, String msg) {
        PlatformException exception = new PlatformException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

}
