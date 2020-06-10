package com.bat.flexible.manager.common.config;

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
@Component
@ConfigurationProperties(prefix = "flexible")
@RefreshScope
public class FlexibleConfig {

    @Value("${modelType:5}")
    private Short modelType;
    @Value("${defaultTenantNo:100}")
    private String defaultTenantNo;
    private Integer exchangeDistributorId;
    private String wxurl;
}
