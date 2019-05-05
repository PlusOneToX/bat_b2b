package com.bat.promotion.db.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块名称: 【Mysql】动态数据源初始化类 模块描述: 用于初始化所有租户的数据源
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/10/21 11:27
 */
@Slf4j
@Configuration
public class DynamicDataSourceInit {
    /**
     * 注入数据源相关的外部动态配置
     */
    @Resource
    private DynamicDataSourceConfig dynamicDataSourceConfig;

    /**
     * 基础数据源为单例
     */
    private static HikariDataSource singleBaseDataSource = null;

    /**
     * 初始化默认配置数据库的方法
     *
     * @return
     */
    public HikariDataSource getBaseDataSource() {
        if (null != singleBaseDataSource) {
            return singleBaseDataSource;
        }
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setDriverClassName(dynamicDataSourceConfig.getDriverClass());
        jdbcConfig.setJdbcUrl(dynamicDataSourceConfig.getUrl());
        jdbcConfig.setUsername(dynamicDataSourceConfig.getUsername());
        jdbcConfig.setPassword(dynamicDataSourceConfig.getPassword());
        // 设置其他数据源配置属性
        jdbcConfig.setConnectionTimeout(dynamicDataSourceConfig.getConnectionTimeout());
        jdbcConfig.setMaxLifetime(dynamicDataSourceConfig.getMaxLifetime());
        jdbcConfig.setIdleTimeout(dynamicDataSourceConfig.getIdleTimeout());
        jdbcConfig.setMaximumPoolSize(dynamicDataSourceConfig.getMaximumPoolSize());
        jdbcConfig.setMinimumIdle(dynamicDataSourceConfig.getMinimumIdle());
        jdbcConfig.setLeakDetectionThreshold(dynamicDataSourceConfig.getLeakDetectionThreshold());
        HikariDataSource baseDataSource = new HikariDataSource(jdbcConfig);
        singleBaseDataSource = baseDataSource;
        return baseDataSource;
    }

    /**
     * 初始化租户数据源
     *
     * @return
     */
    public HikariDataSource getTenantDataSource(PlatformTenantDBRpcDTO tenantDB) {
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setDriverClassName(dynamicDataSourceConfig.getDriverClass());
        jdbcConfig.setJdbcUrl(tenantDB.getDbUrl());
        jdbcConfig.setUsername(tenantDB.getUserName());
        jdbcConfig.setPassword(tenantDB.getPassword());
        // 设置其他数据源配置属性
        jdbcConfig.setConnectionTimeout(dynamicDataSourceConfig.getConnectionTimeout());
        jdbcConfig.setMaxLifetime(dynamicDataSourceConfig.getMaxLifetime());
        jdbcConfig.setIdleTimeout(dynamicDataSourceConfig.getIdleTimeout());
        jdbcConfig.setMaximumPoolSize(dynamicDataSourceConfig.getMaximumPoolSize());
        jdbcConfig.setMinimumIdle(dynamicDataSourceConfig.getMinimumIdle());
        jdbcConfig.setLeakDetectionThreshold(dynamicDataSourceConfig.getLeakDetectionThreshold());
        HikariDataSource tenantDataSource = new HikariDataSource(jdbcConfig);
        return tenantDataSource;
    }
}
