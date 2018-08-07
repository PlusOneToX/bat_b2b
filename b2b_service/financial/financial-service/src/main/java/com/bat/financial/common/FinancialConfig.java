package com.bat.financial.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/13 21:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "financial")
@RefreshScope
public class FinancialConfig {

    @Value("${modelType:8}")
    private Short modelType;
    @Value("${defaultTenantNo:100}")
    private String defaultTenantNo;
}
