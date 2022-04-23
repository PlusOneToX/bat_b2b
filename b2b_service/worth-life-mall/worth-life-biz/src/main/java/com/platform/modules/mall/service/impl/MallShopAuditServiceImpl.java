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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.*;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.AddGroup;
import com.platform.modules.mall.dao.MallShopAuditDao;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.MallAwardConfEntity;
import com.platform.modules.mall.entity.MallShopAuditEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallShopAuditService;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.utils.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service实现类
 *
 * @author 李鹏军
 * @since 2021-01-04 18:50:28
 */
@Service("mallShopAuditService")
public class MallShopAuditServiceImpl extends ServiceImpl<MallShopAuditDao, MallShopAuditEntity> implements MallShopAuditService {





    @Override
    public List<MallShopAuditEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallShopAuditEntity> page = new Query<MallShopAuditEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallShopAuditPage(page, params));
    }

    @Override
    public boolean add(MallShopAuditEntity mallShopAudit) {
        return this.save(mallShopAudit);
    }

    @Override
    public boolean update(MallShopAuditEntity mallShopAudit) {
        return this.updateById(mallShopAudit);
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
