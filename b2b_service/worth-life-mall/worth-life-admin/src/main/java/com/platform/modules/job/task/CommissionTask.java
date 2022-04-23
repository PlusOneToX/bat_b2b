/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.job.task;

import com.platform.common.utils.Constant;
import com.platform.modules.mall.entity.MallDistAmountScheduledEntity;
import com.platform.modules.mall.service.MallDistAmountScheduledService;
import com.platform.modules.mall.service.MallDistService;
import com.platform.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 佣金定时到账任务
 *
 * @author 林伟
 */
@Slf4j
@Component("commissionTask")
public class CommissionTask {
    private final MallDistService mallDistService;
    private final MallDistAmountScheduledService mallDistAmountScheduledService;

    public CommissionTask(MallDistService mallDistService, MallDistAmountScheduledService mallDistAmountScheduledService, SysConfigService sysConfigService) {
        this.mallDistService = mallDistService;
        this.mallDistAmountScheduledService = mallDistAmountScheduledService;
    }


    /**
     * 佣金到账commissionAccount
     */
    @SuppressWarnings(value = "unused")
    @Transactional(rollbackFor = Exception.class)
    public void amountArrive() {
        log.info("--------------------------开始执行佣金到账任务--------------------------");

        //需到账的记录
        List<MallDistAmountScheduledEntity> amountList = mallDistAmountScheduledService.lambdaQuery().eq(MallDistAmountScheduledEntity::getStatus, Constant.AmountStatus.WDZ).list();
        if (null != amountList && amountList.size() > 0) {
            log.info("待到账的记录数：" + amountList.size());

            List<MallDistAmountScheduledEntity> list = new ArrayList<>();
            //当前时间大于过期时间
            amountList.stream().filter(item -> new Date().after(item.getArriveTime()))
                    .forEach(amount -> {
                        amount.setStatus(Constant.AmountStatus.YDZ.getValue());
                        list.add(amount);
                        //添加佣金
                        mallDistService.addAmountAvailable(amount);
                    });
            if (list.size() > 0) {
                mallDistAmountScheduledService.updateBatchById(list);
            }
        }
        log.info("--------------------------结束执行佣金到账任务--------------------------");
    }

}
