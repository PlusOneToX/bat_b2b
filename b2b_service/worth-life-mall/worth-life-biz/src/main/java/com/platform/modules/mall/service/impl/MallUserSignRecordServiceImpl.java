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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallUserSignRecordDao;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserSignRecordEntity;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.MallUserSignRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 用户签到记录Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-02 18:15:12
 */
@Service("mallUserSignRecordService")
public class MallUserSignRecordServiceImpl extends ServiceImpl<MallUserSignRecordDao, MallUserSignRecordEntity> implements MallUserSignRecordService {
    @Autowired
    private MallUserService userService;

    @Override
    public List<MallUserSignRecordEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallUserSignRecordEntity> page = new Query<MallUserSignRecordEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserSignRecordPage(page, params));
    }

    @Override
    public boolean add(MallUserSignRecordEntity mallUserSignRecord) {
        return this.save(mallUserSignRecord);
    }

    @Override
    public boolean update(MallUserSignRecordEntity mallUserSignRecord) {
        return this.updateById(mallUserSignRecord);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public Map<String, Object> getSignByParams(String userId) {
        Map<String, Object> result = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int length = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer[] list = getDefaultList(length + 1);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", "nowMonth");
        List<MallUserSignRecordEntity> userSignRecordVos = queryAll(map);
        if (userSignRecordVos != null && userSignRecordVos.size() > 0) {
            for (MallUserSignRecordEntity vo : userSignRecordVos) {
                calendar.setTime(vo.getSignTime());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                list[day] = day;
            }
        }
        result.put("monthDaySize", length);
        result.put("calendarSignData", list);
        result.put("calendarSignDay", userSignRecordVos.size());
        return result;
    }

    @Override
    public boolean nowDaySigned(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", id);
        //查询当天
        map.put("type", "nowDay");
        List<MallUserSignRecordEntity> userSignRecordVos = queryAll(map);
        if (null == userSignRecordVos || userSignRecordVos.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 签到
     *
     * @param id id
     * @return BigDecimal
     */
    @Override
    @Transactional
    public BigDecimal userSign(String id) {
        //当天未签到
        if (!nowDaySigned(id)) {
            //每次签到获取的积分，可以配置在数据库，这里写死了
            BigDecimal integral = new BigDecimal(1);

            MallUserSignRecordEntity vo = new MallUserSignRecordEntity();
            vo.setUserId(id);
            vo.setSignTime(new Date());
            vo.setSignIntegral(integral);
            save(vo);

            //更新会员表积分
            MallUserEntity userVo = userService.getById(id);
            userVo.setSignAllIntegral(userVo.getSignAllIntegral().add(integral));
            userService.update(userVo);

            return integral;
        }
        return new BigDecimal(0);
    }

    private Integer[] getDefaultList(int length) {
        Integer[] result = new Integer[length];
        for (int i = 0; i < length; i++) {
            result[i] = null;
        }
        return result;
    }
}
