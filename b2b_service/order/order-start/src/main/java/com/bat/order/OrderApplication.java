package com.bat.order;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bat.order.interceptor.RedisJetCacheInterceptor;
import com.bat.order.mq.api.Sink;
import com.bat.order.mq.api.Source;

import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan
@EnableOpenApi
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.order")
@EnableBinding({Source.class, Sink.class})
@EnableAsync
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
