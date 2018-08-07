package com.bat.system;

import com.bat.system.interceptor.RedisJetCacheInterceptor;
import com.bat.system.mq.api.Sink;
import com.bat.system.mq.api.Source;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.system")
@DubboComponentScan
@EnableBinding({Source.class, Sink.class})
@MapperScan("com.bat.system.dao")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
