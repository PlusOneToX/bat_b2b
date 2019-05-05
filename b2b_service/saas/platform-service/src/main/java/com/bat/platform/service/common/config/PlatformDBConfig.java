package com.bat.platform.service.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * config
 *
 * @author 2017-04-28
 */
@Data
@Component
@ConfigurationProperties(value = "db")
@RefreshScope
public class PlatformDBConfig {

    private List<String> dbnames;
    private List<String> goods_db;
    private List<String> distributor_db;
    private List<String> warehouse_db;
    private List<String> system_db;
    private List<String> flexible_db;
    private List<String> promotion_db;
    private List<String> order_db;
    private List<String> financial_db;
    private List<String> thirdparty_db;
    @Value("${dbconnectparam:serverTimezone=GMT%2B8&allowMultiQueries=true}")
    private String dbconnectparam;
}