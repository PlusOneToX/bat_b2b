package com.bat.distributor.api.base;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/2 10:32
 */
@Data
public class DistributorException extends RuntimeException {

    private String code;
    private String msg;

    public static DistributorException buildException(String errCode) {
        DistributorException exception = new DistributorException();
        exception.setCode(errCode);

        return exception;
    }

    public static DistributorException buildException(String errCode, String msg) {
        DistributorException exception = new DistributorException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

}
