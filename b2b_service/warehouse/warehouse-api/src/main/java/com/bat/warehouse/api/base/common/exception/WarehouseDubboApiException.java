package com.bat.warehouse.api.base.common.exception;


import com.bat.warehouse.api.base.util.MessageUtils;
import lombok.Data;

@Data
public class WarehouseDubboApiException extends RuntimeException{

    private String code;

    private String msg;



    public static WarehouseDubboApiException buildException(String errorCode){
        WarehouseDubboApiException warehouseDubboApiException = new WarehouseDubboApiException();
        warehouseDubboApiException.setCode(errorCode);
        String errorMessage =  MessageUtils.get(errorCode);
        if( errorMessage!=null){
            warehouseDubboApiException.setMsg(errorMessage);
        }
        return warehouseDubboApiException;
    }

    public static WarehouseDubboApiException buildException(String errorCode,String msg){
        WarehouseDubboApiException warehouseDubboApiException = new WarehouseDubboApiException();
        warehouseDubboApiException.setCode(errorCode);
        warehouseDubboApiException.setMsg(msg);
        return warehouseDubboApiException;
    }

}
