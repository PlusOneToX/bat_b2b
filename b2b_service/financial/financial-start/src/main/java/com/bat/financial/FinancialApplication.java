package com.bat.financial;

import com.bat.financial.interceptor.RedisJetCacheInterceptor;
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
import com.bat.financial.mq.api.Sink;
import com.bat.financial.mq.api.Source;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bat.financial")
@EnableBinding({Source.class, Sink.class})
@MapperScan("com.bat.financial.dao")
@EnableAsync
public class FinancialApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialApplication.class, args);
    }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }

}
