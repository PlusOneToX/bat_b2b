package com.bat.dubboapi.thirdparty.wx.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/8 8:47
 */

public class WxOfficialProgramAuthorizeRpcCmd implements Serializable {
    private String code;
    private String appid;
    private String secret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
