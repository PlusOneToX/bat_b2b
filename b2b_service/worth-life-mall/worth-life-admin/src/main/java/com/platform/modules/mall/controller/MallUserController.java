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
package com.platform.modules.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.mall.MallUserLevel;
import com.platform.common.utils.ObjectUtil;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserIntegralEntity;
import com.platform.modules.mall.entity.MallUserLevelEntity;
import com.platform.modules.mall.service.MallUserIntegralService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 会员Controller
 *
 * @author 李鹏军
 * @since 2019-07-01 14:59:40
 */
@RestController
@RequestMapping("mall/user")
public class MallUserController extends AbstractController {
    @Autowired
    private MallUserService mallUserService;
    @Autowired
    private MallUserLevel mallUserLevel;
    @Autowired
    private MallUserIntegralService mallUserIntegralService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserEntity> list = mallUserService.queryAll(params);

        return RestResponse.success().put("list", list);
    }
    /**
     * 查看所有列表
     *
     * @return RestResponse
     */
    @RequestMapping("/queryUserIdName")
    public RestResponse queryUserIdName() {
        List<MallUserEntity> list = mallUserService.list(new QueryWrapper<MallUserEntity>().select("ID,USER_NAME"));

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:user:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page<MallUserEntity> page = mallUserService.queryPage(params);
        for (MallUserEntity record : page.getRecords()) {
            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(record.getId());
            if (mallUserLevelEntity.isPresent()) {
                record.setLevel(Integer.valueOf(mallUserLevelEntity.get().getId()));
            }
            //用户总积分
            MallUserIntegralEntity userIntegral = mallUserIntegralService.lambdaQuery().eq(MallUserIntegralEntity::getUserId, record.getId()).one();
            if (userIntegral != null) {
                record.setSignAllIntegral(userIntegral.getAmount());
            }
        }

        return RestResponse.success().put("page", page);
    }


    /**
     * 根据主键查询会员详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:user:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallUserEntity mallUser = mallUserService.getById(id);

        return RestResponse.success().put("user", mallUser);
    }

    /**
     * 新增会员
     *
     * @param mallUser mallUser
     * @return RestResponse
     */
    @SysLog("新增会员")
    @RequestMapping("/save")
    @RequiresPermissions("mall:user:save")
    public RestResponse save(@RequestBody MallUserEntity mallUser) {

        mallUserService.add(mallUser);

        return RestResponse.success();
    }

    /**
     * 修改会员
     *
     * @param mallUser mallUser
     * @return RestResponse
     */
    @SysLog("修改会员")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody MallUserEntity mallUser) {
        MallUserEntity one = mallUserService.lambdaQuery().eq(MallUserEntity::getId, mallUser.getId()).one();
        one.setLevel(mallUser.getLevel());
        mallUserService.update(one);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:user:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserService.deleteBatch(ids);

        return RestResponse.success();
    }
}
