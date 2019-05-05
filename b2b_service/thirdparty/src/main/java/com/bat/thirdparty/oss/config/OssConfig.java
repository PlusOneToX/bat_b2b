package com.bat.thirdparty.oss.config;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/13 21:35 子账户 b2b_oss_sms@rqzykj.onaliyun.com 的key
 */
@Data
public class OssConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucket;

    private String baseHttp;

    private String roleArn;

    private String regionId;

    private String region;

    private String policy;
}
