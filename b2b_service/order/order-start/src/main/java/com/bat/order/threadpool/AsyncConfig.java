package com.bat.order.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/14 14:42
 */
@Configuration
public class AsyncConfig {

    private static final int MAX_POOL_SIZE = 50;

    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();

    @Bean("asyncTaskExecutor-cpu")
    public AsyncTaskExecutor asyncTaskExecutorCpu() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        asyncTaskExecutor.setCorePoolSize(CORE_SIZE +1);
        asyncTaskExecutor.setThreadNamePrefix("async-task-thread-pool-cpu-");
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }

    @Bean("asyncTaskExecutor-io")
    public AsyncTaskExecutor asyncTaskExecutorIo() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        asyncTaskExecutor.setCorePoolSize(CORE_SIZE *2);
        asyncTaskExecutor.setThreadNamePrefix("async-task-thread-pool-io-");
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}