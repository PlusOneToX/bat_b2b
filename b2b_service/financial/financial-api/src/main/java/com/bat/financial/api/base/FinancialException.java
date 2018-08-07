package com.bat.financial.api.base;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 17:05
 */
@Data
public class FinancialException extends RuntimeException {
    private String code;
    private String msg;

    public static FinancialException buildException(String errCode) {
        FinancialException exception = new FinancialException();
        exception.setCode(errCode);
        return exception;
    }

    public static FinancialException buildException(String errCode, String msg) {
        FinancialException exception = new FinancialException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }
}
