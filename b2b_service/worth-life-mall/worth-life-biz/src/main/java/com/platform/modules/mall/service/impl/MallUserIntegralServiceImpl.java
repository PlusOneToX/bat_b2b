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
import com.platform.common.utils.*;
import com.platform.modules.mall.dao.MallUserIntegralDao;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.MallUserIntegralEntity;
import com.platform.modules.mall.entity.MallUserIntegralLogEntity;
import com.platform.modules.mall.service.MallUserIntegralService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 李鹏军
 * @since 2021-01-04 11:44:00
 */
@Service("mallUserIntegralService")
@Slf4j
public class MallUserIntegralServiceImpl extends ServiceImpl<MallUserIntegralDao, MallUserIntegralEntity> implements MallUserIntegralService {

    @Autowired
    private MallUserIntegralLogServiceImpl mallUserIntegralLogService;

    @Override
    public List<MallUserIntegralEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallUserIntegralEntity> page = new Query<MallUserIntegralEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserIntegralPage(page, params));
    }

    @Override
    public boolean add(MallUserIntegralEntity mallUserIntegral) {
        return this.save(mallUserIntegral);
    }

    @Override
    public boolean update(MallUserIntegralEntity mallUserIntegral) {
        return this.updateById(mallUserIntegral);
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


    @Transactional
    public synchronized MallUserIntegralEntity exchange(IntegralDto integralDto) {
        MallUserIntegralEntity mallUserIntegralEntity = lambdaQuery().eq(MallUserIntegralEntity::getUserId, integralDto.getUserId()).one();
        log.info("mallUserIntegralEntity:[{}]",mallUserIntegralEntity.toString());
        if (ObjectUtil.isEmpty(mallUserIntegralEntity)) {
            mallUserIntegralEntity = new MallUserIntegralEntity().setUserId(integralDto.getUserId());
        }

        mallUserIntegralEntity.setAmount(BigDecimalUtil.add(integralDto.getAmount(), mallUserIntegralEntity.getAmount()));
        mallUserIntegralEntity.setFreeze(BigDecimalUtil.add(integralDto.getFreeze(), mallUserIntegralEntity.getFreeze()));
        mallUserIntegralEntity.setUpdateTime(integralDto.getUpdateTime());
        saveOrUpdate(mallUserIntegralEntity);

        MallUserIntegralLogEntity mallUserIntegralLogEntity = new MallUserIntegralLogEntity().setAmount(BigDecimalUtil.toDecimal(integralDto.getAmount())).setFreeze(BigDecimalUtil.toDecimal(integralDto.getFreeze()));
        BeanUtils.copyNewPropertites(integralDto,mallUserIntegralLogEntity);
        mallUserIntegralLogEntity.setCreateTime(integralDto.getUpdateTime());
        log.info("------*******"+mallUserIntegralLogEntity.toString()+"------*******");
        mallUserIntegralLogService.save(mallUserIntegralLogEntity);
        return mallUserIntegralEntity;
    }
}
