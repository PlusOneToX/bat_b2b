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
import com.platform.common.mall.MallUserLevel;
import com.platform.common.utils.*;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.excel.*;
import com.platform.modules.mall.service.MallDistributionRecordService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.impl.MallDistributionRecordServiceImpl;
import com.platform.modules.mall.service.impl.MallShopsServiceImpl;
import com.platform.modules.mall.service.impl.MallUserInviteRecordServiceImpl;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.utils.ExcelUtils;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 邀请记录表Controller
 *
 * @author pfrong
 * @since 2020-11-10 14:35:24
 */
@RestController
@RequestMapping("mall/userinviterecord")
public class MallUserInviteRecordController extends AbstractController {
    @Autowired
    private MallUserInviteRecordServiceImpl mallUserInviteRecordService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallUserLevel mallUserLevel;
    @Autowired
    private MallDistributionRecordServiceImpl mallDistributionRecordService;
    @Autowired
    private MallShopsServiceImpl mallShopsService;


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:userinviterecord:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserInviteRecordEntity> list = mallUserInviteRecordService.queryAll(params);
        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询团队邀请记录表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    public RestResponse list(@RequestParam Map<String, Object> params) {

        Page<MallUserInviteRecordEntity> page = mallUserInviteRecordService.queryPage(params);

        Integer indirect = 0;       //间推
        Integer directCount = 0;     //直推
        Integer indirectCount = 0;
        for (MallUserInviteRecordEntity record : page.getRecords()) {

            //获取该用户的信息
            MallUserEntity user = userService.getById(record.getInviteUserId());
            //存储用户账号
            if (ObjectUtil.isNotEmpty(user)) {
                //如果手机号为空，则用用户名替换
                if (StringUtils.isNotEmpty(user.getMobile())) {
                    record.setUserAccountNumber(user.getMobile());
                } else {
                    record.setUserAccountNumber(user.getNickname());
                }
            } else {
                record.setUserAccountNumber("");
            }
            //获取该用户的等级
            if (ObjectUtil.isNotEmpty(user)) {
                MallUserLevelEntity mallUserLevelEntity = mallUserLevel.myLevel(record.getInviteUserId()).get();
                record.setLevelName(mallUserLevelEntity.getName());
            }

            //计算直推数量
            directCount = mallUserInviteRecordService.lambdaQuery().eq(MallUserInviteRecordEntity::getInviteUserId, record.getInviteUserId()).count();
            //查询当前用户所有直推过的用户信息
            List<MallUserInviteRecordEntity> directUserId = mallUserInviteRecordService.lambdaQuery().eq(MallUserInviteRecordEntity::getInviteUserId, record.getInviteUserId()).list();
            for (MallUserInviteRecordEntity mallUserInviteRecordEntity : directUserId) {
                //查询直推过的用户他们所直推的人数，即为间推
                indirectCount = mallUserInviteRecordService.lambdaQuery().eq(MallUserInviteRecordEntity::getInviteUserId, mallUserInviteRecordEntity.getUserId()).count();
                indirect += indirectCount;
            }
            //返回直推数量
            record.setDirectCount(directCount);
            //返回间推数量
            record.setIndirectCount(indirect);
        }
        return RestResponse.success().put("page", page);
    }

    @GetMapping("/exportInviteShopListShopDetailRecordOrderDetail")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情-店铺用户记录-下单详情", notes = "推广者分销奖励记录-店铺详情-店铺用户记录-下单详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "originId", value = "店铺推广用户id"),
                    @ExampleProperty(mediaType = "userId", value = "推广者id"),
            }), required = true, dataType = "string")
    })
    public void exportInviteShopListShopDetailRecordOrderDetail(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallDistributionRecordEntity> list = mallDistributionRecordService.queryPage(params);

        ArrayList<OrderDetailExcel> orderDetailExcels = new ArrayList<>();
        for (MallDistributionRecordEntity record : list.getRecords()) {
            OrderDetailExcel orderDetailExcel = new OrderDetailExcel();
            BeanUtils.copyProperties(record, orderDetailExcel);
            orderDetailExcels.add(orderDetailExcel);
            ExcelUtils.exportExcel(orderDetailExcels, "下单详情", "下单详情", OrderDetailExcel.class, "下单详情", response);
        }
    }

    @GetMapping("/inviteShopListShopDetailRecordOrderDetail")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情-店铺用户记录-下单详情", notes = "推广者分销奖励记录-店铺详情-店铺用户记录-下单详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "originId", value = "店铺推广用户id"),
                    @ExampleProperty(mediaType = "userId", value = "推广者id"),
            }), required = true, dataType = "string")
    })
    public RestResponse inviteShopListShopDetailRecordOrderDetail(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallDistributionRecordEntity> list = mallDistributionRecordService.queryPage(params);


        return RestResponse.success().put("data", list);
    }


    @PostMapping("/exportTeamInvitationRecord")
    @ApiOperation(value = "导出团队邀请记录", notes = "导出团队邀请记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportTeamInvitationRecord(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //获取团队邀请记录信息
        RestResponse list1 = this.list(params);
        Page page = (Page) list1.get("page");
        List<MallUserInviteRecordEntity> list = page.getRecords();
        ArrayList<TeamInvitationRecordExcel> TeamInvitationRecordExcelList = new ArrayList<>();

        for (MallUserInviteRecordEntity record : list) {
            TeamInvitationRecordExcel teamInvitationRecordExcel = new TeamInvitationRecordExcel();
            BeanUtils.copyProperties(record, teamInvitationRecordExcel);
            TeamInvitationRecordExcelList.add(teamInvitationRecordExcel);
        }

        ExcelUtils.exportExcel(TeamInvitationRecordExcelList, "团队邀请记录", "团队邀请记录", TeamInvitationRecordExcel.class, "团队邀请记录", response);
    }


    /**
     * 分页查询团队邀请详情记录分页
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/queryAllById")
    public RestResponse queryAllById(@RequestParam Map<String, Object> params) {

        Page<MallUserInviteRecordEntity> page = mallUserInviteRecordService.queryPageById(params);

        for (MallUserInviteRecordEntity record : page.getRecords()) {
            //获取该用户的信息
            MallUserEntity user = userService.getById(record.getUserId());
            //存储用户账号
            if (ObjectUtil.isNotEmpty(user)) {
                //如果手机号为空，则用用户名替换
                if (StringUtils.isNotEmpty(user.getMobile())) {
                    record.setUserAccountNumber(user.getMobile());
                } else {
                    record.setUserAccountNumber(user.getNickname());
                }
            } else {
                record.setUserAccountNumber("");
            }


            //存储用户等级
            MallUserLevelEntity mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId()).get();
            if (ObjectUtil.isNotEmpty(mallUserLevelEntity)) {
                record.setLevelName(mallUserLevelEntity.getName());
            } else {
                record.setLevelName("");
            }

            //下单总共金额，返佣总金额
            // List<MallDistributionRecordEntity> mallDistributionRecordEntity = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getOriginId, record.getUserId()).list();
            List<MallDistributionRecordEntity> mallDistributionRecordEntity = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getOriginId, record.getUserId()).eq(MallDistributionRecordEntity::getRelation, record.getRelation()).list();
            //该用户下单的全部总金额
            BigDecimal orderActualPrice = BigDecimal.ZERO;
            //返佣总金额
            BigDecimal multiplySum = BigDecimal.ZERO;

            for (MallDistributionRecordEntity mallDistributionRecordEntity1 : mallDistributionRecordEntity) {
                //购买总金额
                BigDecimal orderPrice = mallDistributionRecordEntity1.getActualPrice();
                orderActualPrice = BigDecimalUtil.add(orderActualPrice, orderPrice);

                //返佣总金额
                BigDecimal award = mallDistributionRecordEntity1.getAward();
                multiplySum = BigDecimalUtil.add(multiplySum, award);
            }
            record.setOrderActualPrice(orderActualPrice);
            record.setRatioSum(multiplySum);
        }
        return RestResponse.success().put("page", page);
    }

    @PostMapping("/exportTeametails")
    @ApiOperation(value = "导出团队详情", notes = "导出团队详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportTeametails(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //获取团队详情
        RestResponse list1 = this.queryAllById(params);
        Page page = (Page) list1.get("page");
        List<MallUserInviteRecordEntity> list = page.getRecords();

        ArrayList<TeamDetailsExcel> TeamDetailsExcelExcel = new ArrayList<>();

        for (MallUserInviteRecordEntity record : list) {
            TeamDetailsExcel teamDetailsExcel = new TeamDetailsExcel();
            BeanUtils.copyProperties(record, teamDetailsExcel);
            TeamDetailsExcelExcel.add(teamDetailsExcel);
        }

        ExcelUtils.exportExcel(TeamDetailsExcelExcel, "团队详情", "团队详情", TeamDetailsExcel.class, "团队详情", response);
    }


    /**
     * 分页查询团队用户下单详情
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/queryUserOrderDesc")
    public RestResponse queryUserOrderDesc(@RequestParam Map<String, Object> params) {
        Page<MallDistributionRecordEntity> page = mallDistributionRecordService.queryUserOrderDesc(params);
        for (MallDistributionRecordEntity record : page.getRecords()) {
            //获取下单用户id
            String originId = record.getOriginId();

            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(originId);
            if (mallUserLevelEntity.isPresent()) {
                //添加用户折扣
                record.setDiscount(mallUserLevelEntity.get().getDiscount());
            }
        }
        return RestResponse.success().put("page", page);
    }

    @GetMapping("/exportUserOrderDesc")
    @ApiOperation(value = "导出团队用户下单详情", notes = "导出团队用户下单详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportUserOrderDesc(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //获取团队详情
        RestResponse list1 = this.queryUserOrderDesc(params);
        Page page = (Page) list1.get("page");
        List<MallDistributionRecordEntity> list = page.getRecords();

        ArrayList<MallUserOrderDescExcel> mallUserOrderDescList = new ArrayList<>();

        for (MallDistributionRecordEntity record : list) {
            MallUserOrderDescExcel mallUserOrderDescExcel = new MallUserOrderDescExcel();
            BeanUtils.copyProperties(record, mallUserOrderDescExcel);
            mallUserOrderDescList.add(mallUserOrderDescExcel);
        }

        ExcelUtils.exportExcel(mallUserOrderDescList, "导出团队用户下单详情", "导出团队用户下单详情", MallUserOrderDescExcel.class, "导出团队用户下单详情", response);
    }


    /**
     * 分页查询店铺分销奖励记录
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/shopList")
    public RestResponse shopListPage(@RequestParam Map<String, Object> params) {
        Page<MallUserEntity> page = new Page<MallUserEntity>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
        //获取商铺用户
        Object userId = params.get("userId");

        QueryWrapper<MallUserEntity> wrapper = new QueryWrapper<MallUserEntity>().eq("role_type", 2);
        if (ObjectUtil.isNotEmpty(userId)) {
            wrapper.eq("id",userId.toString());
        }
        Page<MallUserEntity> mallUserPage = userService.page(page, wrapper);
        for (MallUserEntity mallUserEntity : mallUserPage.getRecords()) {
            //如果手机号为空不做处理，否则用用户名替换
            if (StringUtils.isNotBlank(mallUserEntity.getMobile())) {
            } else {
                mallUserEntity.setMobile(mallUserEntity.getNickname());
            }

            //根据店铺id查询该店铺的所有直推间推用户
            List<MallUserInviteRecordEntity> mallUserInviteRecordEntities = mallUserInviteRecordService.queryUserAll(mallUserEntity.getId());

            //该用户下单的全部总金额
            BigDecimal orderActualPrice = BigDecimal.ZERO;
            //返佣总金额
            BigDecimal multiplySum = BigDecimal.ZERO;

            for (MallUserInviteRecordEntity mallUserInviteRecordEntity : mallUserInviteRecordEntities) {
                //根据用户id查询用户的下单金额与店铺的返佣总额
                List<MallDistributionRecordEntity> mallDistributionRecordEntity = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getOriginId, mallUserInviteRecordEntity.getUserId()).eq(MallDistributionRecordEntity::getRelation, mallUserInviteRecordEntity.getRelation()).list();
                for (MallDistributionRecordEntity mallDistribution : mallDistributionRecordEntity) {
                    //购买总金额
                    BigDecimal orderPrice = mallDistribution.getActualPrice();
                    orderActualPrice = BigDecimalUtil.add(orderActualPrice, orderPrice);

                    //返佣总金额
                    BigDecimal award = mallDistribution.getAward();
                    multiplySum = BigDecimalUtil.add(multiplySum, award);
                }
            }
            mallUserEntity.setOrderActualPrice(orderActualPrice);
            mallUserEntity.setMultiplySum(multiplySum);
        }
        return RestResponse.success().put("page", page);
    }


    @PostMapping("/exportShopListPage")
    @ApiOperation(value = "店铺分销奖励记录", notes = "店铺分销奖励记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportShopListPage(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //店铺分销奖励记录
        RestResponse list1 = this.shopListPage(params);
        Page page = (Page) list1.get("page");
        List<MallUserEntity> list = page.getRecords();

        ArrayList<ShopListExcel> ShopListExcelList = new ArrayList<>();

        for (MallUserEntity record : list) {
            ShopListExcel shopListExcel = new ShopListExcel();

            BeanUtils.copyProperties(record, shopListExcel);
            ShopListExcelList.add(shopListExcel);
        }

        ExcelUtils.exportExcel(ShopListExcelList, "店铺分销奖励记录", "店铺分销奖励记录", ShopListExcel.class, "店铺分销奖励记录", response);
    }


    /**
     * 分页查询店铺邀请详情记录分页
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/shopListUserDescPage")
    public RestResponse shopListUserDescPage(@RequestParam Map<String, Object> params) {

        Page<MallUserInviteRecordEntity> page = mallUserInviteRecordService.queryPageById(params);

        for (MallUserInviteRecordEntity record : page.getRecords()) {
            //获取该用户的信息
            MallUserEntity user = userService.getById(record.getUserId());
            //存储用户账号
            if (ObjectUtil.isNotEmpty(user)) {
                //如果手机号为空，则用用户名替换
                if (StringUtils.isNotEmpty(user.getMobile())) {
                    record.setUserAccountNumber(user.getMobile());
                } else {
                    record.setUserAccountNumber(user.getNickname());
                }
            } else {
                record.setUserAccountNumber("");
            }


            //存储用户等级
            MallUserLevelEntity mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId()).get();
            if (ObjectUtil.isNotEmpty(mallUserLevelEntity)) {
                record.setLevelName(mallUserLevelEntity.getName());
            } else {
                record.setLevelName("");
            }

            //下单总共金额，返佣总金额
            List<MallDistributionRecordEntity> mallDistributionRecordEntity = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getOriginId, record.getUserId()).eq(MallDistributionRecordEntity::getRelation, record.getRelation()).list();
            //该用户下单的全部总金额
            BigDecimal orderActualPrice = BigDecimal.ZERO;
            //返佣总金额
            BigDecimal multiplySum = BigDecimal.ZERO;

            for (MallDistributionRecordEntity mallDistributionRecordEntity1 : mallDistributionRecordEntity) {
                //购买总金额
                BigDecimal orderPrice = mallDistributionRecordEntity1.getActualPrice();
                orderActualPrice = BigDecimalUtil.add(orderActualPrice, orderPrice);

                //返佣总金额
                BigDecimal award = mallDistributionRecordEntity1.getAward();
                multiplySum = BigDecimalUtil.add(multiplySum, award);
            }
            record.setOrderActualPrice(orderActualPrice);
            record.setRatioSum(multiplySum);
        }
        return RestResponse.success().put("page", page);
    }


    @PostMapping("/exportShopListUserDescPage")
    @ApiOperation(value = "店铺邀请详情", notes = "店铺邀请详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportShopListUserDescPage(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //店铺邀请详情
        RestResponse list1 = this.shopListUserDescPage(params);
        Page page = (Page) list1.get("page");
        List<MallUserInviteRecordEntity> list = page.getRecords();

        ArrayList<TeamDetailsExcel> TeamDetailsExcelExcel = new ArrayList<>();

        for (MallUserInviteRecordEntity record : list) {
            TeamDetailsExcel teamDetailsExcel = new TeamDetailsExcel();
            BeanUtils.copyProperties(record, teamDetailsExcel);
            TeamDetailsExcelExcel.add(teamDetailsExcel);
        }

        ExcelUtils.exportExcel(TeamDetailsExcelExcel, "店铺邀请详情", "店铺邀请详情", TeamDetailsExcel.class, "店铺邀请详情", response);
    }


    /**
     * 分页查询店铺用户下单详情
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/queryShopUserOrderDesc")
    public RestResponse queryShopUserOrderDesc(@RequestParam Map<String, Object> params) {
        Page<MallDistributionRecordEntity> page = mallDistributionRecordService.queryUserOrderDesc(params);
        for (MallDistributionRecordEntity record : page.getRecords()) {
            //获取下单用户id
            String originId = record.getOriginId();

            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(originId);
            if (mallUserLevelEntity.isPresent()) {
                //添加用户折扣
                record.setDiscount(mallUserLevelEntity.get().getDiscount());
            }
        }
        return RestResponse.success().put("page", page);
    }

    @GetMapping("/exportShopUserOrderDesc")
    @ApiOperation(value = "导出店铺用户下单详情", notes = "导出店铺用户下单详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportShopUserOrderDesc(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //获取团队详情
        RestResponse list1 = this.queryShopUserOrderDesc(params);
        Page page = (Page) list1.get("page");
        List<MallDistributionRecordEntity> list = page.getRecords();

        ArrayList<MallUserOrderDescExcel> mallUserOrderDescList = new ArrayList<>();

        for (MallDistributionRecordEntity record : list) {
            MallUserOrderDescExcel mallUserOrderDescExcel = new MallUserOrderDescExcel();
            BeanUtils.copyProperties(record, mallUserOrderDescExcel);
            mallUserOrderDescList.add(mallUserOrderDescExcel);
        }

        ExcelUtils.exportExcel(mallUserOrderDescList, "导出店铺用户下单详情", "导出店铺用户下单详情", MallUserOrderDescExcel.class, "导出店铺用户下单详情", response);
    }


    @GetMapping("/inviteShopList")
    @ApiOperation(value = "推广者分销奖励记录", notes = "推广者分销奖励记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse inviteShopList(@RequestParam Map<String, Object> params) {
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.inviteShopList(params);
        for (MallUserInviteRecordEntity record : list.getRecords()) {
            BigDecimal reduce = mallDistributionRecordService.lambdaQuery().select().eq(MallDistributionRecordEntity::getUserId, record.getInviteUserId()).list().stream().map(MallDistributionRecordEntity::getAward).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
            record.setAward(reduce);
        }
        return RestResponse.success().put("data", list);
    }

    @GetMapping("/inviteShopListShopDetail")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情", notes = "推广者分销奖励记录-店铺详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "inviteUserId", value = "推广者id"),
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id"),
            }), required = true, dataType = "string")
    })
    public RestResponse inviteShopListShopDetail(@RequestParam Map<String, Object> params) {
        params.put("sidx", "a.user_id");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.getBaseMapper().inviteShopListShopDetail(page, params);

        List<MallUserInviteRecordEntity> records = list.getRecords();
        List<String> userIds = records.stream().map(MallUserInviteRecordEntity::getUserId).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(userIds)) {
            Map<String, MallShopsEntity> userId$MallShopsEntity = mallShopsService.lambdaQuery().in(MallShopsEntity::getUserId, userIds).list().stream().collect(Collectors.toMap(MallShopsEntity::getUserId, e -> e));
            for (MallUserInviteRecordEntity record : records) {
                MallShopsEntity mallShopsEntity = userId$MallShopsEntity.get(record.getUserId());
                if (ObjectUtil.isNotEmpty(mallShopsEntity)) {
                    record.setJoinTime(mallShopsEntity.getCreateTime());
                }
            }
        }
        return RestResponse.success().put("data", list);
    }

    @GetMapping("/exportInviteShopListShopDetail")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情", notes = "推广者分销奖励记录-店铺详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "inviteUserId", value = "推广者id"),
                    @ExampleProperty(mediaType = "userId", value = "用户id"),
                    @ExampleProperty(mediaType = "joinTimeBegin", value = "入驻时间开始"),
                    @ExampleProperty(mediaType = "joinTimeEnd", value = "入驻时间结束"),
            }), required = true, dataType = "string")
    })
    public void exportInviteShopListShopDetail(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("sidx", "a.user_id");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.getBaseMapper().inviteShopListShopDetail(page, params);


        ArrayList<ShopDetailExcel> shopDetailExcels = new ArrayList<>();
        List<MallUserInviteRecordEntity> records = list.getRecords();
        List<String> userIds = records.stream().map(MallUserInviteRecordEntity::getUserId).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(userIds)) {
            Map<String, MallShopsEntity> userId$MallShopsEntity = mallShopsService.lambdaQuery().in(MallShopsEntity::getUserId, userIds).list().stream().collect(Collectors.toMap(MallShopsEntity::getUserId, e -> e));
            for (MallUserInviteRecordEntity record : records) {
                MallShopsEntity mallShopsEntity = userId$MallShopsEntity.get(record.getUserId());
                if (ObjectUtil.isNotEmpty(mallShopsEntity)) {
                    record.setJoinTime(mallShopsEntity.getCreateTime());
                }
            }
        }

        for (MallUserInviteRecordEntity record : records) {
            ShopDetailExcel shopDetailExcel = new ShopDetailExcel();
            BeanUtils.copyProperties(record, shopDetailExcel);
            shopDetailExcels.add(shopDetailExcel);
        }

        ExcelUtils.exportExcel(shopDetailExcels, "店铺详情", "店铺详情", ShopDetailExcel.class, "店铺详情", response);
    }

    /**
     * <if test="params.relation != null  ">
     * AND AA.relation = ${params.relation}
     * </if>
     * <if test="params.subordinate != null  ">
     * AND AA.user_id = ${params.subordinate}
     * </if>
     *
     * @param params
     * @return
     */
    @GetMapping("/inviteShopListShopDetailRecord")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情-店铺用户记录", notes = "推广者分销奖励记录-店铺详情-店铺用户记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id"),
                    @ExampleProperty(mediaType = "inviteUserId", value = "推广者id"),
                    @ExampleProperty(mediaType = "joinTimeBegin", value = "开始时间"),
                    @ExampleProperty(mediaType = "joinTimeEnd", value = "结束时间"),
                    @ExampleProperty(mediaType = "relation", value = "1直接 2间接"),
                    @ExampleProperty(mediaType = "subordinate", value = "用户id"),
            }), required = true, dataType = "string")
    })
    public RestResponse inviteShopListShopDetailRecord(@RequestParam Map<String, Object> params) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.getBaseMapper().selectAllInviteRecordPage(page, params);
        for (MallUserInviteRecordEntity record : list.getRecords()) {
            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId());
            if (mallUserLevelEntity.isPresent()) {
                record.setLevelName(mallUserLevelEntity.get().getName());
            }
            List<MallDistributionRecordEntity> inviteUserId = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getUserId, params.get("inviteUserId")).eq(MallDistributionRecordEntity::getOriginId, record.getUserId()).list();
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal award = BigDecimal.ZERO;
            for (MallDistributionRecordEntity mallDistributionRecordEntity : inviteUserId) {
                totalPrice = BigDecimalUtil.add(mallDistributionRecordEntity.getActualPrice(), totalPrice);
                award = BigDecimalUtil.add(mallDistributionRecordEntity.getAward(), award);
            }

            record.setActualPrice(totalPrice);
            record.setAward(award);

        }

        return RestResponse.success().put("data", list);
    }

    @GetMapping("/exportInviteShopListShopDetailRecord")
    @ApiOperation(value = "推广者分销奖励记录-店铺详情-店铺用户记录", notes = "推广者分销奖励记录-店铺详情-店铺用户记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id"),
                    @ExampleProperty(mediaType = "inviteUserId", value = "推广者id"),
                    @ExampleProperty(mediaType = "joinTimeBegin", value = "开始时间"),
                    @ExampleProperty(mediaType = "joinTimeEnd", value = "结束时间"),
                    @ExampleProperty(mediaType = "relation", value = "1直接 2间接"),
                    @ExampleProperty(mediaType = "subordinate", value = "用户id"),
            }), required = true, dataType = "string")
    })
    public void exportInviteShopListShopDetailRecord(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.getBaseMapper().selectAllInviteRecordPage(page, params);
        ArrayList<ShopRecordExcel> shopRecordExcels = new ArrayList<>();
        for (MallUserInviteRecordEntity record : list.getRecords()) {
            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId());
            if (mallUserLevelEntity.isPresent()) {
                record.setLevelName(mallUserLevelEntity.get().getName());
            }
            List<MallDistributionRecordEntity> inviteUserId = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getUserId, params.get("inviteUserId")).eq(MallDistributionRecordEntity::getOriginId, record.getUserId()).list();
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal award = BigDecimal.ZERO;
            for (MallDistributionRecordEntity mallDistributionRecordEntity : inviteUserId) {
                totalPrice = BigDecimalUtil.add(mallDistributionRecordEntity.getActualPrice(), totalPrice);
                award = BigDecimalUtil.add(mallDistributionRecordEntity.getAward(), award);
            }

            record.setActualPrice(totalPrice);
            record.setAward(award);
            ShopRecordExcel shopRecordExcel = new ShopRecordExcel();
            BeanUtils.copyProperties(record, shopRecordExcel);
            shopRecordExcels.add(shopRecordExcel);
        }


        ExcelUtils.exportExcel(shopRecordExcels, "店铺用户记录", "店铺用户记录", ShopRecordExcel.class, "店铺用户记录", response);
    }

    @GetMapping("/shopUserOrderDetail")
    @ApiOperation(value = "店铺用户下单详情", notes = "店铺用户下单详情 originId", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id:userId"),
                    @ExampleProperty(mediaType = "originId", value = "店铺邀请的用户id:userId"),
                    @ExampleProperty(mediaType = "createTimeBegin", value = "开始时间"),
                    @ExampleProperty(mediaType = "createTimeEnd", value = "结束时间"),
            }), required = true, dataType = "string")
    })
    public RestResponse shopUserOrderDetail(@RequestParam Map<String, Object> params) {
        Page<MallDistributionRecordEntity> page = mallDistributionRecordService.queryPage(params);
        return RestResponse.success().put("page", page);
    }

    @GetMapping("/shopDistributionRecord")
    @ApiOperation(value = "店铺分销奖励记录", notes = "店铺分销奖励记录 originId", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "joinTimeBegin", value = "开始时间"),
                    @ExampleProperty(mediaType = "joinTimeEnd", value = "结束时间"),
            }), required = true, dataType = "string")
    })
    public RestResponse shopDistributionRecord(@RequestParam Map<String, Object> params) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.getBaseMapper().shopDistributionRecord(page, params);
        for (MallUserInviteRecordEntity record : list.getRecords()) {
            params.put("userId", record.getUserId());
            MallDistributionRecordEntity mallDistributionRecordEntity = mallDistributionRecordService.getBaseMapper().sumTotal(params);
            BeanUtils.copyNewPropertites(mallDistributionRecordEntity, record);
        }
        return RestResponse.success().put("page", list);
    }

    @GetMapping("/exportShopUserOrderDetail")
    @RequiresPermissions("mall:userinviterecord:list")
    @ApiOperation(value = "导出店铺用户下单详情", notes = "导出店铺用户下单详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id:userId"),
                    @ExampleProperty(mediaType = "originId", value = "店铺邀请的用户id:userId"),
                    @ExampleProperty(mediaType = "createTimeBegin", value = "开始时间"),
                    @ExampleProperty(mediaType = "createTimeEnd", value = "结束时间"),
            }), required = true, dataType = "string")
    })
    public void exportShopUserOrderDetail(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        Page<MallDistributionRecordEntity> page = mallDistributionRecordService.queryPage(params);
        ExcelUtils.exportExcel(page.getRecords(), "店铺用户下单详情", "店铺用户下单详情", MallDistributionRecordEntity.class, "店铺用户下单详情", response);
    }


    @GetMapping("/exportInviteShopList")
    @ApiOperation(value = "导出店铺邀请奖励记录", notes = "导出店铺邀请奖励记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public void exportInviteShopList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.inviteShopList(params);

        ArrayList<InviteShopExcel> inviteShopExcels = new ArrayList<>();

        for (MallUserInviteRecordEntity record : list.getRecords()) {
            InviteShopExcel inviteShopExcel = new InviteShopExcel();
            BeanUtils.copyProperties(record, inviteShopExcel);
            inviteShopExcels.add(inviteShopExcel);
        }

        ExcelUtils.exportExcel(inviteShopExcels, "店铺邀请奖励记录", "店铺邀请奖励记录", InviteShopExcel.class, "店铺邀请奖励记录", response);
    }


    @GetMapping("/shopDetail")
    @ApiOperation(value = "店铺详情", notes = "店铺详情", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "推广者id: inviteUserId"),

            }), required = true, dataType = "string")
    })
    public RestResponse shopDetail(@RequestParam Map<String, Object> params) {
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.extendUserRecord(params);
        for (MallUserInviteRecordEntity record : list.getRecords()) {
            MallDistributionRecordEntity mallDistributionRecordEntity = mallDistributionRecordService.sumInviteOrderRecord(params);
            if (ObjectUtil.isNotEmpty(mallDistributionRecordEntity)) {
                BeanUtils.copyNewPropertites(mallDistributionRecordEntity, record);
            }
        }
        return RestResponse.success().put("data", list);
    }


    @GetMapping("/exportShopDetail")
    @ApiOperation(value = "导出店铺详情", notes = "导出店铺详情 ", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "推广者id: inviteUserId"),
            }), required = true, dataType = "string")
    })
    public void exportShopDetail(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        Page<MallUserInviteRecordEntity> list = mallUserInviteRecordService.extendUserRecord(params);

        ArrayList<ShopDetailExcel> shopDetailExcels = new ArrayList<>();


        for (MallUserInviteRecordEntity record : list.getRecords()) {
            params.put("userId", record.getUserId());
            MallDistributionRecordEntity mallDistributionRecordEntity = mallDistributionRecordService.sumInviteOrderRecord(params);
            BeanUtils.copyNewPropertites(mallDistributionRecordEntity, record);
            ShopDetailExcel shopDetailExcel = new ShopDetailExcel();
            BeanUtils.copyProperties(record, shopDetailExcel);
            shopDetailExcels.add(shopDetailExcel);
        }
        ExcelUtils.exportExcel(shopDetailExcels, "店铺详情", "店铺详情", ShopDetailExcel.class, "店铺详情", response);
    }


    @GetMapping("/shopUserRecord")
    @ApiOperation(value = "店铺用户记录", notes = "店铺用户记录", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id:userId"),
            }), required = true, dataType = "string")
    })
    public RestResponse shopUserRecord(@RequestParam Map<String, Object> params) {
        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> mallUserInviteRecordEntities = mallUserInviteRecordService.getBaseMapper().selectAllInviteRecordPage(page, params);
        for (MallUserInviteRecordEntity record : mallUserInviteRecordEntities.getRecords()) {
            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId());
            if (mallUserLevelEntity.isPresent()) {
                record.setLevelName(mallUserLevelEntity.get().getName());
            }
            params.put("subordinate", record.getUserId());
            MallDistributionRecordEntity mallDistributionRecordEntity = mallDistributionRecordService.sumInviteOrderRecord(params);
            BeanUtils.copyNewPropertites(mallDistributionRecordEntity, record);
        }

        return RestResponse.success().put("data", mallUserInviteRecordEntities);
    }

    @GetMapping("/exportShopUserRecord")
    @ApiOperation(value = "导出店铺用户记录", notes = "导出店铺用户记录 ", response = MallUserIntegralLogEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
                    @ExampleProperty(mediaType = "userId", value = "店铺用户id:userId"),
            }), required = true, dataType = "string")
    })
    public void exportShopUserRecord(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        ArrayList<ShopUserRecord> shopUserRecords = new ArrayList<>();

        params.put("sidx", "create_time");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> mallUserInviteRecordEntities = mallUserInviteRecordService.getBaseMapper().selectAllInviteRecordPage(page, params);
        for (MallUserInviteRecordEntity record : mallUserInviteRecordEntities.getRecords()) {
            Optional<MallUserLevelEntity> mallUserLevelEntity = mallUserLevel.myLevel(record.getUserId());
            if (mallUserLevelEntity.isPresent()) {
                record.setLevelName(mallUserLevelEntity.get().getName());
            }
            params.put("subordinate", record.getUserId());
            MallDistributionRecordEntity mallDistributionRecordEntity = mallDistributionRecordService.sumInviteOrderRecord(params);
            BeanUtils.copyNewPropertites(mallDistributionRecordEntity, record);
            ShopUserRecord shopUserRecord = new ShopUserRecord();
            BeanUtils.copyProperties(record, shopUserRecord);
            shopUserRecords.add(shopUserRecord);
        }

        ExcelUtils.exportExcel(shopUserRecords, "店铺用户记录", "店铺用户记录", ShopUserRecord.class, "店铺用户记录", response);

    }


    /**
     * 新增邀请记录表
     *
     * @param mallUserInviteRecord mallUserInviteRecord
     * @return RestResponse
     */
    @SysLog("新增邀请记录表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:userinviterecord:save")
    public RestResponse save(@RequestBody MallUserInviteRecordEntity mallUserInviteRecord) {

        mallUserInviteRecordService.add(mallUserInviteRecord);

        return RestResponse.success();
    }

    /**
     * 修改邀请记录表
     *
     * @param mallUserInviteRecord mallUserInviteRecord
     * @return RestResponse
     */
    @SysLog("修改邀请记录表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:userinviterecord:update")
    public RestResponse update(@RequestBody MallUserInviteRecordEntity mallUserInviteRecord) {

        mallUserInviteRecordService.update(mallUserInviteRecord);

        return RestResponse.success();
    }

    /**
     * 根据主键删除邀请记录表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除邀请记录表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:userinviterecord:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserInviteRecordService.deleteBatch(ids);
        return RestResponse.success();
    }
}
