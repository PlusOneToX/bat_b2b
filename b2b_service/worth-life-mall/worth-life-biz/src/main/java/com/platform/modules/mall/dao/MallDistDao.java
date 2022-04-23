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
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallDistTeamEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分销Dao
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Mapper
public interface MallDistDao extends BaseMapper<MallDistEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallDistEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallDistEntity> selectMallDistPage(IPage page, @Param("params") Map<String, Object> params);

    MallDistEntity queryById(Integer id);

    MallDistEntity queryByUserId(String userId);

    IPage<MallDistTeamEntity> queryTeam(Page<MallDistTeamEntity> page, @Param("ew") QueryWrapper<MallDistTeamEntity> queryWrapper);

    void updateAmount(@Param("userId") String userId, @Param("deductionAmount") BigDecimal deductionAmount,@Param("amount") BigDecimal amount);

    List<Integer> getFirstTeamIds(@Param("id") Integer id);

    MallDistEntity getInvitationInfo(@Param("userId") String userId);

    void addAmountAvailable(@Param("income") BigDecimal income, @Param("userId") String userId);
}
