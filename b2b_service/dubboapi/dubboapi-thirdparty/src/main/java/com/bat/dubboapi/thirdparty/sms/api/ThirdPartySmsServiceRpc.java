package com.bat.dubboapi.thirdparty.sms.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyCodeRpcCmd;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyRpc;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/25 13:52
 */
public interface ThirdPartySmsServiceRpc {

    /**
     * 获取验证码
     *
     * @param cmd
     * @return
     */
    Response<Boolean> getPhoneVerifyCode(UserPhoneVerifyRpc cmd);

    /**
     * 验证验证码
     *
     * @param cmd
     * @return
     */
    Response<Boolean> checkPhoneVerifyCode(UserPhoneVerifyCodeRpcCmd cmd);

}
