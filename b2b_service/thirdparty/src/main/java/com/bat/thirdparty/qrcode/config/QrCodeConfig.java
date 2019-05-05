package com.bat.thirdparty.qrcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/13 21:35
 */
@Data
public class QrCodeConfig {

    private String distributionUrl;

    private String distributionOssFolder;

    private String shopOssFolder;
}
