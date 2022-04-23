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
import com.platform.modules.mall.entity.MallUserLevelConfEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员等级管理Service接口
 *
 * @author 李鹏军
 * @since 2021-01-07 11:06:48
 */
public interface MallUserLevelConfService extends IService<MallUserLevelConfEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallUserLevelConfEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询会员等级管理
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增会员等级管理
     *
     * @param mallUserLevelConf 会员等级管理
     * @return 新增结果
     */
    boolean add(MallUserLevelConfEntity mallUserLevelConf);

    /**
     * 根据主键更新会员等级管理
     *
     * @param mallUserLevelConf 会员等级管理
     * @return 更新结果
     */
    boolean update(MallUserLevelConfEntity mallUserLevelConf);

    /**
     * 根据主键删除会员等级管理
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
}
