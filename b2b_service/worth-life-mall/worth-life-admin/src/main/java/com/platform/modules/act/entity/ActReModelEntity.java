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
package com.platform.modules.act.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2019-03-18 13:33:07
 */
@Data
@TableName("ACT_RE_MODEL")
public class ActReModelEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private String id;
    /**
     * 乐观锁版本号
     */
    private Integer rev;
    /**
     * 模型的名称
     */
    private String name;
    /**
     * 模型的关键字，流程引擎用到。
     */
    private String key;
    /**
     * 类型，用户自己对流程模型的分类。
     */
    private String category;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime;
    /**
     * 版本，从1开始。
     */
    private Integer version;
    /**
     * 数据源信息，以json格式保存流程定义的信息
     */
    private String metaInfo;
    /**
     * 部署ID
     */
    private String deploymentId;
    /**
     * 编辑源值ID，是 ACT_GE_BYTEARRAY 表中的ID_值。
     */
    private String editorSourceValueId;
    /**
     * 编辑源额外值ID，是 ACT_GE_BYTEARRAY 表中的ID_值。
     */
    private String editorSourceExtraValueId;
    /**
     * TENANT_ID_
     */
    private String tenantId;

    @TableField(exist = false)
    private String description;
}
