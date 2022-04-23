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
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.BigDecimalUtil;
import com.platform.common.utils.ObjectUtil;
import com.platform.common.utils.ObjectUtils;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallBonusPoolDetailEntity;
import com.platform.modules.mall.entity.MallUserBonusDetailEntity;
import com.platform.modules.mall.service.impl.MallBonusPoolDetailServiceImpl;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallBonusPoolEntity;
import com.platform.modules.mall.service.MallBonusPoolService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-05 16:03:10
 */
@RestController
@RequestMapping("mall/bonuspool")
@Api(tags = "奖金池瓜分")
public class MallBonusPoolController extends AbstractController {
    @Autowired
    private MallBonusPoolService mallBonusPoolService;
    @Autowired
    private MallBonusPoolDetailServiceImpl mallBonusPoolDetailService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallBonusPoolEntity> list = mallBonusPoolService.queryAll(params);

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
        Page<MallBonusPoolEntity> page = mallBonusPoolService.queryPage(params);
        Date now = new Date();
        for (MallBonusPoolEntity record : page.getRecords()) {
            List<MallBonusPoolDetailEntity> list = mallBonusPoolDetailService.lambdaQuery().eq(MallBonusPoolDetailEntity::getBonusPoolId, record.getId()).list();
            record.setMallBonusPoolDetailEntities(list);
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
        MallBonusPoolEntity mallBonusPool = mallBonusPoolService.getById(id);

        return RestResponse.success().put("bonuspool", mallBonusPool);
    }

    /**
     * 新增
     *
     * @param mallBonusPool mallBonusPool
     * @return RestResponse
     */
    @SysLog("新增")
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "新增")
    @Transactional
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "activityTitle", value = "活动标题"),
                    @ExampleProperty(mediaType = "activityDetail", value = "活动详情"),
                    @ExampleProperty(mediaType = "amountTotal", value = "奖金总数"),
                    @ExampleProperty(mediaType = "status", value = "0未开始 1进行中 2已结束"),
                    @ExampleProperty(mediaType = "startTime", value = "开始时间"),
                    @ExampleProperty(mediaType = "endTime", value = "结束时间"),
            }), required = true, dataType = "string")
    })
    public RestResponse save(@RequestBody MallBonusPoolEntity mallBonusPool) {
        if (ObjectUtil.anyEmpty(mallBonusPool.getStartTime(), mallBonusPool.getEndTime(), mallBonusPool.getAmountTotal(), mallBonusPool.getActivityTitle(), mallBonusPool.getActivityDetail())) {
            return RestResponse.error("参数缺失");
        }
        if (mallBonusPool.getEndTime().before(mallBonusPool.getStartTime())) {
            return RestResponse.error("结束时间不能小于开始时间");
        }

        List<MallBonusPoolDetailEntity> mallBonusPoolDetailEntities = mallBonusPool.getMallBonusPoolDetailEntities();

        AtomicInteger preOrder = new AtomicInteger();
        BigDecimal reduce = mallBonusPoolDetailEntities.stream().sorted(Comparator.comparing(MallBonusPoolDetailEntity::getEndOrder)).map(e -> {
            if (BigDecimalUtil.less(e.getStartOrder(), preOrder.get())) {
                throw new BusinessException("排行有误");
            }
            preOrder.set(e.getStartOrder());
            if (BigDecimalUtil.less(e.getEndOrder(), preOrder.get())) {
                throw new BusinessException("排行有误");
            }
            preOrder.set(e.getEndOrder());
            return e.getAwardRatio();
        }).reduce(BigDecimal.ZERO, BigDecimalUtil::add);

        if (!BigDecimalUtil.equal(reduce, 100)) {
            return RestResponse.error("分配比例有误");
        }

        mallBonusPoolService.add(mallBonusPool);
        for (MallBonusPoolDetailEntity mallBonusPoolDetailEntity : mallBonusPoolDetailEntities) {
            mallBonusPoolDetailEntity.setBonusPoolId(mallBonusPool.getId());
        }

        mallBonusPoolDetailService.saveBatch(mallBonusPoolDetailEntities);
        return RestResponse.success();
    }


    /**
     * 修改
     *
     * @param mallBonusPool mallBonusPool
     * @return RestResponse
     */
    @SysLog("修改")
    @PostMapping("/update")
    @Transactional
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "activityTitle", value = "活动标题"),
                    @ExampleProperty(mediaType = "activityDetail", value = "活动详情"),
                    @ExampleProperty(mediaType = "amountTotal", value = "奖金总数"),
                    @ExampleProperty(mediaType = "status", value = "0未开始 1进行中 2已结束"),
                    @ExampleProperty(mediaType = "startTime", value = "开始时间"),
                    @ExampleProperty(mediaType = "endTime", value = "结束时间"),
            }), required = true, dataType = "string")
    })
    @ApiOperation(value = "修改", notes = "修改")
    public RestResponse update(@RequestBody MallBonusPoolEntity mallBonusPool) {
        if (BigDecimalUtil.equal(mallBonusPool.getIsGain(), 1)) {
            return RestResponse.error("已发放的奖励无法修改");
        }
        if (ObjectUtil.anyEmpty(mallBonusPool.getStartTime(), mallBonusPool.getEndTime(), mallBonusPool.getAmountTotal(), mallBonusPool.getActivityTitle(), mallBonusPool.getActivityDetail())) {
            return RestResponse.error("参数缺失");
        }
        if (mallBonusPool.getEndTime().before(mallBonusPool.getStartTime())) {
            return RestResponse.error("结束时间不能小于开始时间");
        }

        List<MallBonusPoolDetailEntity> mallBonusPoolDetailEntities = mallBonusPool.getMallBonusPoolDetailEntities();

        AtomicInteger preOrder = new AtomicInteger();

        BigDecimal reduce = mallBonusPoolDetailEntities.stream().sorted(Comparator.comparing(MallBonusPoolDetailEntity::getEndOrder)).map(e -> {
            if (BigDecimalUtil.less(e.getStartOrder(), preOrder.get())) {
                throw new BusinessException("排行有误");
            }
            preOrder.set(e.getStartOrder());
            if (BigDecimalUtil.less(e.getEndOrder(), preOrder.get())) {
                throw new BusinessException("排行有误");
            }
            preOrder.set(e.getEndOrder());
            return e.getAwardRatio();
        }).reduce(BigDecimal.ZERO, BigDecimalUtil::add);

        if (!BigDecimalUtil.equal(reduce, 100)) {
            return RestResponse.error("分配比例有误");
        }

        mallBonusPoolService.update(mallBonusPool);
        for (MallBonusPoolDetailEntity mallBonusPoolDetailEntity : mallBonusPoolDetailEntities) {
            mallBonusPoolDetailEntity.setBonusPoolId(mallBonusPool.getId());
        }

        mallBonusPoolDetailService.saveOrUpdateBatch(mallBonusPoolDetailEntities);


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
    @Transactional
    public RestResponse delete(@RequestBody Integer[] ids) {
        List<MallBonusPoolEntity> mallBonusPoolEntities = mallBonusPoolService.listByIds(Arrays.asList(ids));
        for (MallBonusPoolEntity mallBonusPoolEntity : mallBonusPoolEntities) {
            if (BigDecimalUtil.equal(mallBonusPoolEntity.getIsGain(), 1)) {
                return RestResponse.error("已发放的奖励无法修改");
            }
        }
        mallBonusPoolService.deleteBatch(ids);

        return RestResponse.success();
    }


}
