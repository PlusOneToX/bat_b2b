package com.bat.promotion.api.base;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/2 10:32
 */
@Data
public class PromotionException extends RuntimeException {

    private String code;
    private String msg;

    public static PromotionException buildException(String errCode) {
        PromotionException exception = new PromotionException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static PromotionException buildException(String errCode, String msg) {
        PromotionException exception = new PromotionException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

}
