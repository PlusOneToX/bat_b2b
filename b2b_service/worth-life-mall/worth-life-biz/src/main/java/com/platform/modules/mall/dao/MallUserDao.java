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
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserInviteRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author 李鹏军
 */
@Mapper
public interface MallUserDao extends BaseMapper<MallUserEntity> {


    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallUserEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallUserEntity> selectMallUserPage(IPage page, @Param("params") Map<String, Object> params);

    List<String> getMerchantRole();


    @Update("UPDATE  `mall_user` SET role_type=#{role} WHERE id= #{userId}")
    Integer changeRole(@Param("role") Integer role,@Param("userId") String userId);

    //伪登录获取token
    @Select("select * from mall_user where mobile=#{mobile}")
    Map<String,Object> loginDesc(Map<String, Object> reqMap);

    //查询该用户直推间推了多少人
    List<MallUserInviteRecordEntity> getCountPeople(@Param("getUserId") String getUserId);
}
