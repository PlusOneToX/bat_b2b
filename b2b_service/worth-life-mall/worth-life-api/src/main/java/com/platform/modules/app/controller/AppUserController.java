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
package com.platform.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.param.QueryParam;
import com.platform.modules.mall.service.*;
import com.platform.modules.mall.service.impl.*;
import com.platform.modules.mall.vo.DistributionVo;
import com.platform.modules.mall.vo.ShopRecordVo;
import io.swagger.annotations.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李鹏军
 */
@RestController
@RequestMapping("/app/user")
@Api(tags = "AppUserController|APP用户接口")
public class AppUserController {
    @Autowired
    private MallUserLevelService userLevelService;
    @Autowired
    private MallFootprintService footprintService;
    @Autowired
    private MallCollectService collectService;
    @Autowired
    private MallFeedbackService feedbackService;
    @Autowired
    private MallAccountLogService accountLogService;
    @Autowired
    private MallDistService mallDistService;
    @Autowired
    private MallUserBankCardService mallUserBankCardService;
    @Autowired
    private MallBankTypeService mallBankTypeService;
    @Autowired
    private MallPayFaceToFaceService payFaceToFaceService;
    @Autowired
    private MallUserServiceImpl mallUserService;
    @Autowired
    private MallShopAuditServiceImpl mallShopAuditService;
    @Autowired
    private MallBonusPoolServiceImpl mallBonusPoolService;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private MallOrderServiceImpl mallOrderService;
    @Autowired
    private MallUserIntegralService mallUserIntegralService;

    @Autowired
    private MallUserIntegralLogServiceImpl mallUserIntegralLogService;

    /**
     * 根据token获取当前登录用户信息
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取登录用户信息", notes = "根据token获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse userInfo(@LoginUser MallUserEntity user) {
        if (StringUtils.isBlank(user.getInviteCode())) {
            MallUserEntity one = mallUserService.lambdaQuery().eq(MallUserEntity::getId, user.getId()).one();
            if (StringUtils.isBlank(one.getInviteCode())) {
                String serialCode = ShareCodeUtil.toSerialCode(Long.valueOf(one.getId()));
                one.setInviteCode(serialCode);
                mallUserService.saveOrUpdate(one);
            }
            user.setInviteCode(one.getInviteCode());
        }
//        MallUserLevelEntity levelEntity = userLevelService.getById(user.getUserLevelId());

        //查询该用户的用户等级与用户总积分
        if (ObjectUtil.isNotEmpty(user.getRoleType()) && user.getRoleType()!=2){
            RestResponse restResponse1 = this.myLevel(user);

            Map data = (Map) restResponse1.get("data");
            if (MapUtils.isNotEmpty(data)) {
                user.setUserLevelName(data.get("currentDjname").toString());
                //返回用户总积分
                user.setSignAllIntegral(new BigDecimal(data.get("amount").toString()));
//                List<MallUserIntegralLogEntity> list = mallUserIntegralLogService.lambdaQuery().eq(MallUserIntegralLogEntity::getUserId, user.getId()).list();
//                BigDecimal amount =BigDecimal.ZERO;
//                if (ObjectUtil.isNotEmpty(list)){
//                     amount = list.stream().map(MallUserIntegralLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//                }
//                user.setSignAllIntegral(amount);
            } else {
                MallUserLevelEntity levelEntity = userLevelService.getById(user.getUserLevelId());
                user.setUserLevelName(levelEntity.getName());
            }
        }

        List<MallDistributionRecordEntity> list = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getUserId, user.getId()).list();
        //返佣总金额
        BigDecimal reduce = list.stream().map(MallDistributionRecordEntity::getAward).reduce(BigDecimal.ZERO, BigDecimal::add);
        user.setMultiplySum(reduce);
        //查询该用户直推间推总人数
        List<MallUserInviteRecordEntity> countPeople = mallUserService.getCountPeople(user.getId());
        user.setPeopleCount(countPeople.size());


/*        if (null != levelEntity) {
            user.setUserLevelName(levelEntity.getName());
        } else {
            user.setUserLevelName("");
        }*/
        Integer foot = footprintService.lambdaQuery().eq(MallFootprintEntity::getUserId, user.getId()).count();
        user.setMyFootPrint(foot);
        Integer coll = collectService.lambdaQuery().eq(MallCollectEntity::getUserId, user.getId()).count();
        user.setMyCollection(coll);
//        MallDistEntity mallDistEntity = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", user.getId()));
//        user.setIsDistributor(mallDistEntity != null);

        return RestResponse.success().put("data", user);
    }

    /**
     * 查询当前登录用户足迹列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/footprintList")
    @ApiOperation(value = "查询当前登录用户足迹列表", notes = "查询当前登录用户足迹列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse footprintList(@LoginUser MallUserEntity user, @RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer limit) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("page", current);
        params.put("limit", limit);

        IPage data = footprintService.queryPage(params);

        return RestResponse.success().put("data", data);
    }

    /**
     * 删除足迹
     */
    @PostMapping("/deleteFootPrint")
    @ApiOperation(value = "删除足迹", notes = "用户删除足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "footprintId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse deleteFootPrint(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String footprintId = jsonParam.getString("footprintId");
        if (footprintId == null) {
            return RestResponse.error("删除出错");
        }
        MallFootprintEntity footprintEntity = footprintService.getById(footprintId);
        //
        if (loginUser == null || loginUser.getId() == null || footprintEntity == null || footprintEntity.getGoodsId() == null) {
            return RestResponse.error("删除出错");
        }

        Map<String, Object> param = new HashMap<>(4);
        param.put("USER_ID", loginUser.getId());
        param.put("GOODS_ID", footprintEntity.getGoodsId());
        footprintService.removeByMap(param);

        return RestResponse.success("删除成功");
    }

    /**
     * 当前登录用户收藏列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/collectList")
    @ApiOperation(value = "当前登录用户收藏列表", notes = "当前登录用户收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse collectList(@LoginUser MallUserEntity user) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", user.getId());
        List<MallCollectEntity> footprintEntityList = collectService.queryAll(params);
        return RestResponse.success().put("data", footprintEntityList);
    }

    /**
     * 添加或删除用户收藏
     */
    @PostMapping("/addOrDelete")
    @ApiOperation(value = "添加或删除用户收藏", notes = "添加或删除用户收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "goodsId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse addOrDelete(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");

        List<MallCollectEntity> collectEntities = collectService.list(
                new QueryWrapper<MallCollectEntity>().eq("USER_ID", user.getId()).eq("GOODS_ID", goodsId));
        //
        boolean collectRes;
        String handleType = "add";
        if (null == collectEntities || collectEntities.size() < 1) {
            MallCollectEntity collectEntity = new MallCollectEntity();
            collectEntity.setAddTime(new Date());
            collectEntity.setGoodsId(goodsId);
            collectEntity.setUserId(user.getId());
            //添加收藏
            collectRes = collectService.save(collectEntity);
        } else {
            //取消收藏
            collectRes = collectService.delete(collectEntities.get(0).getId());
            handleType = "delete";
        }

        if (collectRes) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("type", handleType);
            return RestResponse.success().put("data", data);
        }
        return RestResponse.error("操作失败");
    }

    /**
     * 添加反馈
     */
    @PostMapping("saveFeedBack")
    @ApiOperation(value = "添加反馈", notes = "用户添加反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "feedType", value = "1"),
                    @ExampleProperty(mediaType = "content", value = "反馈的内容")
            }), required = true, dataType = "string")
    })
    public RestResponse saveFeedBack(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject feedbackJson) {
        MallFeedbackEntity feedbackVo = new MallFeedbackEntity();
        feedbackVo.setUserId(loginUser.getId());
        feedbackVo.setMobile(feedbackJson.getString("mobile"));
        feedbackVo.setFeedType(feedbackJson.getInteger("feedType"));
        feedbackVo.setStatus(1);
        feedbackVo.setContent(feedbackJson.getString("content"));
        feedbackVo.setAddTime(new Date());
        feedbackService.save(feedbackVo);
        return RestResponse.success();
    }

    /**
     * 当前登录用户账户余额变动记录
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("accountLogList")
    @ApiOperation(value = "当前登录用户账户余额变动记录", notes = "当前登录用户账户余额变动记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse accountLogList(@LoginUser MallUserEntity user) {
        List<MallAccountLogEntity> accountLogEntityList = accountLogService.list(
                new QueryWrapper<MallAccountLogEntity>().eq("USER_ID", user.getId()).gt("TYPE", 0).orderByDesc("ADD_TIME"));
        return RestResponse.success().put("data", accountLogEntityList);
    }

    /**
     * 当前登录用户当面付记录
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("payFaceToFaceList")
    @ApiOperation(value = "当前登录用户当面付记录", notes = "当前登录用户当面付记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse payFaceToFaceList(@LoginUser MallUserEntity user) {
        List<MallPayFaceToFaceEntity> payFaceToFaceEntities = payFaceToFaceService.list(
                new QueryWrapper<MallPayFaceToFaceEntity>().eq("USER_ID", user.getId()).eq("PAY_STATUS", 3).orderByDesc("ADD_TIME"));
        return RestResponse.success().put("data", payFaceToFaceEntities);
    }

    /**
     * 当前登录用户银行卡列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/getBankCard")
    @ApiOperation(value = "当前登录用户银行卡列表", notes = "当前登录用户银行卡列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse getBankCard(@LoginUser MallUserEntity user) {
        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        List<MallUserBankCardEntity> list = mallUserBankCardService.getCardList(
                new QueryWrapper<MallUserBankCardEntity>()
                        .eq("USER_ID", userId)
                        .eq("CARD_STATUS", Constant.CardStatus.YBD.getValue())
        );

        list.forEach(r -> {
            String cardNumber = r.getCardNumber();
            int length = cardNumber.length();
            r.setCardNumber(cardNumber.substring(length - 4));
        });

        return RestResponse.success().put("data", list);
    }

    /**
     * 绑定银行卡
     *
     * @param user user
     * @return RestResponse
     */
    @PostMapping("bindingCard")
    @ApiOperation(value = "绑定银行卡", notes = "绑定银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "cardName", value = "收款人姓名"),
                    @ExampleProperty(mediaType = "cardNumber", value = "银行卡号"),
                    @ExampleProperty(mediaType = "cardType", value = "卡类型"),
                    @ExampleProperty(mediaType = "bankTypeId", value = "银行类型ID"),
            }), required = true, dataType = "string")
    })
    public RestResponse bindingCard(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {

        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        String cardName = StringUtils.trimToNull(jsonParam.getString("cardName"));
        String cardNumber = StringUtils.trimToNull(jsonParam.getString("cardNumber"));
        String cardType = StringUtils.trimToNull(jsonParam.getString("cardType"));
        String bankTypeId = StringUtils.trimToNull(jsonParam.getString("bankTypeId"));
        if (cardName == null || cardNumber == null || bankTypeId == null) {
            throw new BusinessException("信息不全！");
        }
        // 先前是否绑定过
        MallUserBankCardEntity entity = mallUserBankCardService.getOne(new QueryWrapper<MallUserBankCardEntity>().eq("CARD_NUMBER", cardNumber).eq("USER_ID", userId));
        if (null != entity) {
            // 是解绑状态就恢复
            if (entity.getCardStatus() == 2) {
                entity.setUserId(userId);
                entity.setCardName(cardName);
                entity.setCardNumber(cardNumber);
                entity.setCardType(cardType);
                entity.setBankTypeId(Integer.valueOf(bankTypeId));
                entity.setCardStatus(Constant.CardStatus.YBD.getValue());
                mallUserBankCardService.update(entity);
                return RestResponse.success();
            }
            throw new BusinessException("已绑定当前卡！");
        }


        MallUserBankCardEntity mallUserBankCardEntity = new MallUserBankCardEntity();
        mallUserBankCardEntity.setUserId(userId);
        mallUserBankCardEntity.setCardName(cardName);
        mallUserBankCardEntity.setCardNumber(cardNumber);
        mallUserBankCardEntity.setCardType(cardType);
        mallUserBankCardEntity.setBankTypeId(Integer.valueOf(bankTypeId));
        mallUserBankCardEntity.setCardStatus(Constant.CardStatus.YBD.getValue());
        mallUserBankCardService.add(mallUserBankCardEntity);

        return RestResponse.success();
    }

    /**
     * 解绑银行卡
     *
     * @param user user
     * @return RestResponse
     */
    @PostMapping("unbindingCard")
    @ApiOperation(value = "解绑银行卡", notes = "解绑银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "id", value = "ID"),
            }), required = true, dataType = "string")
    })
    public RestResponse unbindingCard(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {
        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        // 绑定的银行卡列id
        String id = StringUtils.trimToNull(jsonParam.getString("id"));
        if (id == null) {
            throw new BusinessException("解绑信息有误，请稍后再尝试！");
        }

        MallUserBankCardEntity cardEntity = mallUserBankCardService.getById(id);
        if (cardEntity == null) {
            throw new BusinessException("解绑信息有误，请稍后再尝试！");
        }
        if (!cardEntity.getUserId().equals(userId)) {
            throw new BusinessException("越权，不可解绑他人银行卡");
        }

        cardEntity.setCardStatus(Constant.CardStatus.YJB.getValue());
        mallUserBankCardService.update(cardEntity);

        return RestResponse.success();
    }

    /**
     * 获取银行卡类型表
     */
    @IgnoreAuth
    @GetMapping("bankTypeList")
    @ApiOperation(value = "获取银行卡类型表", notes = "获取银行卡类型表")
    public RestResponse bankTypeList() {
        List<MallBankTypeEntity> list = mallBankTypeService.list(
                new QueryWrapper<>());
        return RestResponse.success().put("data", list);
    }

    /**
     * 提交商户审核
     */
//    @IgnoreAuth
    @PostMapping("subMerchantInfo")
    @ApiOperation(value = "提交商户审核信息", notes = "提交商户审核信息", response = MallShopAuditEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "mallShopAuditEntity", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "shopName", value = "店铺名称"),
                    @ExampleProperty(mediaType = "shopAddress", value = "店铺地址"),
                    @ExampleProperty(mediaType = "phone", value = "电话"),
                    @ExampleProperty(mediaType = "authCode", value = "社会信用代码"),
                    @ExampleProperty(mediaType = "licenseFront", value = "营业执照正面"),
                    @ExampleProperty(mediaType = "licenseVerso", value = "营业执照反面"),
                    @ExampleProperty(mediaType = "legalPerson", value = "法人"),
                    @ExampleProperty(mediaType = "idCardNo", value = "法人身份证号"),
                    @ExampleProperty(mediaType = "identityCardFront", value = "法人身份证正面"),
                    @ExampleProperty(mediaType = "identityCardVerso", value = "身份证反面"),
            }), required = true, dataType = "string")
    })
    @Transactional
    public RestResponse subMerchantInfo(@LoginUser MallUserEntity user, @RequestBody MallShopAuditEntity mallShopAuditEntity) {
        if (StringUtils.isAnyBlank(mallShopAuditEntity.getShopName(), mallShopAuditEntity.getPhone())) {
            return RestResponse.error("参数缺失");
        }

        List<MallShopAuditEntity> list = mallShopAuditService.lambdaQuery().eq(MallShopAuditEntity::getUserId, user.getId()).in(MallShopAuditEntity::getStatus, 0, 1).list();
        if (ObjectUtil.isNotEmpty(list)) {
            return RestResponse.error("请勿重复提交");
        }
        mallShopAuditEntity.setUserId(user.getId()).setCreateTime(new Date()).setStatus(0);
        mallShopAuditService.save(mallShopAuditEntity);
        // mallUserService.getBaseMapper().changeRole(3, user.getId());

        jedisUtil.set(Constant.SHOP_CERTIFICATION + user.getId(), JSONObject.toJSONString(mallShopAuditEntity));
        return RestResponse.success().put("data", mallShopAuditEntity);
    }


    /**
     * 获取提交商户信息
     */
    @GetMapping("getSubMerchantInfo")
    @ApiOperation(value = "获取提交商户信息", notes = "获取提交商户信息", response = MallShopAuditEntity.class)
    public RestResponse getSubMerchantInfo(@LoginUser MallUserEntity user) {

        List<MallShopAuditEntity> list = mallShopAuditService.lambdaQuery().eq(MallShopAuditEntity::getUserId, user.getId()).orderByDesc(MallShopAuditEntity::getCreateTime).list();
        if (ObjectUtil.isEmpty(list)) {
            return RestResponse.success();
        }
        String obj = jedisUtil.get(Constant.SHOP_CERTIFICATION + user.getId());
        if (StringUtils.isNotBlank(obj)) {
            return RestResponse.success().put("data", JSONObject.parse(obj));
        }
        return RestResponse.success().put("data", list.get(0));
    }


    @PostMapping("getIntegralOrder")
    @ApiOperation(value = "获取用户积分排行", notes = "获取用户积分排行", response = MallUserBonusDetailEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse getIntegralOrder(@RequestBody QueryParam queryParam) {
        Page page = new Page();
        String json = jedisUtil.get(Constant.INTEGRAL_ORDER);
        if (StringUtils.isEmpty(json)) {
            List list = new ArrayList<>();
            page.setRecords(list).setPages(queryParam.getPageNo()).setSize(queryParam.getPageSize()).setTotal(0);
            return RestResponse.success().put("data", page);
        }

        List<MallUserBonusDetailEntity> mallUserBonusDetailEntities = JSONObject.parseArray(json, MallUserBonusDetailEntity.class);
        List list = ListUtils.pageBySubList(mallUserBonusDetailEntities, queryParam.getPageNo(), queryParam.getPageSize());

        page.setRecords(list).setSize(queryParam.getPageNo()).setSize(queryParam.getPageSize()).setTotal(mallUserBonusDetailEntities.size());


        return RestResponse.success().put("data", page);
    }

    @GetMapping("getBonusActivity")
    @ApiOperation(value = "获取近期积分活动", notes = "获取近期积分活动", response = MallBonusPoolEntity.class)
    public RestResponse getBonusActivity() {
        MallBonusPoolEntity mallBonusPoolEntity = mallBonusPoolService.lambdaQuery().orderByAsc(MallBonusPoolEntity::getStartTime).last("limit 1").one();
        return RestResponse.success().put("data", mallBonusPoolEntity);
    }


    /**
     * 我的等级
     */
    @PostMapping("myLevel")
    @ApiOperation(value = "我的等级", notes = "我的等级")
    public RestResponse myLevel(@LoginUser MallUserEntity user) {

        //最终返回的集合
        Map<String, Object> resMap = new LinkedHashMap<>();

        //1、查询该用户的累计积分与购买金额
        //积分
        MallUserIntegralEntity userIntegral = mallUserIntegralService.lambdaQuery().eq(MallUserIntegralEntity::getUserId, user.getId()).one();
        BigDecimal amount = BigDecimal.ZERO;
        if (userIntegral != null) {
            amount = userIntegral.getAmount();
        }
        //resMap.put("amount", amount);       //个人总积分

        //金额
//        QueryWrapper<MallOrderEntity> order = new QueryWrapper<>();
//        order.eq("USER_ID",user.getId());
//        order.and(mallOrderEntityQueryWrapper -> mallOrderEntityQueryWrapper.eq("ORDER_STATUS","200").or().eq("ORDER_STATUS","300").or().eq("ORDER_STATUS",301));
//        //查询用户金额
//        List<MallOrderEntity> mallOrder = mallOrderService.list(order);
//        List<MallOrderEntity> mallOrder = mallOrderService.lambdaQuery().eq(MallOrderEntity::getUserId, user.getId()).eq(MallOrderEntity::getOrderStatus, 201).or().eq(MallOrderEntity::getOrderStatus, 300).or().eq(MallOrderEntity::getOrderStatus, 301).list();

        List<MallOrderEntity> mallOrder = mallOrderService.getBaseMapper().queryMallOrder(user.getId());
        BigDecimal actualPrice = BigDecimal.ZERO;
        for (MallOrderEntity mallOrderEntity : mallOrder) {
            actualPrice = actualPrice.add(mallOrderEntity.getActualPrice());
        }
        //resMap.put("actualPrice", actualPrice);    //个人消费总金额

        //2、查询等级梯度信息并过滤出进阶所需要的积分与金额
        List<MallUserLevelEntity> userLevel = userLevelService.list();
        List<BigDecimal> moneyList = new ArrayList<>();     //每个阶段所需金额
        List<BigDecimal> integralList = new ArrayList<>();   //每个阶段所需积分
        LinkedHashMap<String, Object> gradeMap = null;
        ArrayList<LinkedHashMap<String, Object>> gradeList = new ArrayList<>();
//        for (MallUserLevelEntity mallUserLevelEntity:userLevel) {
        for (int i = 0; i < userLevel.size(); i++) {
            //获取每一个阶段的金额--
            String money = userLevel.get(i).getMoney();
            BigDecimal bigMoney = new BigDecimal(money);
            moneyList.add(bigMoney);
            //获取每一个阶段的积分
            String integral = userLevel.get(i).getIntegral();
            BigDecimal bigIntegral = new BigDecimal(integral);
            integralList.add(bigIntegral);

            //3、查询用户等级及解锁的标记
            gradeMap = new LinkedHashMap<>();
            //如果个人积分大于进阶所需积分且个人金额大于进阶所需金额，则表明该阶段已经解锁
            if (amount.compareTo(bigIntegral) > -1 && actualPrice.compareTo(bigMoney) > -1) {
                //如果是已激活的最后一个阶段，则不需要加后缀
                if (i == userLevel.size() - 1) {
                    gradeMap.put("name", userLevel.get(i).getName());
                    gradeMap.put("newIntegral", userLevel.get(i).getIntegral());
                    gradeMap.put("newMoney", userLevel.get(i).getMoney());
                    gradeMap.put("integral", userLevel.get(i).getIntegral());
                    gradeMap.put("money", userLevel.get(i).getMoney());
                    gradeMap.put("activation", 1);
                } else {
                    //已激活的其它
                    gradeMap.put("name", userLevel.get(i).getName());

                    //获取金额的第一个.的位置
                    int i1 = userLevel.get(i).getMoney().indexOf(".");
                    int i2 = userLevel.get(i + 1).getMoney().indexOf(".");

                    gradeMap.put("newIntegral", userLevel.get(i).getIntegral().replace(".0", "") + "-" + userLevel.get(i + 1).getIntegral());
                    gradeMap.put("newMoney", userLevel.get(i).getMoney().substring(0, i1) + "-" + userLevel.get(i + 1).getMoney().substring(0, i2));
                    gradeMap.put("integral", userLevel.get(i).getIntegral());
                    gradeMap.put("money", userLevel.get(i).getMoney());
                    gradeMap.put("activation", 1);
                }
            } else {
                //否则表示未激活的
                //如果是第一个等级，则标识点亮
                if (Integer.parseInt(userLevel.get(i).getId().toString()) == 1) {
                    gradeMap.put("name", userLevel.get(i).getName());
                    gradeMap.put("newIntegral", userLevel.get(i).getIntegral().replace(".0", "") + "-" + userLevel.get(i + 1).getIntegral());

                    //获取金额的第一个.的位置
                    int i1 = userLevel.get(i).getMoney().indexOf(".");
                    int i2 = userLevel.get(i + 1).getMoney().indexOf(".");

                    gradeMap.put("newMoney", userLevel.get(i).getMoney().substring(0, i1) + "-" + userLevel.get(i + 1).getMoney().substring(0, i2));
                    gradeMap.put("integral", userLevel.get(i).getIntegral());
                    gradeMap.put("money", userLevel.get(i).getMoney());
                    gradeMap.put("activation", 1);
                } else {
                    //如果是未激活的最后一个
                    if (i == userLevel.size() - 1) {
                        gradeMap.put("name", userLevel.get(i).getName());
                        gradeMap.put("newIntegral", userLevel.get(i).getIntegral());
                        gradeMap.put("integral", userLevel.get(i).getIntegral());
                        gradeMap.put("money", userLevel.get(i).getMoney());
                        gradeMap.put("newMoney", userLevel.get(i).getMoney());
                        gradeMap.put("activation", 0);
                    } else {
                        //未激活的其它
                        gradeMap.put("newIntegral", userLevel.get(i).getIntegral().replace(".0", "") + "-" + userLevel.get(i + 1).getIntegral());

                        //获取金额的第一个.的位置
                        int i1 = userLevel.get(i).getMoney().indexOf(".");
                        int i2 = userLevel.get(i + 1).getMoney().indexOf(".");

                        gradeMap.put("newMoney", userLevel.get(i).getMoney().substring(0, i1) + "-" + userLevel.get(i + 1).getMoney().substring(0, i2));
                        gradeMap.put("name", userLevel.get(i).getName());
                        gradeMap.put("integral", userLevel.get(i).getIntegral());
                        gradeMap.put("money", userLevel.get(i).getMoney());
                        gradeMap.put("activation", 0);
                    }
                }
            }
            gradeList.add(gradeMap);
        }
        //4、获取所在的等级及下个等级所需要的积分与金额
        Map<String, Object> activMap = new LinkedHashMap<>();
        int i = 0;
        for (LinkedHashMap<String, Object> grade : gradeList) {
            int activation = Integer.parseInt(grade.get("activation").toString());
            if (activation == 0) {
                activMap = grade;
                break;
            }
            i++;
        }
        BigDecimal disparityIntegral = null;
        BigDecimal disparityMoney = null;
        BigDecimal integral = null;
        BigDecimal money1 = null;
        if (activMap != null) {
            //用户下阶段达标的所需积分
            integral = new BigDecimal(activMap.get("integral").toString());
            //用户下阶段达标所需金额
            money1 = new BigDecimal(activMap.get("money").toString());
            //计算积分差
            disparityIntegral = BigDecimalUtil.sub(integral, amount);
            //计算金额差
            disparityMoney = BigDecimalUtil.sub(money1, actualPrice);
        }
        //获取用户当前等级
        LinkedHashMap<String, Object> currentDj = gradeList.get(i - 1);
        String currentDjname = currentDj.get("name").toString();

        //如果后台没有直接设置用户等级，则按照查询出来的那套升级流程来运转
        if (BigDecimalUtil.equal(user.getLevel(),0)){
            resMap.put("actualPrice", actualPrice);                  //个人消费总金额
            resMap.put("amount", amount);                            //个人总积分
            resMap.put("nextIntegral", integral);                    //下个等级所需要的积分
            resMap.put("nextMoney", money1);                         //下个等级所需要的金额
            resMap.put("disparityIntegral", disparityIntegral);      //距离下个等级还需的积分
            resMap.put("disparityMoney", disparityMoney);            //距离下个等级还需的金额
            resMap.put("currentDjname", currentDjname);              //用户当前等级
            resMap.put("moneyList", moneyList);                      //每个阶段所需金额
            resMap.put("integralList", integralList);                //每个阶段所需积分
            resMap.put("gradeList", gradeList);                      //点亮的等级梯度
        }else{
          //从后台设置了用户等级
          //查询用户等级
            MallUserLevelEntity one = mallUserLevelService.lambdaQuery().eq(MallUserLevelEntity::getLevelValue, user.getLevel()).one();
            Integer levelValue = one.getLevelValue();

            resMap.put("actualPrice", actualPrice);                  //个人消费总金额
            resMap.put("amount", amount);                            //个人总积分
            resMap.put("currentDjname", one.getName());              //用户当前等级
            resMap.put("moneyList", moneyList);                      //每个阶段所需金额
            resMap.put("integralList", integralList);                //每个阶段所需积分
            resMap.put("gradeList", gradeList);                      //点亮的等级梯度
            //点亮等级
            for (int z=0;z<levelValue;z++){
                LinkedHashMap<String, Object> map = gradeList.get(z);
                map.put("activation",1);
            }
        }
        return RestResponse.success().put("data", resMap);
    }


    /**
     * 积分明细
     */
    @PostMapping("pointsDetails")
    @ApiOperation(value = "积分明细", notes = "积分明细")
    public RestResponse pointsDetails(@RequestBody QueryParam queryParam, @LoginUser MallUserEntity user) {
        Page<MallUserIntegralLogEntity> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        Page<MallUserIntegralLogEntity> page1 = mallUserIntegralLogService.lambdaQuery().eq(MallUserIntegralLogEntity::getUserId, user.getId()).orderByDesc(MallUserIntegralLogEntity::getCreateTime).page(page);

        //获取用户总积分
        MallUserIntegralEntity userIntegral = mallUserIntegralService.lambdaQuery().eq(MallUserIntegralEntity::getUserId, user.getId()).one();
        BigDecimal amount = BigDecimal.ZERO;
        if (userIntegral != null) {
            amount = userIntegral.getAmount();
        }
        //将总积分存进去返回
        for (MallUserIntegralLogEntity mallUserIntegralLogEntity : page1.getRecords()) {
            mallUserIntegralLogEntity.setIntegralCount(amount);
        }
        return RestResponse.success().put("data", page1);
    }


    @PostMapping("invitedRecord")
    @ApiOperation(value = "获取用户邀请记录", notes = "获取用户邀请记录", response = MallUserInviteRecordEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse invitedRecord(@LoginUser MallUserEntity user, @RequestBody QueryParam queryParam) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", queryParam.getPageNo());
        params.put("limit", queryParam.getPageSize());
        params.put("sidx", "create_time");
        params.put("desc", false);
        params.put("userId", user.getId());
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        Page<MallUserInviteRecordEntity> mallUserInviteRecordEntities = mallUserInviteRecordService.getBaseMapper().selectAllInviteRecordPage(page, params);
        Set<String> userIds = mallUserInviteRecordEntities.getRecords().stream().map(MallUserInviteRecordEntity::getUserId).collect(Collectors.toSet());

        List<MallUserLevelEntity> mallUserLevelConfEntities = mallUserLevelService.lambdaQuery().list();

        if (ObjectUtil.isNotEmpty(userIds)) {
            Map<String, List<MallOrderEntity>> userId$MallOrderEntityList = mallOrderService.lambdaQuery().in(MallOrderEntity::getUserId, userIds).list().stream().collect(Collectors.groupingBy(MallOrderEntity::getUserId));


            Map<String, MallUserIntegralEntity> userId$MallUserIntegralEntity = mallUserIntegralService.lambdaQuery().in(MallUserIntegralEntity::getUserId, userIds).list().stream().collect(Collectors.toMap(MallUserIntegralEntity::getUserId, e -> e));
            for (MallUserInviteRecordEntity mallUserInviteRecordEntity : mallUserInviteRecordEntities.getRecords()) {
                MallUserIntegralEntity mallUserIntegralEntity = userId$MallUserIntegralEntity.get(mallUserInviteRecordEntity.getUserId());
                List<MallOrderEntity> mallOrderEntities = userId$MallOrderEntityList.get(mallUserInviteRecordEntity.getUserId());
                if (ObjectUtil.isNotEmpty(mallOrderEntities)) {
                    BigDecimal reduce = mallOrderEntities.stream().map(MallOrderEntity::getActualPrice).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
                    Optional<MallUserLevelEntity> first = mallUserLevelConfEntities.stream().filter(e -> !BigDecimalUtil.greater(e.getMoney(), reduce) && !BigDecimalUtil.greater(e.getIntegral(), mallUserIntegralEntity.getAmount())).sorted(Comparator.comparing(MallUserLevelEntity::getLevelValue).reversed()).findFirst();
                    if (first.isPresent()) {
                        mallUserInviteRecordEntity.setLevelName(first.get().getName());
                    }
                } else {
                    BigDecimal reduce = BigDecimal.ZERO;
                    Optional<MallUserLevelEntity> first = mallUserLevelConfEntities.stream().filter(e -> !BigDecimalUtil.greater(e.getMoney(), reduce) && !BigDecimalUtil.greater(e.getIntegral(), mallUserIntegralEntity.getAmount())).sorted(Comparator.comparing(MallUserLevelEntity::getLevelValue).reversed()).findFirst();
                    if (first.isPresent()) {
                        mallUserInviteRecordEntity.setLevelName(first.get().getName());
                    }
                }

            }
        }
        List<MallUserInviteRecordEntity> collect = mallUserInviteRecordEntities.getRecords().stream().sorted(Comparator.comparing(MallUserInviteRecordEntity::getCreateTime).reversed()).collect(Collectors.toList());
        mallUserInviteRecordEntities.setRecords(collect);

        return RestResponse.success().put("data", mallUserInviteRecordEntities);
    }


    @PostMapping("getAwardIntegralRecord")
    @ApiOperation(value = "获取我的积分奖励", notes = "获取我的积分奖励", response = MallUserInviteRecordEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse getAwardIntegralRecord(@RequestBody QueryParam queryParam, @LoginUser MallUserEntity loginUser) {
        Page<MallOrderAwardRecordEntity> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        Page<MallOrderAwardRecordEntity> data = mallOrderAwardRecordService.lambdaQuery().eq(MallOrderAwardRecordEntity::getUserId, loginUser.getId()).page(page);
        return RestResponse.success().put("data", data);
    }

    @PostMapping("getDistributionRecord")
    @ApiOperation(value = "推广奖励", notes = "推广奖励", response = MallDistributionRecordEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = true, dataType = "string")
    })
    public RestResponse getDistributionRecord(@RequestBody QueryParam queryParam, @LoginUser MallUserEntity loginUser) {
        Page<MallDistributionRecordEntity> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        Page<MallDistributionRecordEntity> data;
        if (ObjectUtil.isNotEmpty(queryParam.getSubordinateId())) {
            data = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getUserId, loginUser.getId()).eq(MallDistributionRecordEntity::getSubordinateId, queryParam.getSubordinateId()).page(page);
        } else {
            data = mallDistributionRecordService.lambdaQuery().eq(MallDistributionRecordEntity::getUserId, loginUser.getId()).page(page);
        }
        Set<String> ids = data.getRecords().stream().map(MallDistributionRecordEntity::getOriginId).collect(Collectors.toSet());
        if (ObjectUtil.isNotEmpty(ids)) {
            Map<String, MallUserEntity> id$MallUserEntity = mallUserService.lambdaQuery().in(MallUserEntity::getId, ids).list().stream().collect(Collectors.toMap(MallUserEntity::getId, e -> e));
            for (MallDistributionRecordEntity record : data.getRecords()) {
                MallUserEntity mallUserEntity = id$MallUserEntity.get(record.getOriginId());
                record.setOriginNikeName(mallUserEntity.getNickname());
            }
        }

        return RestResponse.success().put("data", data);
    }


    @GetMapping("teamDistributionVo")
    @ApiOperation(value = "获取团队分销Vo", notes = "获取团队分销Vo", response = DistributionVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse teamDistributionVo(@LoginUser MallUserEntity loginUser) {
        DistributionVo distributionVo = mallUserInviteRecordService.getBaseMapper().teamDistributionVo(loginUser.getId());
        return RestResponse.success().put("data", distributionVo);
    }

    @GetMapping("shopDistributionVo")
    @ApiOperation(value = "获取店铺分销Vo", notes = "获取店铺分销Vo", response = DistributionVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse shopDistributionVo(@LoginUser MallUserEntity loginUser) {
        DistributionVo distributionVo = mallUserInviteRecordService.getBaseMapper().shopDistributionVo(loginUser.getId());
        return RestResponse.success().put("data", distributionVo);
    }

    @PostMapping("shopRecord")
    @ApiOperation(value = "获取我推广的店铺记录", notes = "获取我推广的店铺记录", response = ShopRecordVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse shopRecord(@LoginUser MallUserEntity loginUser, @RequestBody QueryParam queryParam) {
        Page<ShopRecordVo> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        Page<ShopRecordVo> shopRecords = mallUserInviteRecordService.getBaseMapper().shopRecord(page, loginUser.getId());
        return RestResponse.success().put("data", shopRecords);
    }

    @PostMapping("shopDistributionDetail")
    @ApiOperation(value = "分销店铺详情", notes = "分销店铺详情", response = MallShopsEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse shopDistributionDetail(@LoginUser MallUserEntity loginUser, @RequestBody QueryParam queryParam) {
        MallShopsEntity mallShopsEntity = mallShopsService.lambdaQuery().eq(MallShopsEntity::getUserId, queryParam.getShopUserId()).one();
        BigDecimal reduce = mallDistributionRecordService.lambdaQuery().select(MallDistributionRecordEntity::getAward).eq(MallDistributionRecordEntity::getOriginId, queryParam.getShopUserId()).eq(MallDistributionRecordEntity::getUserId, loginUser.getId()).list().stream().map(MallDistributionRecordEntity::getAward).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
        mallShopsEntity.setAwardTotal(reduce);
        MallUserEntity one = mallUserService.lambdaQuery().eq(MallUserEntity::getId, queryParam.getShopUserId()).one();
        mallShopsEntity.setUserNikeName(one.getNickname());
        return RestResponse.success().put("data", mallShopsEntity);
    }

    @GetMapping("shopDetail")
    @ApiOperation(value = "我的店铺详情", notes = "我的店铺详情", response = MallShopsEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse shopDetail(@LoginUser MallUserEntity loginUser) {
        MallShopsEntity mallShopsEntity = mallShopsService.lambdaQuery().eq(MallShopsEntity::getUserId, loginUser.getId()).one();
        BigDecimal reduce = BigDecimal.ZERO;
        BigDecimal amount = mallDistributionRecordService.lambdaQuery().select(MallDistributionRecordEntity::getAward).eq(MallDistributionRecordEntity::getUserId, loginUser.getId()).list().stream().map(MallDistributionRecordEntity::getAward).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
        if (ObjectUtil.isNotEmpty(amount)) {
            reduce = amount;
        }
        mallShopsEntity.setAwardTotal(reduce);
        MallUserInviteRecordEntity mallUserInviteRecordEntity = mallUserInviteRecordService.lambdaQuery().eq(MallUserInviteRecordEntity::getUserId, loginUser.getId()).one();
        if (ObjectUtil.isNotEmpty(mallUserInviteRecordEntity)) {
            mallShopsEntity.setInviteUserNikeName(mallUserInviteRecordEntity.getInviteUserName());
        }
        return RestResponse.success().put("data", mallShopsEntity);
    }

    @PostMapping("integralLog")
    @ApiOperation(value = "积分记录", notes = "积分记录", response = DistributionVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse integralLog(@LoginUser MallUserEntity loginUser, @RequestBody QueryParam queryParam) {
        Page<MallUserIntegralLogEntity> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        Page<MallUserIntegralLogEntity> data = mallUserIntegralLogService.lambdaQuery().eq(MallUserIntegralLogEntity::getUserId, loginUser.getId()).in(MallUserIntegralLogEntity::getType, queryParam.getTypes()).page(page);
        return RestResponse.success().put("data", data);
    }

    @GetMapping("getUserInfo")
    @ApiOperation(value = "获取用户最新信息", notes = "获取用户最新信息", response = DistributionVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse getUserInfo(@LoginUser MallUserEntity loginUser) {
        MallUserEntity mallUserEntity = mallUserService.lambdaQuery().eq(MallUserEntity::getId, loginUser.getId()).one();
        return RestResponse.success().put("data", mallUserEntity);
    }


    @Autowired
    private MallShopsServiceImpl mallShopsService;
    @Autowired
    private MallDistributionRecordServiceImpl mallDistributionRecordService;
    @Autowired
    private MallOrderAwardRecordServiceImpl mallOrderAwardRecordService;
    @Autowired
    private MallUserInviteRecordServiceImpl mallUserInviteRecordService;
    @Autowired
    private MallUserLevelServiceImpl mallUserLevelService;


}
