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
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.service.impl.MallDistributionConfServiceImpl;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallDistributionConfEntity;
import com.platform.modules.mall.service.MallDistributionConfService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-07 10:37:39
 */
@RestController
@RequestMapping("mall/distributionconf")
public class MallDistributionConfController extends AbstractController {
    @Autowired
    private MallDistributionConfServiceImpl mallDistributionConfService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallDistributionConfEntity> list = mallDistributionConfService.queryAll(params);

        return RestResponse.success().put("list", list);
    }
    @RequestMapping("/queryAllByPage")
    public RestResponse queryAllByPage(@RequestParam Map<String, Object> params) {
        Page<MallDistributionConfEntity> list = mallDistributionConfService.queryPage(params);
        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @ApiOperation(value = "分销奖励配置", notes = "分销奖励配置", response = MallDistributionConfEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1平台用户, 2店铺", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallDistributionConfService.queryList(params);

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
        MallDistributionConfEntity mallDistributionConf = mallDistributionConfService.getById(id);

        return RestResponse.success().put("distributionconf", mallDistributionConf);
    }

    /**
     * 新增
     *
     * @param mallDistributionConf mallDistributionConf
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody MallDistributionConfEntity mallDistributionConf) {

        mallDistributionConfService.add(mallDistributionConf);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallDistributionConf mallDistributionConf
     * @return RestResponse
     */
    @SysLog("修改一组")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody List<MallDistributionConfEntity> mallDistributionConf) {

        HashSet<Integer> integers = new HashSet<>();
        for (MallDistributionConfEntity mallDistributionConfEntity : mallDistributionConf) {
            if (!integers.add(mallDistributionConfEntity.getOrderUserLevel())) {
                return RestResponse.error("出现相同的等级" + mallDistributionConfEntity.getOrderUserLevel());
            }
        }
        mallDistributionConfService.saveOrUpdateBatch(mallDistributionConf);

        return RestResponse.success();
    }
    @SysLog("修改单个")
    @RequestMapping("/updateOne")
    public RestResponse updateOne(@RequestBody MallDistributionConfEntity mallDistributionConf) {

        MallDistributionConfEntity one = mallDistributionConfService.lambdaQuery().eq(MallDistributionConfEntity::getType, mallDistributionConf.getType()).eq(MallDistributionConfEntity::getOrderUserLevel, mallDistributionConf.getOrderUserLevel()).eq(MallDistributionConfEntity::getLevel, mallDistributionConf.getLevel()).one();
        if (ObjectUtil.isNotEmpty(one)&& one.getId()!=mallDistributionConf.getId()) {
            return RestResponse.error("已存在相同的配置");
        }
        mallDistributionConfService.update(mallDistributionConf);

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
        mallDistributionConfService.deleteBatch(ids);

        return RestResponse.success();
    }
}
