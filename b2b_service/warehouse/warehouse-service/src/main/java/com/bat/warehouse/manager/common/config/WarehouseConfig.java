package com.bat.warehouse.manager.common.config;

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
@ConfigurationProperties(prefix = "warehouse")
@RefreshScope
public class WarehouseConfig {

    @Value("${modelType:3}")
    private Short modelType;
    @Value("${syncStockNum:1000}")
    private Integer syncStockNum;
    @Value("${defaultTenantNo:100}")
    private String defaultTenantNo;
}
