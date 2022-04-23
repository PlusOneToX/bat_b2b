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
package com.platform.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品分类实体
 *
 * @author 李鹏军
 * @since 2019-07-02 19:53:21
 */
@Data
@TableName("MALL_CATEGORY")
public class MallCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父节点
     */
    private String parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否显示 0:否 1:是
     */
    private Integer isShow;
    /**
     * 分类页面顶部大图
     */
    private String imgUrl;
    /**
     * icon链接
     */
    private String iconUrl;
    /**
     * 简介
     */
    private String frontName;
    /**
     * 父节点名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 子分类数据
     */
    @TableField(exist = false)
    List<MallCategoryEntity> subCategoryList;

    @TableField(exist = false)
    private Boolean checked;
}
