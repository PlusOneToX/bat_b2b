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
import com.platform.modules.mall.entity.MallRoomAllGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 直播接口所有商品信息Service接口
 *
 * @author 李鹏军
 * @since 2020-07-06 16:35:39
 */
public interface MallRoomAllGoodsService extends IService<MallRoomAllGoodsEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallRoomAllGoodsEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询直播接口所有商品信息
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增直播接口所有商品信息
     *
     * @param mallRoomAllGoods 直播接口所有商品信息
     * @return 新增结果
     */
    boolean add(MallRoomAllGoodsEntity mallRoomAllGoods);

    /**
     * 根据主键更新直播接口所有商品信息
     *
     * @param mallRoomAllGoods 直播接口所有商品信息
     * @return 更新结果
     */
    boolean update(MallRoomAllGoodsEntity mallRoomAllGoods);

    /**
     * 根据主键删除直播接口所有商品信息
     *
     * @param goodsId goodsId
     * @return 删除结果
     */
    boolean delete(Integer goodsId);

    /**
     * 根据主键批量删除
     *
     * @param goodsIds goodsIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] goodsIds);

    /**
     * 同步商品信息
     *
     * @param start 起始拉取房间，start = 0 表示从第 1 个房间开始拉取
     * @param limit 每次拉取的个数上限，不要设置过大，建议 100 以内
     * @return boolean
     */
    boolean getapproved(Integer start, Integer limit);

    /**
     * 撤回审核
     *
     * @param goodsId goodsId
     * @param auditId auditId
     * @return boolean
     */
    boolean resetaudit(Integer goodsId, Integer auditId);

    /**
     * 重新审核
     *
     * @param goodsId goodsId
     * @return boolean
     */
    boolean audit(int goodsId);
}
