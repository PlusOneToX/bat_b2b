package com.bat.promotion.db.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 模块名称: 动态数据源外部配置类 模块描述: 用于统一获取数据源相关的外部动态配置
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/10/21 10:23
 */
@Data
@Configuration
public class DynamicDataSourceConfig {

    @Value("${mybatis.configuration.log-impl:''}")
    private String mybatisConfigurationLogImpl;

    /**
     * HikariDataSource数据库连接池的必要连接配置
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.name}")
    private String baseDbName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /******************************** HikariDataSource数据库连接池的其他自定义配置-开始 ********************************/
    /**
     * 连接超时时间:30秒(Long类型)
     */
    @Value("${aicc.spring.datasource.hikari.connection-timeout:30000}")
    private Long connectionTimeout;
    /**
     * 最大生命时间:10分钟(Long类型)
     */
    @Value("${aicc.spring.datasource.hikari.max-lifetime:600000}")
    private Long maxLifetime;
    /**
     * 最大空闲时间:5分钟(Long类型)
     */
    @Value("${aicc.spring.datasource.hikari.idle-timeout:300000}")
    private Long idleTimeout;
    /**
     * 最大连接数(Integer)
     */
    @Value("${aicc.spring.datasource.hikari.maximum-pool-size:20}")
    private Integer maximumPoolSize;
    /**
     * 最小空闲连接数(Integer)
     */
    @Value("${aicc.spring.datasource.hikari.minimum-idle:1}")
    private Integer minimumIdle;
    /**
     * 启用泄漏检测超时判定标准(毫秒):3分钟(Long)
     */
    @Value("${aicc.spring.datasource.hikari.leak-detection-threshold:180000}")
    private Integer leakDetectionThreshold;

    /******************************** HikariDataSource数据库连接池的其他自定义配置-结束 ********************************/
}
