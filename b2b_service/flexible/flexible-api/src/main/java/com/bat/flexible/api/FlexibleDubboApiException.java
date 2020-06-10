package com.bat.flexible.api;


import com.bat.flexible.api.util.MessageUtils;
import lombok.Data;

@Data
public class FlexibleDubboApiException extends RuntimeException{

    private String code;

    private String msg;



    public static FlexibleDubboApiException buildException(String errorCode){
        FlexibleDubboApiException flexibleDubboApiException = new FlexibleDubboApiException();
        flexibleDubboApiException.setCode(errorCode);
        String errorMessage =  MessageUtils.get(errorCode);
        if( errorMessage!=null){
            flexibleDubboApiException.setMsg(errorMessage);
        }
        return flexibleDubboApiException;
    }

    public static FlexibleDubboApiException buildException(String errorCode,String msg){
        FlexibleDubboApiException flexibleDubboApiException = new FlexibleDubboApiException();
        flexibleDubboApiException.setCode(errorCode);
        flexibleDubboApiException.setMsg(msg);
        return flexibleDubboApiException;
    }

}
