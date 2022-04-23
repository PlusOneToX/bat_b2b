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
import com.platform.modules.mall.entity.MallUserInviteRecordEntity;
import com.platform.modules.mall.vo.DistributionVo;
import com.platform.modules.mall.vo.ShopRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 邀请记录表Dao
 *
 * @author pfrong
 * @since 2020-11-10 14:35:24
 */
@Mapper
public interface MallUserInviteRecordDao extends BaseMapper<MallUserInviteRecordEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallUserInviteRecordEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallUserInviteRecordEntity> selectMallUserInviteRecordPage(IPage page, @Param("params") Map<String, Object> params);

    Page<MallUserInviteRecordEntity> selectAllInviteRecordPage(IPage page, @Param("params") Map<String, Object> params);

    DistributionVo getDistributionVo(@Param("userId") String userId);


    @Select("SELECT * FROM (select  IFNULL(count(1),0) as directCount from  MALL_USER_INVITE_RECORD  WHERE invite_user_id=#{userId}) AS A,(select  IFNULL(count(1),0) as indirectCount from  MALL_USER_INVITE_RECORD  WHERE invite_user_id in (select  USER_ID from  MALL_USER_INVITE_RECORD  WHERE invite_user_id=#{userId})) AS B,(SELECT IFNULL(SUM(award),0) as award FROM mall_distribution_record WHERE user_id=#{userId} and relation <3 ) AS C")
    DistributionVo teamDistributionVo(@Param("userId") String id);


    @Select("SELECT * FROM (SELECT count(1) AS shopCount FROM mall_shops WHERE USER_ID in (SELECT user_id FROM  mall_user_invite_record  WHERE invite_user_id=#{userId})) AS A,(SELECT IFNULL(SUM(award),0) FROM mall_distribution_record WHERE user_id=#{userId} and relation >2 ) AS B)")
    DistributionVo shopDistributionVo(@Param("userId") String id);


    @Select("SELECT ms.`NAME`,muir.create_time ,muir.user_name,ms.user_id FROM mall_shops AS ms,MALL_USER_INVITE_RECORD AS muir WHERE muir.invite_user_id=#{userId} AND muir.user_id=ms.USER_ID")
    Page<ShopRecordVo> shopRecord(IPage page, @Param("userId") String id);

    List<MallUserInviteRecordEntity> queryPageById(Page<MallUserInviteRecordEntity> page, @Param("params") Map<String, Object> params);

    Page<MallUserInviteRecordEntity> inviteShopList(Page<MallUserInviteRecordEntity> page, @Param("params") Map<String, Object> params);

    @Select("SELECT muir.user_id ,muir.USER_NAME,ms.CREATE_TIME as joinTime FROM mall_user_invite_record AS muir,mall_shops AS ms WHERE muir.invite_user_id =#{param.userId} AND muir.user_id=ms.user_id")
    Page<MallUserInviteRecordEntity> extendUserRecord(Page<MallUserInviteRecordEntity> page, @Param("param") Map<String, Object> params);

    //查询店铺的直推间推用户
    List<MallUserInviteRecordEntity> queryUserAll(@Param("id") String id);


    //@Select("SELECT a.user_id,a.user_name, IFNULL( SUM(actual_price),0) AS actual_price, IFNULL(SUM(award),0) AS award FROM mall_user_invite_record a , mall_distribution_record b WHERE a.invite_user_id=#{params.inviteUserId} AND b.user_id=a.invite_user_id GROUP BY a.user_id,a.user_name\n")
    Page<MallUserInviteRecordEntity> inviteShopListShopDetail(Page<MallUserInviteRecordEntity> page, @Param("params") Map<String, Object> params);

    Page<MallUserInviteRecordEntity> shopDistributionRecord(Page<MallUserInviteRecordEntity> page, @Param("params") Map<String, Object> params);
}
