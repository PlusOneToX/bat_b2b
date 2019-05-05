package com.bat.thirdparty.suning.request;

import lombok.Data;

@Data
public class CommonUrlRequest {

    private String method;

    private String app_key;

    private String sign;

}
