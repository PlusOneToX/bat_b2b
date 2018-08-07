package com.bat.distributor;

import com.bat.distributor.interceptor.RedisJetCacheInterceptor;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bat.distributor.mq.api.Sink;
import com.bat.distributor.mq.api.Source;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.distributor")
@EnableBinding({Source.class, Sink.class})
public class DistributorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributorApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
