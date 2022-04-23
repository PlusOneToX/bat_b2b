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
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallDistAmountScheduledDao;
import com.platform.modules.mall.entity.MallDistAmountScheduledEntity;
import com.platform.modules.mall.entity.MallDistOrderEntity;
import com.platform.modules.mall.service.MallDistAmountScheduledService;
import com.platform.modules.mall.service.MallDistOrderService;
import com.platform.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 提成定时到账记录Service实现类
 *
 * @author 林伟
 * @since 2020-06-07 13:19:23
 */
@Service("mallDistAmountScheduledService")
public class MallDistAmountScheduledServiceImpl extends ServiceImpl<MallDistAmountScheduledDao, MallDistAmountScheduledEntity> implements MallDistAmountScheduledService {

    @Autowired
    private MallDistOrderService mallDistOrderService;
    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public List<MallDistAmountScheduledEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistAmountScheduledEntity> page = new Query<MallDistAmountScheduledEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistAmountScheduledPage(page, params));
    }

    @Override
    public boolean add(MallDistAmountScheduledEntity mallDistAmountScheduled) {
        return this.save(mallDistAmountScheduled);
    }

    @Override
    public boolean update(MallDistAmountScheduledEntity mallDistAmountScheduled) {
        return this.updateById(mallDistAmountScheduled);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public void addAmountScheduled(String orderId) {
        if (orderId == null) {
            return;
        }

        List<MallDistOrderEntity> distOrderList = mallDistOrderService.getDistOrderList(new QueryWrapper<MallDistOrderEntity>().eq("ORDER_ID", orderId));

        if (distOrderList.size() == 0) {
            return;
        }

        // 允许退货的期限
        int refundTime = Integer.parseInt(sysConfigService.getValue(Constant.ALLOW_REFUND_TIME, "7"));

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.add(Calendar.DATE, refundTime);

        for (MallDistOrderEntity entity : distOrderList) {
            String distOrderId = entity.getId();
            String userId = entity.getUserId();
            BigDecimal income = entity.getIncome();

            MallDistAmountScheduledEntity amountScheduledEntity = new MallDistAmountScheduledEntity();

            amountScheduledEntity.setDistOrderId(distOrderId);
            amountScheduledEntity.setOrderId(orderId);
            amountScheduledEntity.setUserId(userId);
            amountScheduledEntity.setIncome(income);
            amountScheduledEntity.setArriveTime(nowCalendar.getTime());
            amountScheduledEntity.setStatus(Constant.AmountStatus.WDZ.getValue());// 未到账
            this.save(amountScheduledEntity);

        }

    }
}
