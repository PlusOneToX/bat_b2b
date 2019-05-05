package com.bat.thirdparty.mongodb.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.bat.thirdparty.mongodb.api.MongoServiceI;

import lombok.Data;

/**
 * 模块名称: 【MongoDB】动态MongoTemplate维护类 模块描述: 维护一份全局静态Map<String, MongoTemplate>,用于存储不同租户对应的MongoTemplate.
 * 每个请求进来,在AOP切面中,会根据tenantId,从该templateMap中取出该租户对应的MongoTemplate,绑定到该线程上下文中,供dao层调用
 *
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/26 18:09
 */
@Data
@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private String port;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Resource
    private MongoServiceI mongoService;

    // @Bean
    // public MongoClientFactoryBean mongoClientFactoryBean() {
    // MongoClientFactoryBean factoryBean = new MongoClientFactoryBean();
    // factoryBean.setHost("192.168.1.100");
    // factoryBean.setPort(27017);
    // return factoryBean;
    // }

}
