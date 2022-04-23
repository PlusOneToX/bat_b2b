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
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallOrderAwardRecordDao;
import com.platform.modules.mall.entity.MallOrderAwardRecordEntity;
import com.platform.modules.mall.service.MallOrderAwardRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 李鹏军
 * @since 2021-01-08 17:26:14
 */
@Service("mallOrderAwardRecordService")
public class MallOrderAwardRecordServiceImpl extends ServiceImpl<MallOrderAwardRecordDao, MallOrderAwardRecordEntity> implements MallOrderAwardRecordService {

    @Override
    public List<MallOrderAwardRecordEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallOrderAwardRecordEntity> page = new Query<MallOrderAwardRecordEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallOrderAwardRecordPage(page, params));
    }

    @Override
    public boolean add(MallOrderAwardRecordEntity mallOrderAwardRecord) {
        return this.save(mallOrderAwardRecord);
    }

    @Override
    public boolean update(MallOrderAwardRecordEntity mallOrderAwardRecord) {
        return this.updateById(mallOrderAwardRecord);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }
}
