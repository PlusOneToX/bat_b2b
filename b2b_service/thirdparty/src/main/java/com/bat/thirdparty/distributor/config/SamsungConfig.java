package com.bat.thirdparty.distributor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@RefreshScope
public class SamsungConfig {
    @Value("${samsung.qualifiedUrl}")
    private String qualifiedUrl;

    @Value("${samsung.checkTokenUrl}")
    private String checkTokenUrl;

    @Value("${samsung.distributor.id}")
    private String distributorId;

    @Value("${samsung.platform.no}")
    private String platform;
}
