package com.bat.dubboapi.thirdparty.sms.dto.user;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/20 8:50
 */
public class UserPhoneVerifyRpc implements Serializable {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机号验证码类型
     */
    private Short codeType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Short getCodeType() {
        return codeType;
    }

    public void setCodeType(Short codeType) {
        this.codeType = codeType;
    }
}
