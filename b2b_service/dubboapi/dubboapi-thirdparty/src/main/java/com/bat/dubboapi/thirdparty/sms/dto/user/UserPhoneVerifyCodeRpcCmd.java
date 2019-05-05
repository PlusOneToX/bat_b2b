package com.bat.dubboapi.thirdparty.sms.dto.user;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/20 8:50
 */
public class UserPhoneVerifyCodeRpcCmd extends UserPhoneVerifyRpc {
    /**
     * 验证码
     */
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
