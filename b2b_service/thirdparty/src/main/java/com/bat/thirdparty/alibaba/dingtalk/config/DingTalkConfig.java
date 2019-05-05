package com.bat.thirdparty.alibaba.dingtalk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 16:11
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ding-talk")
public class DingTalkConfig {
    private String appkey;
    private String appsecret;
}
