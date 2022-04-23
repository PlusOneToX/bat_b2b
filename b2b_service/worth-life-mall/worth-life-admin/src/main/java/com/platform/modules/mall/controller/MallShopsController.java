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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 店铺Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 13:51:29
 */
@RestController
@RequestMapping("mall/shops")
public class MallShopsController extends AbstractController {
    @Autowired
    private MallShopsService mallShopsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shops:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShopsEntity> list = mallShopsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询店铺
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shops:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        IPage page = mallShopsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shops:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallShopsEntity mallShops = mallShopsService.getById(id);

        return RestResponse.success().put("shops", mallShops);
    }

    /**
     * 新增店铺
     *
     * @param mallShops mallShops
     * @return RestResponse
     */
    @SysLog("新增店铺")
    @RequestMapping("/save")
    @RequiresPermissions("mall:shops:save")
    public RestResponse save(@RequestBody MallShopsEntity mallShops) {
        mallShops.setCreateTime(new Date());
        mallShops.setCreateUserId(getUserId());
        mallShopsService.add(mallShops);

        return RestResponse.success();
    }

    /**
     * 修改店铺
     *
     * @param mallShops mallShops
     * @return RestResponse
     */
    @SysLog("修改店铺")
    @RequestMapping("/update")
    @RequiresPermissions("mall:shops:update")
    public RestResponse update(@RequestBody MallShopsEntity mallShops) {

        mallShopsService.update(mallShops);

        return RestResponse.success();
    }

    /**
     * 根据主键删除店铺
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除店铺")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shops:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallShopsService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 分页查询我的店铺
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/myShop")
    @RequiresPermissions("mall:shops:myShop")
    public RestResponse myShop(@RequestParam Map<String, Object> params) {
        params.put("userId", getUserId());
        IPage page = mallShopsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 查询我的所有店铺
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryMyShop")
    @RequiresPermissions("mall:shops:myShop")
    public RestResponse queryMyShop(@RequestParam Map<String, Object> params) {
        String userId = getUserId();
        //超级管理员查看所有
        if (!Constant.SUPER_ADMIN.equals(userId)) {
            params.put("userId", userId);
        }
        List<MallShopsEntity> list = mallShopsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 修改我的店铺
     *
     * @param mallShops mallShops
     * @return RestResponse
     */
    @SysLog("修改店铺")
    @RequestMapping("/myUpdate")
    @RequiresPermissions("mall:shops:myUpdate")
    public RestResponse myUpdate(@RequestBody MallShopsEntity mallShops) {

        mallShopsService.myUpdate(mallShops, getUserId());

        return RestResponse.success();
    }
}
