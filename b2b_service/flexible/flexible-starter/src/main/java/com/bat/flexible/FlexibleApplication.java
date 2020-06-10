package com.bat.flexible;

import com.bat.flexible.interceptor.RedisJetCacheInterceptor;
import com.bat.flexible.mq.api.Sink;
import com.bat.flexible.mq.api.Source;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 仓库模块启动类
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan
@EnableOpenApi
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.flexible")
@EnableBinding({Source.class, Sink.class})
public class FlexibleApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlexibleApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }
}
