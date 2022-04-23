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
import com.platform.modules.mall.dto.BalanceDto;
import com.platform.modules.mall.entity.MallUserEntity;
import me.chanjar.weixin.cp.bean.WxCpAgent;

import java.util.List;
import java.util.Map;

/**
 * 用户service
 *
 * @author 李鹏军
 */
public interface MallUserService extends IService<MallUserEntity> {


    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallUserEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增
     *
     * @param mallUser mallUser
     * @return 新增结果
     */
    boolean add(MallUserEntity mallUser);

    /**
     * 根据主键更新
     *
     * @param mallUser mallUser
     * @return 更新结果
     */
    boolean update(MallUserEntity mallUser);

    /**
     * 根据主键删除
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(String[] ids);

    /**
     * queryByMobile
     *
     * @param mobile 手机号
     * @return UserEntity
     */
    MallUserEntity queryByMobile(String mobile);

    /**
     * 登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @return String
     */
    MallUserEntity loginByMobile(String mobile, String password);

    /**
     * 根据openId获取用户
     *
     * @param openId openId
     * @return UserEntity
     */
    MallUserEntity selectByOpenId(String openId);

    /**
     * 根据unionId获取用户
     *
     * @param unionId unionId
     * @return UserEntity
     */
    MallUserEntity selectByOpenUnionId(String unionId);

    //伪登录获取token
    Map<String, Object> loginDesc(Map<String, Object> reqMap);

    MallUserEntity exchangeBalance(BalanceDto balanceDto);
}
