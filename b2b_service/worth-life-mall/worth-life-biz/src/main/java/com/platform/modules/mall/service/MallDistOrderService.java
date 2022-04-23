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
package com.platform.modules.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.modules.mall.entity.IncomeDetailsEntity;
import com.platform.modules.mall.entity.MallDistOrderDetailEntity;
import com.platform.modules.mall.entity.MallDistOrderEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 分销订单Service接口
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
public interface MallDistOrderService extends IService<MallDistOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallDistOrderEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询分销订单
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增分销订单
     *
     * @param mallDistOrder 分销订单
     * @return 新增结果
     */
    boolean add(MallDistOrderEntity mallDistOrder);

    /**
     * 根据主键更新分销订单
     *
     * @param mallDistOrder 分销订单
     * @return 更新结果
     */
    boolean update(MallDistOrderEntity mallDistOrder);

    /**
     * 根据主键删除分销订单
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(String[] ids);

    void confirmAudit(String[] ids, String auditStatus, String auditDesc,String ip) throws WxPayException;

    MallDistOrderEntity queryById(String id);

    /**
     * 获取本月、当日的开始结束
     *
     * @return Map
     */
    Map<String, Long> getTime();

    /**
     * 根据指定用户，时间范围，获取提现类型列表的和
     *
     * @param userId    userId
     * @param startTime startTime
     * @param endTime   endTime
     * @return BigDecimal
     */
    BigDecimal getTotalWithdraw(String userId, Timestamp startTime, Timestamp endTime);

    IPage<MallDistOrderDetailEntity> distOrderPage(Page<MallDistOrderDetailEntity> page, QueryWrapper<MallDistOrderDetailEntity> queryWrapper);

    BigDecimal getIncomeDetails(String userId, Timestamp startTime, Timestamp endTime);

    IPage<IncomeDetailsEntity> getIncomeDetailsPage(Page<IncomeDetailsEntity> page, QueryWrapper<IncomeDetailsEntity> queryWrapper);

    Integer getOrderCount(String userId);

    void addDistOrder(String userId, String orderId);

    List<MallDistOrderEntity> getDistOrderList(QueryWrapper<MallDistOrderEntity> qw);
}
