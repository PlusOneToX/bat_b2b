package com.bat.dubboapi.thirdparty.dy.dto.data;

import java.io.Serializable;

/**
 * 沙漠
 */
public class DyMiniProgramAuthorizeRpcDTO implements Serializable {
    private String phone;
    private String countryCode;
    private String openid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
