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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallUserBankCardEntity;
import com.platform.modules.mall.service.MallUserBankCardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡表Controller
 *
 * @author 李鹏军
 * @since 2020-06-15 09:42:45
 */
@RestController
@RequestMapping("mall/userbankcard")
public class MallUserBankCardController extends AbstractController {
    @Autowired
    private MallUserBankCardService mallUserBankCardService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:userbankcard:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserBankCardEntity> list = mallUserBankCardService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询用户银行卡表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:userbankcard:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallUserBankCardService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:userbankcard:info")
    public RestResponse info(@PathVariable("id") String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        MallUserBankCardEntity mallUserBankCard = mallUserBankCardService.getCardInfoById(params);

        return RestResponse.success().put("userbankcard", mallUserBankCard);
    }

    /**
     * 新增用户银行卡表
     *
     * @param mallUserBankCard mallUserBankCard
     * @return RestResponse
     */
    @SysLog("新增用户银行卡表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:userbankcard:save")
    public RestResponse save(@RequestBody MallUserBankCardEntity mallUserBankCard) {

        mallUserBankCardService.add(mallUserBankCard);

        return RestResponse.success();
    }

    /**
     * 修改用户银行卡表
     *
     * @param mallUserBankCard mallUserBankCard
     * @return RestResponse
     */
    @SysLog("修改用户银行卡表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:userbankcard:update")
    public RestResponse update(@RequestBody MallUserBankCardEntity mallUserBankCard) {

        mallUserBankCardService.update(mallUserBankCard);

        return RestResponse.success();
    }

    /**
     * 根据主键删除用户银行卡表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除用户银行卡表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:userbankcard:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserBankCardService.deleteBatch(ids);

        return RestResponse.success();
    }
}
