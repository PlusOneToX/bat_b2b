package com.bat.thirdparty.wx.api.response;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 10:44
 */
@Data
public class WxProgramAuthorizeResponse {
    private String session_key;
    private String openid;
    private String unionid;
    private String errmsg;
    private String errcode;
}
