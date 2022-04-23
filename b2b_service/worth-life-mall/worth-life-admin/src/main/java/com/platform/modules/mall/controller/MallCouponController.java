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
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallCouponBrandEntity;
import com.platform.modules.mall.entity.MallCouponEntity;
import com.platform.modules.mall.entity.MallCouponGoodsEntity;
import com.platform.modules.mall.service.MallCouponBrandService;
import com.platform.modules.mall.service.MallCouponGoodsService;
import com.platform.modules.mall.service.MallCouponService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 优惠券列表Controller
 *
 * @author 李鹏军
 * @since 2019-07-06 22:09:37
 */
@RestController
@RequestMapping("mall/coupon")
public class MallCouponController extends AbstractController {
    @Autowired
    private MallCouponService mallCouponService;
    @Autowired
    private MallCouponGoodsService mallCouponGoodsService;
    @Autowired
    private MallCouponBrandService mallCouponBrandService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:coupon:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallCouponEntity> list = mallCouponService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询优惠券列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:coupon:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallCouponService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    public RestResponse info(@PathVariable("id") String id) {
        MallCouponEntity mallCoupon = mallCouponService.getById(id);
        List<String> goodsIds = new ArrayList<>();
        List<String> brandIds = new ArrayList<>();

        if (mallCoupon.getLimitType() == 1) {
            List<MallCouponGoodsEntity> couponGoodsEntityList = mallCouponGoodsService.list(new QueryWrapper<MallCouponGoodsEntity>().eq("COUPON_ID", mallCoupon.getId()));
            couponGoodsEntityList.forEach(couponGoodsEntity -> goodsIds.add(couponGoodsEntity.getGoodsId()));
            mallCoupon.setGoodsIds(goodsIds);
        }
        if (mallCoupon.getLimitType() == 2) {
            List<MallCouponBrandEntity> couponGoodsEntityList = mallCouponBrandService.list(new QueryWrapper<MallCouponBrandEntity>().eq("COUPON_ID", mallCoupon.getId()));
            couponGoodsEntityList.forEach(couponGoodsEntity -> brandIds.add(couponGoodsEntity.getBrandId()));
            mallCoupon.setBrandIds(brandIds);
        }

        return RestResponse.success().put("coupon", mallCoupon);
    }

    /**
     * 新增优惠券列表
     *
     * @param mallCoupon mallCoupon
     * @return RestResponse
     */
    @SysLog("新增优惠券列表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:coupon:save")
    public RestResponse save(@RequestBody MallCouponEntity mallCoupon) {

        mallCouponService.add(mallCoupon);

        return RestResponse.success();
    }

    /**
     * 修改优惠券列表
     *
     * @param mallCoupon mallCoupon
     * @return RestResponse
     */
    @SysLog("修改优惠券列表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:coupon:update")
    public RestResponse update(@RequestBody MallCouponEntity mallCoupon) {

        mallCouponService.update(mallCoupon);

        return RestResponse.success();
    }

    /**
     * 根据主键删除优惠券列表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除优惠券列表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:coupon:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallCouponService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 发放优惠券
     *
     * @param parmas
     * @return
     */
    @SysLog("发放优惠券")
    @RequestMapping("/sendUser")
    @RequiresPermissions("mall:coupon:sendUser")
    public RestResponse sendUser(@RequestBody Map<String, Object> parmas) {

        mallCouponService.sendUser(parmas);
        return RestResponse.success();
    }
}
