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
import com.platform.modules.mall.entity.MallUserCouponEntity;
import com.platform.modules.mall.service.MallUserCouponService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员优惠券Controller
 *
 * @author 李鹏军
 * @since 2020-04-10 16:02:58
 */
@RestController
@RequestMapping("mall/usercoupon")
public class MallUserCouponController extends AbstractController {
    @Autowired
    private MallUserCouponService mallUserCouponService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:usercoupon:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserCouponEntity> list = mallUserCouponService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员优惠券
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:usercoupon:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallUserCouponService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:usercoupon:info")
    public RestResponse info(@PathVariable("id") String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        MallUserCouponEntity mallUserCoupon = mallUserCouponService.queryAll(params).get(0);

        return RestResponse.success().put("usercoupon", mallUserCoupon);
    }

    /**
     * 新增会员优惠券
     *
     * @param mallUserCoupon mallUserCoupon
     * @return RestResponse
     */
    @SysLog("新增会员优惠券")
    @RequestMapping("/save")
    @RequiresPermissions("mall:usercoupon:save")
    public RestResponse save(@RequestBody MallUserCouponEntity mallUserCoupon) {

        mallUserCouponService.add(mallUserCoupon);

        return RestResponse.success();
    }

    /**
     * 修改会员优惠券
     *
     * @param mallUserCoupon mallUserCoupon
     * @return RestResponse
     */
    @SysLog("修改会员优惠券")
    @RequestMapping("/update")
    @RequiresPermissions("mall:usercoupon:update")
    public RestResponse update(@RequestBody MallUserCouponEntity mallUserCoupon) {

        mallUserCouponService.update(mallUserCoupon);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员优惠券
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员优惠券")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:usercoupon:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserCouponService.deleteBatch(ids);

        return RestResponse.success();
    }
}
