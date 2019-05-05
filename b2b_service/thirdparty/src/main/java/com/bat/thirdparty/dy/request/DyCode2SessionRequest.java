package com.bat.thirdparty.dy.request;

import lombok.Data;

/**
 * 沙漠
 */
@Data
public class DyCode2SessionRequest {
    private String appid;
    private String secret;
    private String code;
}
