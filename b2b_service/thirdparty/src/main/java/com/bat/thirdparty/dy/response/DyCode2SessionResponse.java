package com.bat.thirdparty.dy.response;

import lombok.Data;

/**
 * 沙漠
 */
@Data
public class DyCode2SessionResponse {
    private int err_no;
    private String err_tips;
    private Body data;

    @Data
    public static class Body {
        private String session_key;
        private String openid;
        private String anonymous_openid;
        private String unionid;
        private String dopenid;
    }
}
