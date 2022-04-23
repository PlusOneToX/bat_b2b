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
import com.platform.modules.act.entity.ActReProcdefEntity;
import org.activiti.engine.repository.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Service接口
 *
 * @author 李鹏军
 * @since 2019-03-18 09:47:54
 */
public interface ActReProcdefService extends IService<ActReProcdefEntity> {
    /**
     * 通过部署ID读取资源
     *
     * @param id       流程部署标识
     * @param proInsId 流程实例表示
     * @param resType  部署文件类型
     * @return 文件流
     */
    InputStream resourceRead(String id, String proInsId, String resType);

    /**
     * 启动流程实例，通过processDefinitionId
     *
     * @param processDefinitionId
     */
    void startProcessInstanceById(String processDefinitionId);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 删除部署流程
     *
     * @param deploymentId 流程部署标识
     */
    void delete(String deploymentId);

    /**
     * 删除部署流程
     *
     * @param deploymentIds 流程部署标识
     */
    void deleteBatch(String[] deploymentIds);

    /**
     * 根据文件部署工作流
     *
     * @param exportDir 文件地址
     * @param file      上传文件
     * @return 部署信息
     * @throws IOException
     */
    String deploy(String exportDir, MultipartFile file) throws IOException;

    /**
     * 转为模型
     *
     * @param id id
     * @return Model
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    Model convertToModel(String id) throws UnsupportedEncodingException, XMLStreamException;


    /**
     * 流程挂起和激活
     *
     * @param state 流程状态
     * @param id    流程部署标识
     * @return 操作信息
     */
    String updateState(int state, String id);
}
