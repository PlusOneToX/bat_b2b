package com.bat.thirdparty.task;

import javax.annotation.Resource;

import com.bat.thirdparty.alibaba.dingtalk.service.SystemServiceDingTalkRpcImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 缓存任务 缓存预热
 * @date: 2019/6/16 18:06
 */
@Component
@Slf4j
public class CacheTask implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private SystemServiceDingTalkRpcImpl systemServiceDingTalkRpc;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("=====================ThirdPartyApplication has to be started======================");
        log.info("========================cache warming task is starting=========================");
        systemServiceDingTalkRpc.listDepartment();
        log.info("========================cache warming task has been finished=========================");

        log.info("========================开始处理mongoDB索引=========================");
        // mongoServiceI.initIndex();
        log.info("========================mongoDB索引处理完毕=========================");
    }
}
