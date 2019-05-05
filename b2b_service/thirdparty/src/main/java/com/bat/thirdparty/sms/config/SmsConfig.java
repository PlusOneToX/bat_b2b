package com.bat.thirdparty.sms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/13 21:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
@RefreshScope
public class SmsConfig {

    @Value("${smsSign:B2B}")
    public String smsSign;
    private List<String> templateCodes;
    @Value("${verifyCodeLength:4}")
    private int verifyCodeLength;
    @Value("${codeVerifyTime:300}")
    public int codeVerifyTime;
    @Value("${verifyCodeCountdown:60}")
    public int verifyCodeCountdown;
    @Value("${accessKeyId:null}")
    public String accessKeyId;
    @Value("${accessKeySecret:null}")
    public String accessKeySecret;
}
