package com.bat.goods.db.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块名称: Mybatis配置类 模块描述: 主要是配置动态数据源
 * 小知识：@Configuration注解的类中,@Bean注解的方法会被Spring重写生成代理,缓存返回的对象,所以即便在此处多次调用dynamicDataSource(),返回的都是同一个dynamicDataSource对象
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/05/21 10:25
 */
@Slf4j
@Configuration
public class MybatisConfig {
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
     * 初始化【基础数据源】的方法
     *
     * @return
     */
    public HikariDataSource getBaseDataSource() {
        if (null != singleBaseDataSource) {
            return singleBaseDataSource;
        }
        HikariConfig jdbcConfig = new HikariConfig();
        // 设置必要的连接属性
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
     * Mybatis多数据库支持适配器
     *
     * @return
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties p = new Properties();
        p.setProperty("MySQL", "mysql");
        // p.setProperty("Oracle", "oracle");
        // p.setProperty("MySQL", "mysql");
        // p.setProperty("PostgreSQL", "postgresql");
        // p.setProperty("DB2", "db2");
        // p.setProperty("SQL Server", "sqlserver");
        databaseIdProvider.setProperties(p);
        return databaseIdProvider;
    }

    /**
     * 动态数据源(切库专用)
     *
     * @return
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        if (log.isDebugEnabled()) {
            log.debug("===== 新建dynamicDataSource Bean，初始化基础数据源");
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 1.创建【基础数据源】baseDataSource
        HikariDataSource baseDataSource = getBaseDataSource();
        // 2.将【基础数据源】baseDataSource设置为【动态数据源】dynamicDataSource的默认值
        dynamicDataSource.setDefaultTargetDataSource(baseDataSource);
        // 3.将【基础数据源】放入【动态数据源】dynamicDataSource底层维护的TargetDataSources中，作为第一个目标数据源，其对应键名为【baseDatasource】(整个程序启动完毕后:TargetDataSources维护的数据源构成:1个基础数据源+n个租户数据源)
        Map<Object, Object> dataSourceMap = new HashMap<>(16);
        dataSourceMap.put(dynamicDataSourceConfig.getBaseDbName(), baseDataSource);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    /**
     * 验证表是否存在:返回true表示存在,false表示不存在
     *
     * @param conn
     * @param tableName
     * @return 返回true表示存在, false表示不存在
     */
    private boolean validateTableNameExist(Connection conn, String tableName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = true;
        // 2.编写sql语句
        String sql = "select count(1) from " + tableName;
        try {
            // 3.获取sql预编译对象
            ps = conn.prepareStatement(sql);
            // 4.执行并保存结果集
            rs = ps.executeQuery();
        } catch (SQLException e) {
            // 如果抛出了table不存在异常,说明数据源中tenant表不存在,说明当前是【单机版】
            result = false;
        } finally {
            // 释放连接资源
            release(conn, ps, rs);
        }
        return result;
    }

    /**
     * 释放连接资源
     *
     * @param conn
     *            Connection
     * @param ps
     *            PreparedStatement
     * @param rs
     *            ResultSet
     */
    private static void release(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mybatis sqlSession核心工厂
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为【关键配置】，如果没有此步,则dynamicDataSource动态数据源的切换无效
        // 小知识：@Configuration注解的类中,@Bean注解的方法会被Spring重写生成代理,缓存返回的对象,所以即便在此处多次调用dynamicDataSource(),返回的都是同一个dynamicDataSource对象
        sessionFactory.setDataSource(dynamicDataSource());
        sessionFactory.setDatabaseIdProvider(databaseIdProvider());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 扫描映射文件
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/*xml"));
        // 打印详细sql日志 为了找BUG
        // if (StringUtils.isNotBlank(this.dynamicDataSourceConfig.getMybatisConfigurationLogImpl())) {
        // org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // try {
        // configuration.setLogImpl(
        // (Class<? extends Log>)Class.forName(this.dynamicDataSourceConfig.getMybatisConfigurationLogImpl()));
        // } catch (ClassNotFoundException e) {
        // configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        // }
        // sessionFactory.setConfiguration(configuration);
        // }
        // 加载mybatis的全局配置文件
        // org.springframework.core.io.Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
        // sessionFactory.setConfigLocation(mybatisConfigXml);
        return sessionFactory;
    }

    /**
     * 事务管理器
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }

    /**
     * JdbcTemplate
     *
     * @return
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dynamicDataSource());
    }
}
