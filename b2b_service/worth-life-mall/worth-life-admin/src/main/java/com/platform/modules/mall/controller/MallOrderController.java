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
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.service.MallOrderGoodsService;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单表Controller
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@RestController
@RequestMapping("mall/order")
public class MallOrderController extends AbstractController {
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallShopsService shopsService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:order:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallOrderEntity> list = mallOrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询订单表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:order:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallOrderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:order:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallOrderEntity mallOrder = mallOrderService.queryById(id);

        List<MallOrderGoodsEntity> orderGoodsEntityList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", mallOrder.getId()));
        mallOrder.setOrderGoodsEntityList(orderGoodsEntityList);
        return RestResponse.success().put("order", mallOrder);
    }

    /**
     * 新增订单表
     *
     * @param mallOrder mallOrder
     * @return RestResponse
     */
    @SysLog("新增订单表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:order:save")
    public RestResponse save(@RequestBody MallOrderEntity mallOrder) {

        mallOrderService.add(mallOrder);

        return RestResponse.success();
    }

    /**
     * 修改订单表
     *
     * @param mallOrder mallOrder
     * @return RestResponse
     */
    @SysLog("修改订单表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:order:update")
    public RestResponse update(@RequestBody MallOrderEntity mallOrder) {

        mallOrderService.update(mallOrder);

        return RestResponse.success();
    }

    /**
     * 根据主键删除订单表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除订单表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:order:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallOrderService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 分页查询我的订单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/myOrder")
    @RequiresPermissions("mall:order:myOrder")
    public RestResponse myOrder(@RequestParam Map<String, Object> params) {
        params.put("sysUserId", getUserId());
        Page page = mallOrderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 查询我的所有订单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/queryMyOrder")
    @RequiresPermissions("mall:order:myOrder")
    public RestResponse myOrderList(@RequestParam Map<String, Object> params) {
        params.put("sysUserId", getUserId());
        List<MallOrderEntity> list = mallOrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 发货
     *
     * @param orderEntity
     * @return
     */
    @SysLog("发货")
    @RequestMapping("/sendGoods")
    @RequiresPermissions("mall:order:sendGoods")
    public RestResponse sendGoods(@RequestBody MallOrderEntity orderEntity) {
        mallOrderService.sendGoods(orderEntity);

        return RestResponse.success();
    }

    /**
     * 确定收货
     *
     * @param id
     * @return
     */
    @SysLog("确定收货")
    @RequestMapping("/confirmReceive")
    @RequiresPermissions("mall:order:confirmReceive")
    public RestResponse confirmReceive(String id) {
        mallOrderService.confirmReceive(id);

        return RestResponse.success();
    }

    /**
     * 修改价格
     *
     * @return
     */
    @SysLog("修改价格")
    @RequestMapping("/modPrice")
    @RequiresPermissions("mall:order:modPrice")
    public RestResponse modPrice(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        MallOrderEntity orderEntity = mallOrderService.getById(id);
        orderEntity.setActualPrice(new BigDecimal(params.get("actualPrice")));
        mallOrderService.updateById(orderEntity);
        return RestResponse.success();
    }

    /**
     * 分店商品销售统计
     *
     * @param params params
     * @return RestResponse
     */
    @RequiresPermissions("mall:order:report")
    @GetMapping("/shopsGoodsSalesCount")
    public RestResponse shopsGoodsSalesCount(@RequestParam Map<String, Object> params) {
        MallShopsEntity shopsEntity;
        SysUserEntity user;
        if (!StringUtils.isEmpty(params.get("shopsId"))) {
            shopsEntity = shopsService.getById(params.get("shopsId").toString());
            user = sysUserService.getById(shopsEntity.getUserId());
        } else {
            user = getUser();
            shopsEntity = shopsService.getOne(new QueryWrapper<MallShopsEntity>().eq("USER_ID", user.getUserId()), false);
            if (null == shopsEntity) {
                return RestResponse.error("该管理员账号下未查询到店铺");
            }
            params.put("shopsId", shopsEntity.getId());
        }
        List<Map<String, Object>> list = mallOrderService.shopsGoodsSalesCount(params);
        return RestResponse.success().put("list", list).put("shopsId", shopsEntity.getId()).put("details", shopsEntity.getDetails() + "-" + user.getUserName());
    }

    /**
     * 所有分店销售统计
     *
     * @param params params
     * @return RestResponse
     */
    @RequiresPermissions("mall:order:allReport")
    @GetMapping("/allShopsGoodsSalesCount")
    public RestResponse allShopsGoodsSalesCount(@RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = mallOrderService.allShopsGoodsSalesCount(params);
        return RestResponse.success().put("list", list);
    }
}
