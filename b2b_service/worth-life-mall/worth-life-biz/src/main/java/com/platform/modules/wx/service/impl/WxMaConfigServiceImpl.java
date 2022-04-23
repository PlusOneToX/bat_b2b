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
package com.platform.modules.wx.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.wx.dao.WxMaConfigDao;
import com.platform.modules.wx.entity.WxMaConfigEntity;
import com.platform.modules.wx.service.WxMaConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序配置Service实现类
 *
 * @author 李鹏军
 * @since 2020-04-05 21:58:47
 */
@Service("wxMaConfigService")
public class WxMaConfigServiceImpl extends ServiceImpl<WxMaConfigDao, WxMaConfigEntity> implements WxMaConfigService {

    @Override
    public List<WxMaConfigEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<WxMaConfigEntity> page = new Query<WxMaConfigEntity>(params).getPage();
        return page.setRecords(baseMapper.selectWxMaConfigPage(page, params));
    }

    @Override
    public boolean add(WxMaConfigEntity wxMaConfig) {
        return this.save(wxMaConfig);
    }

    @Override
    public boolean update(WxMaConfigEntity wxMaConfig) {
        return this.updateById(wxMaConfig);
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
}
