package com.bat.thirdparty.sms.service;

import com.bat.thirdparty.sms.service.executor.SmsExe;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.sms.api.ThirdPartySmsServiceRpc;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyCodeRpcCmd;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyRpc;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/25 14:25
 */
@DubboService
public class ThirdPartySmsServiceRpcImpl implements ThirdPartySmsServiceRpc {

    @Resource
    private SmsExe smsExe;

    /**
     * 获取手机验证码
     *
     * @param cmd
     * @return
     */
    @Override
    public Response<Boolean> getPhoneVerifyCode(UserPhoneVerifyRpc cmd) {
        return smsExe.getPhoneVerifyCode(cmd);
    }

    /**
     * 验证手机验证码
     *
     * @param cmd
     * @return
     */
    @Override
    public Response<Boolean> checkPhoneVerifyCode(UserPhoneVerifyCodeRpcCmd cmd) {
        return smsExe.checkPhoneVerifyCode(cmd);
    }
}
