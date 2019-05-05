package com.bat.thirdparty.sms.service.executor;

import static com.bat.thirdparty.sms.service.executor.ErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.MessageUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.spring.boot.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyCodeRpcCmd;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyRpc;
import com.bat.thirdparty.sms.config.SmsConfig;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/25 14:56
 */
@Component
public class SmsExe {

    @Resource
    private SmsConfigExe smsConfigExe;
    @Resource
    private ISmsService smsService;
    @CreateCache
    private Cache<String, String> phoneVerifyCodeCache;

    public Response<Boolean> getPhoneVerifyCode(UserPhoneVerifyRpc cmd) {
        SmsConfig smsConfig = smsConfigExe.getSmsConfig();
        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(cmd.getPhone());
        // 必填:短信签名-可在短信控制台中找到
        String smsSign = smsConfig.getSmsSign();
        request.setSignName(smsSign);
        // 必填:短信模板-可在短信控制台中找到
        String templateCodes = smsConfig.getTemplateCodes().get(cmd.getCodeType() - 1);
        request.setTemplateCode(templateCodes);
        /** 查看缓存是否有验证码,有时且有效时间超过60秒时，说明上次发送的验证码还有效 */
        String timeVerifyCode = phoneVerifyCodeCache.get(cmd.getPhone() + templateCodes);
        long time = System.currentTimeMillis();
        if (StringUtils.isNotBlank(timeVerifyCode)) {
            String[] split = timeVerifyCode.split(",");
            long codeVerifyTime = (time - Long.parseLong(split[1])) / 1000;
            if ((smsConfig.getCodeVerifyTime() - codeVerifyTime) > smsConfig.getVerifyCodeCountdown()) {
                return Response.buildSuccess();
            }
        }
        // 生成随机数存入缓存
        String verifyCode = RandomStringUtils.random(smsConfig.getVerifyCodeLength(), false, true);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        Map<String, String> params = new HashMap<>();
        params.put("code", verifyCode);
        request.setTemplateParam(JSONObject.toJSONString(params));
        try {
            SendSmsResponse sendSmsResponse =
                smsService.sendSmsRequest(request, smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                /** 缓存验证码格式 = 验证码+","+时间 */
                phoneVerifyCodeCache.put(cmd.getPhone() + templateCodes, verifyCode + "," + String.valueOf(time));
                return Response.of(true);
            }
            return Response.buildFailure(B_SMS_VERIFY_CODE_ERROR, sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return Response.buildFailure(B_SMS_VERIFY_CODE_ERROR, MessageUtils.get("B_SMS_VERIFY_CODE_ERROR"));
    }

    /**
     * 验证手机验证码
     *
     * @param cmd
     * @return
     */
    public Response<Boolean> checkPhoneVerifyCode(UserPhoneVerifyCodeRpcCmd cmd) {
        SmsConfig smsConfig = smsConfigExe.getSmsConfig();
        List<String> codes = smsConfig.getTemplateCodes();
        System.out.println(JSON.toJSONString(codes));
        String templateCodes = smsConfig.getTemplateCodes().get(cmd.getCodeType() - 1);
        String timeVerifyCode = phoneVerifyCodeCache.get(cmd.getPhone() + templateCodes);
        if (StringUtils.isNotBlank(timeVerifyCode)) {
            String[] split = timeVerifyCode.split(",");
            /** 获取缓存验证码进行对比 */
            String verifyCode = split[0];
            if (StringUtils.isNotBlank(verifyCode) && verifyCode.equals(cmd.getCode())) {
                return Response.of(true);
            } else {
                return Response.buildFailure(P_SMS_VERIFY_CODE_DIFFERENT_ERROR,
                    MessageUtils.get("P_SMS_VERIFY_CODE_DIFFERENT_ERROR"));
            }
        } else {
            return Response.buildFailure(B_SMS_VERIFY_CODE_DIFFERENT_INVALID,
                MessageUtils.get("B_SMS_VERIFY_CODE_DIFFERENT_INVALID"));
        }
    }

}
