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
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Base64Util;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallCommentDao;
import com.platform.modules.mall.entity.MallCommentEntity;
import com.platform.modules.mall.service.MallCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 会员评价Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-04 14:46:38
 */
@Service("mallCommentService")
public class MallCommentServiceImpl extends ServiceImpl<MallCommentDao, MallCommentEntity> implements MallCommentService {

    @Override
    public List<MallCommentEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallCommentEntity> page = new Query<MallCommentEntity>(params).getPage();
        List<MallCommentEntity> commentEntityList = baseMapper.selectMallCommentPage(page, params);

        for (MallCommentEntity commentItem : commentEntityList) {
            commentItem.setContent(Base64Util.decode(commentItem.getContent()));
        }
        return page.setRecords(commentEntityList);
    }

    @Override
    public boolean add(MallCommentEntity mallComment) {
        return this.save(mallComment);
    }

    @Override
    public boolean update(MallCommentEntity mallComment) {
        return this.updateById(mallComment);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public boolean changeStatus(String[] ids) {
        MallCommentEntity commentEntity = this.getById(ids[0]);
        commentEntity.setStatus(1);
        return this.update(commentEntity);
    }

    @Override
    public Integer queryHasPicTotal(Map<String, Object> param) {
        return baseMapper.queryHasPicTotal(param);
    }
}
