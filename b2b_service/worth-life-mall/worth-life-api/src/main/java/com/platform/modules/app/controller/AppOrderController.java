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
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.app.service.PayService;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "ApiOrderController|订单管理控制器")
@RestController
@RequestMapping("/app/order")
public class AppOrderController {
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallGoodsSkuService goodsSkuService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallCartService cartService;
    @Autowired
    private WxSendMsgService sendMsgService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallCommentService commentService;
    @Autowired
    private MallShopsGoodsService shopsGoodsService;
    @Autowired
    private PayService payService;
    @Autowired
    private MallCouponService couponService;
    @Autowired
    private MallUserCouponService userCouponService;
    @Autowired
    private MallDistPromoService mallDistPromoService;
    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private AppUserController appUserController;
    @Autowired
    private MallUserLevelService mallUserLevelService;


    /**
     * 获取订单列表
     */
    @GetMapping("list")
    @ApiOperation(value = "订单列表", notes = "获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse list(@LoginUser MallUserEntity loginUser, Integer orderStatus, Integer orderType,
                             @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", current);
        params.put("limit", limit);
        params.put("userId", loginUser.getId());
        if (orderStatus != -2) {
            params.put("orderStatus", orderStatus);
        }
        if (Constant.OrderStatus.QRSH.getValue().equals(orderStatus)) {
            params.put("notExists", orderStatus);
        }
        if (orderType != null) {
            params.put("orderType", orderType);
        }

        Page<MallOrderEntity> data = orderService.queryPage(params);
        //
        for (MallOrderEntity item : data.getRecords()) {
            //订单的商品
            List<MallOrderGoodsEntity> goodsList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", item.getId()));
            Integer goodsCount = 0;
            for (MallOrderGoodsEntity orderGoodsEntity : goodsList) {
                goodsCount += orderGoodsEntity.getNumber();
            }
            item.setCommentCount(commentService.count(new QueryWrapper<MallCommentEntity>().eq("ORDER_ID", item.getId())));
            item.setGoodsCount(goodsCount);
            item.setOrderGoodsEntityList(goodsList);
        }
        return RestResponse.success().put("data", data);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("detail")
    @ApiOperation(value = "订单详情", notes = "根据订单ID获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "int", example = "1")
    })
    public RestResponse detail(@LoginUser MallUserEntity loginUser, String orderId) {
        Map<String, Object> resultObj = new HashMap<>(4);
        //
        MallOrderEntity orderInfo = orderService.queryById(orderId);
        if (null == orderInfo) {
            return RestResponse.error("订单不存在！");
        }
        if (!loginUser.getId().equals(orderInfo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        //订单的商品
        List<MallOrderGoodsEntity> orderGoods = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", orderId));

        orderInfo.setCommentCount(commentService.count(new QueryWrapper<MallCommentEntity>().eq("ORDER_ID", orderId)));
        resultObj.put("orderInfo", orderInfo);
        resultObj.put("orderGoods", orderGoods);
        return RestResponse.success(resultObj);
    }

    /**
     * 提交订单
     */
    @PostMapping("submitOrder")
    @ApiOperation(value = "提交订单", notes = "购物车页面提交订单，商品为购物车中已经选中的")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "fromType", value = "用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序"),
                    @ExampleProperty(mediaType = "postscript", value = "订单说明"),
                    @ExampleProperty(mediaType = "checkedAddress", value = "AddressVo"),
                    @ExampleProperty(mediaType = "couponId", value = "couponId"),
                    @ExampleProperty(mediaType = "shopsId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse submitOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String postscript = jsonParam.getString("postscript");
        String shopsId = jsonParam.getString("shopsId");
        String type = jsonParam.getString("type");
        String couponId = jsonParam.getString("couponId");
        String promoUserId = jsonParam.getString("promoUserId");

        Integer fromType = jsonParam.getInteger("fromType");

        // * 获取要购买的商品和总价
        List<MallCartEntity> checkedGoodsList = new ArrayList<>();

        //查询用户所在的等级
        Map<String, Object> user = appUserController.myLevel(loginUser);

        Map data = (Map) user.get("data");
        String Djname =data.get("currentDjname").toString();


        BigDecimal discount =new BigDecimal(1);

        //根据当前用户的等级去查询所享受的折扣
        MallUserLevelEntity currentLevel = mallUserLevelService.lambdaQuery().eq(MallUserLevelEntity::getName, Djname).one();
        if (currentLevel!=null){
            discount = new BigDecimal(currentLevel.getDiscount());//所享折扣
        }
        BigDecimal goodsTotalPrice;

        // 是直接购买的
        if (type.equals("buy")) {
            BuyGoodsVo goodsVO = (BuyGoodsVo) jedisUtil.getObject(Constant.MTM_CACHE + "goods" + loginUser.getId());

            MallGoodsSkuEntity productInfo = goodsSkuService.getById(goodsVO.getSkuId());
            if (null == productInfo || productInfo.getGoodsNumber() < goodsVO.getNumber() || productInfo.getGoodsNumber() <= 0) {
                throw new BusinessException("商品库存不足");
            }
            productInfo.setGoodsNumber(productInfo.getGoodsNumber() - goodsVO.getNumber());
            goodsSkuService.update(productInfo);
            //计算订单的费用
            //商品总价
            goodsTotalPrice = productInfo.getRetailPrice().multiply(new BigDecimal(goodsVO.getNumber()));
            MallGoodsEntity entity = goodsService.getById(goodsVO.getGoodsId());
            entity.setGoodsNumber(entity.getGoodsNumber() - goodsVO.getNumber());
            if (null == entity.getGoodsNumber() || entity.getGoodsNumber() < goodsVO.getNumber() || entity.getGoodsNumber() <= 0) {
                throw new BusinessException("商品库存不足");
            }
            goodsService.updateById(entity);

            MallCartEntity cartVo = new MallCartEntity();
            cartVo.setGoodsName(entity.getName());
            cartVo.setNumber(goodsVO.getNumber());
            cartVo.setGoodsId(goodsVO.getGoodsId());
            cartVo.setSkuId(goodsVO.getSkuId());
            cartVo.setRetailPrice(productInfo.getRetailPrice());
            cartVo.setListPicUrl(entity.getListPicUrl());
            cartVo.setGoodsSn(entity.getGoodsSn());
            cartVo.setGoodsSpecifitionNameValue(goodsVO.getSelectedText());
            checkedGoodsList.add(cartVo);
        } else {
            //获取要购买的商品
            Map<String, Object> param = new HashMap<>(4);
            param.put("userId", loginUser.getId());
            param.put("checked", 1);
            if (StringUtils.isNotBlank(shopsId)) {
                param.put("shopsId", shopsId);
            } else {
                param.put("normal", "0");
            }
            checkedGoodsList = cartService.queryAll(param);
            if (null == checkedGoodsList || checkedGoodsList.size() == 0) {
                return RestResponse.error("请选择商品！");
            }

            goodsTotalPrice = new BigDecimal(0.00);
            for (MallCartEntity cartItem : checkedGoodsList) {
                //减商品库存
                MallGoodsEntity goodsVo = goodsService.getById(cartItem.getGoodsId());
                if (null == goodsVo.getGoodsNumber() || goodsVo.getGoodsNumber() < cartItem.getNumber() || goodsVo.getGoodsNumber() <= 0) {
                    throw new BusinessException(cartItem.getGoodsName() + "库存不足");
                }
                goodsVo.setGoodsNumber(goodsVo.getGoodsNumber() - cartItem.getNumber());
                goodsVo.setSales(goodsVo.getSales() + cartItem.getNumber());
                goodsService.updateById(goodsVo);
                //非店铺订单减产品库存
                if (StringUtils.isNullOrEmpty(shopsId)) {
                    //减产品库存
                    MallGoodsSkuEntity goodsSkuEntity = goodsSkuService.getById(cartItem.getSkuId());
                    if (null == goodsSkuEntity || goodsSkuEntity.getGoodsNumber() < cartItem.getNumber() || goodsSkuEntity.getGoodsNumber() <= 0) {
                        throw new BusinessException(cartItem.getGoodsName() + "库存不足");
                    }
                    goodsSkuEntity.setGoodsNumber(goodsSkuEntity.getGoodsNumber() - cartItem.getNumber());
                    goodsSkuService.update(goodsSkuEntity);
                } else {
                    //店铺订单减库存
                    MallShopsGoodsEntity shopsGoodsEntity = shopsGoodsService.getOne(new LambdaQueryWrapper<MallShopsGoodsEntity>()
                                    .eq(MallShopsGoodsEntity::getGoodsId, goodsVo.getId())
                                    .eq(MallShopsGoodsEntity::getShopsId, shopsId)
                            , false);
                    if (null == shopsGoodsEntity.getGoodsNumber() || shopsGoodsEntity.getGoodsNumber() < cartItem.getNumber() || shopsGoodsEntity.getGoodsNumber() <= 0) {
                        throw new BusinessException(cartItem.getGoodsName() + "库存不足");
                    }
                    shopsGoodsEntity.setGoodsNumber(shopsGoodsEntity.getGoodsNumber() - cartItem.getNumber());

                    shopsGoodsService.update(shopsGoodsEntity);
                }

                //统计商品总价
                goodsTotalPrice = goodsTotalPrice.add(cartItem.getRetailPrice().multiply(new BigDecimal(cartItem.getNumber())));
            }
        }

        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //非店铺订单加上运费
        if (StringUtils.isNullOrEmpty(shopsId)) {
            //免邮门槛
            String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
            if (goodsTotalPrice.compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
                shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
            }
        }
        String orderSn = StringUtils.transFromType(fromType) + StringUtils.generateOrderNumber();

        //优惠金额
        BigDecimal subPrice = new BigDecimal(0);
        if (StringUtils.isNotBlank(couponId)) {
            MallCouponEntity couponEntity = couponService.getById(couponId);
            if (null != couponEntity && couponEntity.getStatus() == 1) {
                MallUserCouponEntity userCouponEntity = userCouponService.getOne(new QueryWrapper<MallUserCouponEntity>().eq("COUPON_ID", couponId).eq("USER_ID", loginUser.getId()));
                if (userCouponEntity.getStatus() == 0) {
                    userCouponEntity.setStatus(1);
                    userCouponEntity.setUsedTime(new Date());
                    userCouponEntity.setOrderId(orderSn);
                    userCouponService.update(userCouponEntity);
                    // 商品总价 >= 最低消费金额
                    BigDecimal minPrice = couponEntity.getMinPrice();
                    if (goodsTotalPrice.compareTo(minPrice) > -1) {
                        Integer couponType = couponEntity.getCouponType();
                        //代金券类型
                        if (Constant.CouponType.DJQ.getValue().equals(couponType)) {
                            subPrice = couponEntity.getSubPrice();
                        }
                        //折扣
                        if (Constant.CouponType.ZK.getValue().equals(couponType)) {
                            //这里使用商品总价乘以（1-折扣/10），然后四舍五入保留两位小数
                            subPrice = new BigDecimal(1).subtract(couponEntity.getDiscount().divide(new BigDecimal(10))).multiply(goodsTotalPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                    }
                }
            }
        }

        //订单价格计算//订单的总价
        BigDecimal orderTotalPrice = goodsTotalPrice.add(shippingFee);
        BigDecimal actualPrice = orderTotalPrice.subtract(subPrice);
        System.out.println(orderTotalPrice);

        MallOrderEntity orderInfo = new MallOrderEntity();
        orderInfo.setId(orderSn);
        orderInfo.setOrderSn(orderSn);
        orderInfo.setUserId(loginUser.getId());
        orderInfo.setCouponId(couponId);
        orderInfo.setCouponPrice(subPrice);

        orderInfo.setOrderType(Constant.NORMAL_ORDER);
        if (StringUtils.isNotBlank(shopsId)) {
            //店铺订单
            orderInfo.setShopsId(shopsId);
            orderInfo.setOrderType(Constant.SHOPS_ORDER);
            orderInfo.setConsignee(loginUser.getNickname());
            orderInfo.setMobile(loginUser.getMobile());
        } else {
            MallAddressEntity addressVo = jsonParam.getObject("checkedAddress", MallAddressEntity.class);
            orderInfo.setConsignee(addressVo.getUserName());
            orderInfo.setMobile(addressVo.getMobile());
            orderInfo.setCountry(addressVo.getNationalCode());
            orderInfo.setProvince(addressVo.getProvinceName());
            orderInfo.setCity(addressVo.getCityName());
            orderInfo.setDistrict(addressVo.getCountyName());
            orderInfo.setAddress(addressVo.getDetailInfo());
            orderInfo.setPostalCode(addressVo.getPostalCode());
        }
        orderInfo.setFromType(fromType);
        //留言
        orderInfo.setPostscript(postscript);
        orderInfo.setAddTime(new Date());
        orderInfo.setExpireTime(DateUtils.addDateMinutes(new Date(), Integer.parseInt(sysConfigService.getValue(Constant.ORDER_EXPIRE, "30"))));
        //将订单实际支付价格进行用户等级进行打折
        if (actualPrice!=null){
            //计算用户折扣后的实际付款价格
            BigDecimal multiply = BigDecimalUtil.multiply(actualPrice, discount);
            orderInfo.setActualPrice(multiply);
        }
        orderInfo.setOrderPrice(orderTotalPrice);
        // 待付款
        orderInfo.setOrderStatus(Constant.OrderStatus.DFK.getValue());
        orderInfo.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
        orderInfo.setPayStatus(Constant.PayStatus.WFK.getValue());
        orderInfo.setShippingFee(shippingFee);
        orderInfo.setIntegralMoney(new BigDecimal(0));
        //插入订单信息和订单商品
        orderService.save(orderInfo);
        if (null == orderInfo.getId()) {
            return RestResponse.error("订单提交失败！");
        }

        List<String> mallCartIds = new ArrayList<>();//推广追踪用
        List<MallOrderGoodsEntity> orderGoodsData = new ArrayList<>();
        for (MallCartEntity goodsItem : checkedGoodsList) {
            mallCartIds.add(goodsItem.getId());
            MallOrderGoodsEntity orderGoodsVo = new MallOrderGoodsEntity();
            orderGoodsVo.setOrderId(orderInfo.getId());
            orderGoodsVo.setGoodsId(goodsItem.getGoodsId());
            orderGoodsVo.setGoodsSn(goodsItem.getGoodsSn());
            orderGoodsVo.setSkuId(goodsItem.getSkuId());
            orderGoodsVo.setGoodsName(goodsItem.getGoodsName());
            orderGoodsVo.setListPicUrl(goodsItem.getListPicUrl());
            orderGoodsVo.setMarketPrice(goodsItem.getMarketPrice());
            orderGoodsVo.setRetailPrice(goodsItem.getRetailPrice());
            orderGoodsVo.setNumber(goodsItem.getNumber());
            orderGoodsVo.setGoodsSpecifitionNameValue(goodsItem.getGoodsSpecifitionNameValue());
            orderGoodsData.add(orderGoodsVo);
        }
        orderGoodsService.saveBatch(orderGoodsData);

        // 添加推广追踪记录
        // 直接购买有goodsId
        // 购物车结算无goodsId，但可以通过购物车记录获取通过推广而购买的商品
        if (type.equals("buy")) {
            MallCartEntity mallCartEntity = checkedGoodsList.get(0);
            MallDistPromoEntity distPromoEntity = new MallDistPromoEntity();
            distPromoEntity.setOrderId(orderInfo.getId());
            distPromoEntity.setUserId(promoUserId);
            distPromoEntity.setGoodsId(mallCartEntity.getGoodsId());
            distPromoEntity.setStatus(Constant.PromoStatus.ORDER.getValue());
            distPromoEntity.setSkuId(mallCartEntity.getSkuId());
            mallDistPromoService.addPromoRecord(loginUser.getId(), distPromoEntity);
        } else {
            List<MallDistPromoEntity> entityList = mallDistPromoService.getPromoListByCartIds(mallCartIds);
            for (MallDistPromoEntity distPromoEntity : entityList) {
                // 查得出数据代表有过“将推广商品添加入购物车”行为
                distPromoEntity.setOrderId(orderInfo.getId());
                distPromoEntity.setStatus(Constant.PromoStatus.ORDER.getValue());
                mallDistPromoService.addPromoRecord(loginUser.getId(), distPromoEntity);
            }
        }
        // 是直接购买的
        if (!type.equals("buy")) {
            //清空购物车已选中的商品
            cartService.remove(new QueryWrapper<MallCartEntity>().eq("USER_ID", loginUser.getId()).eq("checked", 1));
        }

        orderInfo.setOrderGoodsEntityList(orderGoodsData);

        try {
            if (fromType.equals(Constant.FromType.MA.getValue())) {
                // 微信通知
                sendMsgService.notifyPaySuccess(orderInfo, 0);
            } else if (orderInfo.getFromType().equals(Constant.FromType.MP.getValue())) {
                //TODO 公众号下单，发送消息
            } else if (orderInfo.getFromType().equals(Constant.FromType.MA.getValue())) {
                //TODO 小程序下单，发送消息
            } else if (orderInfo.getFromType().equals(Constant.FromType.QQ.getValue())) {
                //TODO QQ小程序下单，发送消息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestResponse.success().put("data", orderInfo);
    }

    @PostMapping("confirmOrder")
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "orderId", name = "orderId", value = "JSON格式参数", required = true, dataType = "string")
    })
    public RestResponse confirmOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject params) {
        String orderId = params.getString("orderId");
        MallOrderEntity orderInfo = orderService.getById(orderId);

        if (!loginUser.getId().equals(orderInfo.getUserId())) {
            return RestResponse.error("越权操作！");
        }

        orderService.confirmReceive(orderId);

        return RestResponse.success("确认成功");
    }

    /**
     * 取消订单
     */
    @PostMapping("cancelOrder")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse cancelOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");

        MallOrderEntity orderVo = orderService.getById(orderId);
        if (!loginUser.getId().equals(orderVo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        if (orderVo.getOrderStatus().equals(Constant.OrderStatus.YFH.getValue())) {
            return RestResponse.error("已发货，不能取消！");
        } else if (orderVo.getOrderStatus().equals(Constant.OrderStatus.QRSH.getValue())) {
            return RestResponse.error("已收货，不能取消！");
        }
        return payService.refund(orderVo);
    }

    /**
     * 修改秒杀订单的收货地址
     */
    @PostMapping("updateOrderAddress")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateOrderAddress(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");

        MallOrderEntity orderVo = orderService.getById(orderId);
        if (!loginUser.getId().equals(orderVo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        String consignee = jsonParam.getString("consignee"),
                province = jsonParam.getString("province"),
                city = jsonParam.getString("city"),
                district = jsonParam.getString("district"),
                address = jsonParam.getString("address"),
                mobile = jsonParam.getString("mobile");
        orderVo.setConsignee(consignee);
        orderVo.setProvince(province);
        orderVo.setCity(city);
        orderVo.setDistrict(district);
        orderVo.setAddress(address);
        orderVo.setMobile(mobile);
        orderService.update(orderVo);

        return RestResponse.success();
    }
}
