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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.mall.entity.MallOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单表Dao
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@Mapper
public interface MallOrderDao extends BaseMapper<MallOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallOrderEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallOrderEntity> selectMallOrderPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 统计个人中心订单数据
     *
     * @param params params
     * @return Map
     */
    Map<String, Object> queryUserCountMap(Map<String, Object> params);

    MallOrderEntity queryById(String orderId);

    /**
     * 店铺商品销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> shopsGoodsSalesCount(@Param("params") Map<String, Object> params);

    /**
     * 所有分店销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> allShopsGoodsSalesCount(@Param("params") Map<String, Object> params);

    /**
     * 统计用户实际成交金额
     * @param userId 用户id
     * @return
     */
    @Select("SELECT COALESCE(SUM(ACTUAL_PRICE),0) FROM mall_order WHERE USER_ID = #{USER_ID} and ORDER_STATUS = 301")
    BigDecimal sumUserPayAmount(@Param("USER_ID") String userId);

    /**
     * 统计用户实际成交金额
     * @param userId 用户id
     * @return
     */
    @Select("SELECT * FROM MALL_ORDER WHERE USER_ID = #{userId} AND ORDER_STATUS = 301")
    List<MallOrderEntity> queryMallOrder(@Param("userId")String userId);

}
