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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallSeckillEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀库存表Service接口
 *
 * @author 李鹏军
 * @since 2019-07-07 12:05:21
 */
public interface MallSeckillService extends IService<MallSeckillEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallSeckillEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询秒杀库存表
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增秒杀库存表
     *
     * @param mallSeckill 秒杀库存表
     * @return 新增结果
     */
    boolean add(MallSeckillEntity mallSeckill);

    /**
     * 根据主键更新秒杀库存表
     *
     * @param mallSeckill 秒杀库存表
     * @return 更新结果
     */
    boolean update(MallSeckillEntity mallSeckill);

    /**
     * 根据主键删除秒杀库存表
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

    /**
     * 秒杀
     *
     * @param seckillId 秒杀配置表ID
     * @param userId    用户ID
     * @param fromType  用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序
     * @return RestResponse
     */
    RestResponse startSeckill(String seckillId, String userId, Integer fromType);
}
