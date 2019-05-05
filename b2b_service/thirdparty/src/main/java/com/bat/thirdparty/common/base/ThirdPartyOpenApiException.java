package com.bat.thirdparty.common.base;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/2 10:32
 */
@Data
public class ThirdPartyOpenApiException extends RuntimeException {

    public ThirdPartyOpenApiException(String message) {

        super(message);
    }
}
