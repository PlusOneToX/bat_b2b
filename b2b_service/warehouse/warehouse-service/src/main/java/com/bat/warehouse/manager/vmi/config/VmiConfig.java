package com.bat.warehouse.manager.vmi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 沙漠
 */
@Data
@Component
@RefreshScope
public class VmiConfig {

    private String vmiWarehouse;

}
