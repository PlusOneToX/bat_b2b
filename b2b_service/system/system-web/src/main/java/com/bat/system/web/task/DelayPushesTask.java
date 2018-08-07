package com.bat.system.web.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import com.bat.system.api.globalsetting.FactorySettingService;

/**
 * @author: lim
 * @description: XXLJOB实现了
 * @date: 2018/5/8 16:30
 */
// @Component
public class DelayPushesTask {

    @Resource
    private FactorySettingService factorySettingService;

    /**
     * 工厂生产推送时效 每天+1
     */
    @Scheduled(cron = "0 0 0 * * ? *")
    public void updateDelayPushesPushTimeByTask() {
        factorySettingService.updateDelayPushesPushTimeByTask();
    }
}
