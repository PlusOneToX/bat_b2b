package com.bat.thirdparty.common.config;

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
@ConfigurationProperties(prefix = "thirdparty")
@RefreshScope
public class ThirdPartyConfig {

    @Value("${modelType:9}")
    private Short modelType;
    @Value("${defaultTenantNo:100}")
    private String defaultTenantNo;
    @Value("${orderPurchaseAndOutboundSyncToErpTime:23}")
    private Integer orderPurchaseAndOutboundSyncToErpTime;
}
