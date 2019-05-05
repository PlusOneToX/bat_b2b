package com.bat.thirdparty.order.api.dto.common;

import lombok.Data;

@Data
public class ThirdOrderResp {

    private String msg;

    private String userMsg;

    private Integer code;

    private Object data;

}
