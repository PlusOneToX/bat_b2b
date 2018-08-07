package com.bat.goods.api.base;

import com.bat.goods.api.utils.MessageUtils;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/2 10:32
 */
@Data
public class GoodsException extends RuntimeException {

    private String code;
    private String msg;

    public static GoodsException buildException(String errCode) {
        GoodsException exception = new GoodsException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static GoodsException buildException(String errCode, String msg) {
        GoodsException exception = new GoodsException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

}
