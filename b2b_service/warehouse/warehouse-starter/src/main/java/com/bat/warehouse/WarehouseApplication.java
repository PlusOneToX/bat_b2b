package com.bat.warehouse;

import com.bat.warehouse.interceptor.RedisJetCacheInterceptor;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bat.warehouse.mq.api.Sink;
import com.bat.warehouse.mq.api.Source;

import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 仓库模块启动类
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan
@EnableOpenApi
@EnableCreateCacheAnnotation
@EnableBinding({Sink.class, Source.class})
@EnableMethodCache(basePackages = "com.bat.warehouse")
public class WarehouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }
}
