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
import com.platform.modules.mall.entity.MallAddressEntity;
import com.platform.modules.mall.service.MallAddressService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员地址Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 10:37:28
 */
@RestController
@RequestMapping("mall/address")
public class MallAddressController extends AbstractController {
    @Autowired
    private MallAddressService mallAddressService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:address:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallAddressEntity> list = mallAddressService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员地址
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:address:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallAddressService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:address:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallAddressEntity mallAddress = mallAddressService.getById(id);

        return RestResponse.success().put("address", mallAddress);
    }

    /**
     * 新增会员地址
     *
     * @param mallAddress mallAddress
     * @return RestResponse
     */
    @SysLog("新增会员地址")
    @RequestMapping("/save")
    @RequiresPermissions("mall:address:save")
    public RestResponse save(@RequestBody MallAddressEntity mallAddress) {

        mallAddressService.add(mallAddress);

        return RestResponse.success();
    }

    /**
     * 修改会员地址
     *
     * @param mallAddress mallAddress
     * @return RestResponse
     */
    @SysLog("修改会员地址")
    @RequestMapping("/update")
    @RequiresPermissions("mall:address:update")
    public RestResponse update(@RequestBody MallAddressEntity mallAddress) {

        mallAddressService.update(mallAddress);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员地址
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员地址")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:address:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallAddressService.deleteBatch(ids);

        return RestResponse.success();
    }
}
