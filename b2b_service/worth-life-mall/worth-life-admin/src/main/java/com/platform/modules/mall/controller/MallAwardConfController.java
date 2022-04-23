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
import com.platform.modules.mall.entity.MallUserIntegralLogEntity;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallAwardConfEntity;
import com.platform.modules.mall.service.MallAwardConfService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-04 14:13:25
 */
@RestController
@RequestMapping("mall/awardconf")
public class MallAwardConfController extends AbstractController {
    @Autowired
    private MallAwardConfService mallAwardConfService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallAwardConfEntity> list = mallAwardConfService.queryAll(params);

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
                    @ExampleProperty(mediaType = "type", value = "选填  奖励类型: 1直推邀请 2间推邀请 3直推购买 4间推购买  5自购白酒 6自购海报 7自购全部商品 8店铺认证 9店铺新增直推 10店铺新增间推 "),
            }), required = false, dataType = "string")
    })
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallAwardConfService.queryPage(params);

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
        MallAwardConfEntity mallAwardConf = mallAwardConfService.getById(id);

        return RestResponse.success().put("awardconf", mallAwardConf);
    }

    /**
     * 新增
     *
     * @param mallAwardConf mallAwardConf
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody MallAwardConfEntity mallAwardConf) {

        mallAwardConfService.add(mallAwardConf);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallAwardConf mallAwardConf
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody MallAwardConfEntity mallAwardConf) {

        mallAwardConfService.update(mallAwardConf);

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
        mallAwardConfService.deleteBatch(ids);

        return RestResponse.success();
    }
}
