package com.bat.system.web.base;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 13:53
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String errCode;
    private String errMessage;
    private T data;

    public Response() {}

    public static <T> Response<T> of(T data) {
        Response<T> response = new Response();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static Response buildSuccess() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response buildFailure(String errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }
}