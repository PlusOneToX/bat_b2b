package com.bat.thirdparty.common.base;

import com.bat.thirdparty.common.util.MessageUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/2 10:32
 */
@Data
public class ThirdPartyException extends RuntimeException {

    private String code;
    private String msg;

    public static ThirdPartyException buildException(String errCode) {
        ThirdPartyException exception = new ThirdPartyException();
        exception.setCode(errCode);
        exception.setMsg(errCode);
        String errorMsg = MessageUtils.get(errCode);
        if(StringUtils.isNotBlank(errorMsg)){
            exception.setMsg(errorMsg);
        }
        return exception;
    }

    public static ThirdPartyException buildException(String errCode, String msg) {
        ThirdPartyException exception = new ThirdPartyException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

}
