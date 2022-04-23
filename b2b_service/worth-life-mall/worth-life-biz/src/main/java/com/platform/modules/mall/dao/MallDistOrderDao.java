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
package com.platform.modules.mall.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.modules.mall.entity.IncomeDetailsEntity;
import com.platform.modules.mall.entity.MallDistOrderDetailEntity;
import com.platform.modules.mall.entity.MallDistOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 分销订单Dao
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Mapper
public interface MallDistOrderDao extends BaseMapper<MallDistOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallDistOrderEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallDistOrderEntity> selectMallDistOrderPage(IPage page, @Param("params") Map<String, Object> params);

    MallDistOrderEntity queryById(String id);

    BigDecimal getTotalWithdraw(@Param("userId") String userId, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime, @Param("type") Integer type);

    IPage<MallDistOrderDetailEntity> distOrderPage(Page<MallDistOrderDetailEntity> page, @Param("ew") QueryWrapper<MallDistOrderDetailEntity> queryWrapper);

    BigDecimal getIncomeDetails(@Param("userId") String userId, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime, @Param("orderType") List<Integer> orderType, @Param("orderStatus") List<Integer> orderStatus);

    IPage<IncomeDetailsEntity> getIncomeDetailsPage(Page<IncomeDetailsEntity> page,@Param("ew") QueryWrapper<IncomeDetailsEntity> queryWrapper);
}
