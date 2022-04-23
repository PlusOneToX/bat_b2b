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
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.BigDecimalUtil;
import com.platform.common.utils.JedisUtil;
import com.platform.common.utils.ObjectUtil;
import com.platform.common.utils.Query;
import com.platform.common.validator.AbstractAssert;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.dto.BalanceDto;
import com.platform.modules.mall.entity.MallAccountLogEntity;
import com.platform.modules.mall.entity.MallDistributionRecordEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserInviteRecordEntity;
import com.platform.modules.mall.service.MallAccountLogService;
import com.platform.modules.mall.service.MallUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 李鹏军
 */
@Service("userService")
public class MallUserServiceImpl extends ServiceImpl<MallUserDao, MallUserEntity> implements MallUserService {
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    private MallAccountLogService mallAccountLogService;

    @Autowired
    MallUserDao mallUserDao;

    @Override
    public List<MallUserEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.REGISTER_TIME");
        params.put("asc", false);
        Page<MallUserEntity> page = new Query<MallUserEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserPage(page, params));
    }

    @Override
    public boolean add(MallUserEntity mallUser) {
        return this.save(mallUser);
    }

    @Override
    public boolean update(MallUserEntity mallUser) {
        return this.updateById(mallUser);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public MallUserEntity queryByMobile(String mobile) {
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("MOBILE", mobile), false);
    }

    @Override
    public MallUserEntity loginByMobile(String mobile, String password) {
        MallUserEntity user = queryByMobile(mobile);
        AbstractAssert.isNull(user, "该手机暂未绑定用户");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new BusinessException("手机号或密码错误");
        }

        return user;
    }

    @Override
    public MallUserEntity selectByOpenId(String openId) {
        MallUserEntity userEntity = new MallUserEntity();
        userEntity.setOpenId(openId);
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("OPEN_ID", openId), false);
    }

    @Override
    public MallUserEntity selectByOpenUnionId(String unionId) {
        MallUserEntity userEntity = new MallUserEntity();
        userEntity.setUnionId(unionId);
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("UNION_ID", unionId), false);
    }

    //伪登录获取token
    @Override
    public Map<String, Object> loginDesc(Map<String, Object> reqMap) {
        return mallUserDao.loginDesc(reqMap);
    }

    @Override
    @Transactional
    public synchronized MallUserEntity  exchangeBalance(BalanceDto balanceDto) {
        MallUserEntity mallUserEntity = lambdaQuery().eq(MallUserEntity::getId, balanceDto.getUserId()).one();
        if (ObjectUtil.isEmpty(mallUserEntity)) {
            throw new BusinessException("用户不存在");
        }
        mallUserEntity.setBalance(BigDecimalUtil.add(balanceDto.getAmount(), mallUserEntity.getBalance()));

        saveOrUpdate(mallUserEntity);

        MallAccountLogEntity logEntity = new MallAccountLogEntity();
        logEntity.setAddTime(balanceDto.getUpdateTime());
        logEntity.setFromType(balanceDto.getBusinessType());
        logEntity.setOrderSn(balanceDto.getPid());
        logEntity.setLogDesc(balanceDto.getLabel());
        logEntity.setRemark(balanceDto.getRemark());
        logEntity.setUserId(balanceDto.getUserId());
        logEntity.setPrice(BigDecimalUtil.toDecimal(balanceDto.getAmount()));
        logEntity.setOrderType(balanceDto.getBusinessType());
        logEntity.setType(balanceDto.getType());
        mallAccountLogService.save(logEntity);
        return mallUserEntity;
    }

    //查询该用户直推间推了多少人
    public List<MallUserInviteRecordEntity> getCountPeople(String getUserId) {
       return mallUserDao.getCountPeople(getUserId);
    }
}
