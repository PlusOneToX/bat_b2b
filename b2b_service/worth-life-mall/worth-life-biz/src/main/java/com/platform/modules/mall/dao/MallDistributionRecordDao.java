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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.modules.mall.entity.MallDistributionRecordEntity;
import com.platform.modules.mall.entity.MallOrderAwardRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author 李鹏军
 * @since 2021-01-07 11:20:34
 */
@Mapper
public interface MallDistributionRecordDao extends BaseMapper<MallDistributionRecordEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallDistributionRecordEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallDistributionRecordEntity> selectMallDistributionRecordPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 分页查询团队用户下单详情
     *
     * @param params 查询参数
     * @return Page
     */
    List<MallDistributionRecordEntity> queryUserOrderDesc(Page<MallDistributionRecordEntity> page, @Param("params") Map<String, Object> params);

    //@Select("SELECT SUM(IF(relation=1,actual_price,0)) AS actual_price,SUM(award)  AS award FROM mall_distribution_record\n" +
    //        " WHERE origin_id in\n" +
    //        "(SELECT USER_id FROM mall_user_invite_record WHERE invite_user_id =#{userId}\n" +
    //        "UNION ALL\n" +
    //        "SELECT USER_id FROM mall_user_invite_record WHERE invite_user_id in (SELECT USER_id FROM mall_user_invite_record WHERE invite_user_id =#{userId}) )\n" +
    //        "\n" +
    //        "GROUP BY origin_id \n")
    MallDistributionRecordEntity sumInviteOrderRecord(@Param("param") Map<String, Object> params);

    MallDistributionRecordEntity sumTotal(@Param("param") Map<String, Object> params);


}
