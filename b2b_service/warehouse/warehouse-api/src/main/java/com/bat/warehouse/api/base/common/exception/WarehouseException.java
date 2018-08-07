package com.bat.warehouse.api.base.common.exception;


import com.bat.warehouse.api.base.util.MessageUtils;
import lombok.Data;

@Data
public class WarehouseException extends RuntimeException{

    private String code;
    private String msg;

    public WarehouseException() {
    }

    public WarehouseException(String message) {
         super(message);
    }

    public static WarehouseException buildException(String errCode) {
        WarehouseException exception = new WarehouseException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static WarehouseException buildException(String errCode, String msg) {
        WarehouseException exception = new WarehouseException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }
}
