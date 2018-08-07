package com.bat.financial.api.base;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

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

    public static Response buildFailure(String errCode) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        String msg = MessageUtils.get(errCode);
        if(StringUtils.isNotBlank(msg)){
            response.setErrMessage(msg);
        }else{
            response.setErrMessage(errCode);
        }
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
