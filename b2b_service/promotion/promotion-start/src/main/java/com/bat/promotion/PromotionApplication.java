package com.bat.promotion;

import com.bat.promotion.interceptor.RedisJetCacheInterceptor;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bat.promotion.mq.api.Sink;
import com.bat.promotion.mq.api.Source;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.promotion")
@DubboComponentScan
@EnableOpenApi
@EnableBinding({Source.class, Sink.class})
public class PromotionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromotionApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
