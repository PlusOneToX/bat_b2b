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
package com.platform.modules.act.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.act.entity.ActReModelEntity;
import org.activiti.engine.repository.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Service接口
 *
 * @author 李鹏军
 * @since 2019-03-18 13:33:07
 */
public interface ActReModelService extends IService<ActReModelEntity> {

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
     * @param actReModel
     * @return 新增结果
     * @throws UnsupportedEncodingException
     */
    Model add(ActReModelEntity actReModel) throws UnsupportedEncodingException;

    /**
     * 部署工作流模型
     *
     * @param id 模型标识
     * @return 部署信息
     */
    String deploy(String id);

    /**
     * 导出XML
     *
     * @param id       流程模型标识
     * @param response 响应
     */
    void export(String id, HttpServletResponse response);

    /**
     * 根据主键删除
     *
     * @param id id
     */
    void delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     */
    void deleteBatch(String[] ids);
}
