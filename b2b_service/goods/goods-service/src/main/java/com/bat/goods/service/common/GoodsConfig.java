package com.bat.goods.service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/13 21:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "goods")
@RefreshScope
public class GoodsConfig {

    @Value("${modelType:1}")
    private Short modelType;
    @Value("${defaultTenantNo:100}")
    private String defaultTenantNo;
}
