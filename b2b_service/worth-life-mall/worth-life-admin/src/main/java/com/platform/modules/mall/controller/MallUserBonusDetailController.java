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
import com.platform.common.utils.ObjectUtil;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.impl.MallUserServiceImpl;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallUserBonusDetailEntity;
import com.platform.modules.mall.service.MallUserBonusDetailService;
import com.platform.utils.ExcelUtils;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-05 17:36:02
 */
@RestController
@RequestMapping("mall/userbonusdetail")
public class MallUserBonusDetailController extends AbstractController {
    @Autowired
    private MallUserBonusDetailService mallUserBonusDetailService;
    @Autowired
    private MallUserServiceImpl mallUserService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:userbonusdetail:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserBonusDetailEntity> list = mallUserBonusDetailService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page<MallUserBonusDetailEntity> page = mallUserBonusDetailService.queryPage(params);

        List<MallUserBonusDetailEntity> records = page.getRecords();
        Set<String> userIds = records.stream().map(MallUserBonusDetailEntity::getUserId).collect(Collectors.toSet());
        if (ObjectUtil.isNotEmpty(userIds)) {
            Map<String, MallUserEntity> id$MallUserEntity = mallUserService.lambdaQuery().in(MallUserEntity::getId, userIds).list().stream().collect(Collectors.toMap(MallUserEntity::getId, e -> e));
            for (MallUserBonusDetailEntity record : records) {
                MallUserEntity mallUserEntity = id$MallUserEntity.get(record.getUserId());
                if (ObjectUtil.isNotEmpty(mallUserEntity)) {
                    record.setNikeName(mallUserEntity.getNickname());
                }
            }
        }

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
        MallUserBonusDetailEntity mallUserBonusDetail = mallUserBonusDetailService.getById(id);

        return RestResponse.success().put("userbonusdetail", mallUserBonusDetail);
    }

    /**
     * 新增
     *
     * @param mallUserBonusDetail mallUserBonusDetail
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody MallUserBonusDetailEntity mallUserBonusDetail) {

        mallUserBonusDetailService.add(mallUserBonusDetail);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallUserBonusDetail mallUserBonusDetail
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody MallUserBonusDetailEntity mallUserBonusDetail) {

        mallUserBonusDetailService.update(mallUserBonusDetail);

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
        mallUserBonusDetailService.deleteBatch(ids);

        return RestResponse.success();
    }


    @PostMapping("exportList")
    @ApiOperation(value = "导出用户积分排行", notes = "导出用户积分排行", response = MallUserBonusDetailEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse exportList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        Page page = mallUserBonusDetailService.queryPage(params);

        List<MallUserBonusDetailEntity> records = page.getRecords();

        Set<String> userIds = records.stream().map(MallUserBonusDetailEntity::getUserId).collect(Collectors.toSet());

        Map<String, MallUserEntity> id$MallUserEntity = mallUserService.lambdaQuery().in(MallUserEntity::getId, userIds).list().stream().collect(Collectors.toMap(MallUserEntity::getId, e -> e));

        for (MallUserBonusDetailEntity record : records) {
            MallUserEntity mallUserEntity = id$MallUserEntity.get(record.getUserId());
            if (ObjectUtil.isNotEmpty(mallUserEntity)) {
                record.setNikeName(mallUserEntity.getNickname());
            }
        }

        ExcelUtils.exportExcel(records, "积分排行榜", "积分排行", MallUserBonusDetailEntity.class, "积分排行", response);

        return RestResponse.success();
    }

}
