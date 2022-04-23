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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.*;
import com.platform.modules.mall.entity.MallUserBonusDetailEntity;
import com.platform.modules.mall.param.QueryParam;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallUserIntegralLogEntity;
import com.platform.modules.mall.service.MallUserIntegralLogService;
import com.platform.utils.ExcelUtils;
import io.swagger.annotations.*;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-04 11:44:00
 */
@RestController
@RequestMapping("mall/userintegrallog")
public class MallUserIntegralLogController extends AbstractController {
    @Autowired
    private MallUserIntegralLogService mallUserIntegralLogService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:userintegrallog:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserIntegralLogEntity> list = mallUserIntegralLogService.queryAll(params);


        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @ApiOperation(value = "积分详情", notes = "积分详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallUserIntegralLogService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    public RestResponse info(@PathVariable("id") Integer id) {
        MallUserIntegralLogEntity mallUserIntegralLog = mallUserIntegralLogService.getById(id);

        return RestResponse.success().put("userintegrallog", mallUserIntegralLog);
    }

    /**
     * 新增
     *
     * @param mallUserIntegralLog mallUserIntegralLog
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody MallUserIntegralLogEntity mallUserIntegralLog) {

        mallUserIntegralLogService.add(mallUserIntegralLog);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallUserIntegralLog mallUserIntegralLog
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody MallUserIntegralLogEntity mallUserIntegralLog) {

        mallUserIntegralLogService.update(mallUserIntegralLog);

        return RestResponse.success();
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    public RestResponse delete(@RequestBody Integer[] ids) {
        mallUserIntegralLogService.deleteBatch(ids);

        return RestResponse.success();
    }


    @GetMapping("getIntegralOrder")
    @ApiOperation(value = "获取当前用户积分排行", notes = "获取当前用户积分排行", response = MallUserBonusDetailEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse getIntegralOrder(@RequestParam Map<String, Object> queryParam) {
        int pageNo = BigDecimalUtil.toDecimal(queryParam.get("page")).intValue();
        int pageSize = BigDecimalUtil.toDecimal(queryParam.get("limit")).intValue();
        Object userId = queryParam.get("userId");
        String json = jedisUtil.get(Constant.INTEGRAL_ORDER);
        Page page = new Page();
        if (StringUtils.isEmpty(json)) {
            page.setRecords(new ArrayList()).setSize(pageNo).setSize(pageSize).setTotal(0);
            return RestResponse.success().put("data", page);
        }

        List<MallUserBonusDetailEntity> mallUserBonusDetailEntities = JSONObject.parseArray(json, MallUserBonusDetailEntity.class);
        List<MallUserBonusDetailEntity> temp = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(userId)) {
            temp = mallUserBonusDetailEntities.stream().filter(e -> e.getUserId().equals(userId.toString())).collect(Collectors.toList());
        } else {
            temp = mallUserBonusDetailEntities;
        }
        List list = new ArrayList();
        if (ObjectUtil.isNotEmpty(temp)) {
            list = ListUtils.pageBySubList(temp, pageNo, pageSize);
        }
        page.setRecords(list).setSize(pageNo).setSize(pageSize).setTotal(temp.size());
        return RestResponse.success().put("data", page);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string")
    })
    @GetMapping("exportIntegralOrder")
    @ApiOperation(value = "导出当前用户积分排行", notes = "导出当前用户积分排行", response = MallUserBonusDetailEntity.class)
    public RestResponse exportIntegralOrder(HttpServletResponse response) {
        String json = jedisUtil.get(Constant.INTEGRAL_ORDER);
        if (StringUtils.isEmpty(json)) {
            return RestResponse.success();
        }

        List<MallUserBonusDetailEntity> mallUserBonusDetailEntities = JSONObject.parseArray(json, MallUserBonusDetailEntity.class);
        ExcelUtils.exportExcel(mallUserBonusDetailEntities, "当前用户积分排行", "当前用户积分排行", MallUserBonusDetailEntity.class, "当前用户积分排行", response);

        return RestResponse.success();
    }

    @Autowired
    private JedisUtil jedisUtil;
}
