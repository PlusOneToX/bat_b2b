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
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.ConfirmAuditEntity;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallDistOrderEntity;
import com.platform.modules.mall.service.MallDistOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分销订单Controller
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@RestController
@RequestMapping("mall/distorder")
public class MallDistOrderController extends AbstractController {
    @Autowired
    private MallDistOrderService mallDistOrderService;
    @Value("${wx.pay.spbillCreateIp}")
    private String spbillCreateIp;


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:distorder:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallDistOrderEntity> list = mallDistOrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询分销订单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:distorder:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallDistOrderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:distorder:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallDistOrderEntity mallDistOrder = mallDistOrderService.queryById(id);

        return RestResponse.success().put("distorder", mallDistOrder);
    }

    /**
     * 新增分销订单
     *
     * @param mallDistOrder mallDistOrder
     * @return RestResponse
     */
    @SysLog("新增分销订单")
    @RequestMapping("/save")
    @RequiresPermissions("mall:distorder:save")
    public RestResponse save(@RequestBody MallDistOrderEntity mallDistOrder) {

        mallDistOrderService.add(mallDistOrder);

        return RestResponse.success();
    }

    /**
     * 修改分销订单
     *
     * @param mallDistOrder mallDistOrder
     * @return RestResponse
     */
    @SysLog("修改分销订单")
    @RequestMapping("/update")
    @RequiresPermissions("mall:distorder:update")
    public RestResponse update(@RequestBody MallDistOrderEntity mallDistOrder) {

        mallDistOrderService.update(mallDistOrder);

        return RestResponse.success();
    }

    /**
     * 根据主键删除分销订单
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除分销订单")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:distorder:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallDistOrderService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 审核提现订单
     */
    @SysLog("审核提现订单")
    @RequestMapping("/confirmAudit")
    @RequiresPermissions("mall:distorder:confirmAudit")
    public RestResponse confirmAudit(@RequestBody ConfirmAuditEntity entity) throws WxPayException {
        System.out.println(entity);
        mallDistOrderService.confirmAudit(entity.getIds(), entity.getAuditStatus(), entity.getAuditDesc(),spbillCreateIp);

        return RestResponse.success();
    }
}
