package com.bat.thirdparty;

import javax.annotation.Resource;

import com.bat.thirdparty.common.interceptor.RedisJetCacheInterceptor;
import com.bat.thirdparty.message.api.Sink;
import com.bat.thirdparty.message.api.Source;
import com.bat.thirdparty.mongodb.MultiMongoTemplate;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bat.thirdparty.mongodb.api.MongoServiceI;
import com.bat.thirdparty.mongodb.config.MongoConfig;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync        //开启异步任务
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.thirdparty")
@EnableBinding({Source.class, Sink.class})
@DubboComponentScan
public class ThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyApplication.class, args);
    }

    @Resource
    MongoConfig mongoConfig;
    @Resource
    MongoServiceI mongoService;

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        MultiMongoTemplate mongoTemplate = new MultiMongoTemplate(mongoDbFactory());
        // 备份到全局map中
        mongoService.initIndex(mongoTemplate);
        return mongoTemplate;
    }

    @Bean(name = "mongoDbFactory")
    public MongoDatabaseFactory mongoDbFactory() {
        String mongodbUri = "mongodb://" + mongoConfig.getUsername() + ":" + mongoConfig.getPassword() + "@"
            + mongoConfig.getHost() + ":" + mongoConfig.getPort() + "/" + mongoConfig.getDatabase();
        SimpleMongoClientDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongodbUri);
        TenantContext.mongoDFactoryMap.put(mongodbUri, mongoDbFactory);
        return mongoDbFactory;
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
