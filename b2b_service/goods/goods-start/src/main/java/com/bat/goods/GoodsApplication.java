package com.bat.goods;

import com.bat.goods.interceptor.RedisJetCacheInterceptor;
import com.bat.goods.manager.mq.api.Sink;
import com.bat.goods.manager.mq.api.Source;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
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
@EnableMethodCache(basePackages = "com.bat.goods")
@DubboComponentScan
@EnableBinding({Source.class, Sink.class})
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    // @Bean
    // public RedisTemplate<String, String> redisTemplate() {
    // DynamicRedisTemplate<String, String> template = new DynamicRedisTemplate<String, String>();
    // return template;
    // }

    @Bean
    public JetCacheInterceptor jetCacheInterceptor() {
        return new RedisJetCacheInterceptor();
    }
}
