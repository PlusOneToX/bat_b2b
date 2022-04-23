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
package com.platform.modules.mall.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.platform.common.utils.DateUtils;
import com.platform.modules.mall.dao.MallOrderGoodsDao;
import com.platform.modules.mall.dao.MallTemplateConfDao;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.entity.MallTemplateConfEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.WxSendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一次性订阅消息Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-04 18:08:15
 */
@Slf4j
@Service("wxSendMsgService")
public class WxSendMsgServiceImpl implements WxSendMsgService {
    @Autowired
    private WxMaMsgService maMsgService;
    @Autowired
    private MallTemplateConfDao templateConfDao;
    @Autowired
    private MallUserDao userDao;
    @Autowired
    private MallOrderGoodsDao orderGoodsDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean notifyPaySuccess(MallOrderEntity orderInfo, int templateType) {
        log.info("----------微信消息通知----------");
        log.info("订单编号：" + orderInfo.getOrderSn() + "；templateType：" + templateType);
        WxMaSubscribeMessage paras = new WxMaSubscribeMessage();

        MallTemplateConfEntity templateConfVo = templateConfDao.selectOne(new QueryWrapper<MallTemplateConfEntity>().eq("TEMPLATE_TYPE", templateType));
        if (null == templateConfVo) {
            return false;
        }
        MallUserEntity userVo = userDao.selectById(orderInfo.getUserId());
        String page = "/pages/ucenter/orderDetail/orderDetail?id=";
        if (orderInfo.getOrderType() == 3) {
            page = "/pages/ucenter/skillDetail/skillDetail?id=";
        }
        paras.setTemplateId(templateConfVo.getTemplateId());
        paras.setToUser(userVo.getOpenId());
        paras.setPage(page + orderInfo.getId());

        //订单的商品
        Map<String, Object> orderGoodsParam = new HashMap<>(2);
        orderGoodsParam.put("orderId", orderInfo.getId());
        List<MallOrderGoodsEntity> orderGoods = orderGoodsDao.queryAll(orderGoodsParam);
        StringBuilder body = new StringBuilder();
        if (null != orderGoods) {
            for (MallOrderGoodsEntity goodsVo : orderGoods) {
                if (body.length() < 100) {
                    body.append(goodsVo.getGoodsName()).append("*").append(goodsVo.getNumber()).append("、");
                }
            }
            if (body.length() > 0) {
                body = new StringBuilder(body.substring(0, body.length() - 1));
            }
        }

        //-------------------- 根据templateType使用不同的模板 --------------------
        //新订单提醒
        if (templateType == 0) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string4", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("date6", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing2", body.toString()),
                    new WxMaSubscribeMessage.Data("amount3", orderInfo.getActualPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "元"),
                    new WxMaSubscribeMessage.Data("thing1", transOrderType(orderInfo.getOrderType()))
            ));
        }
        //下单成功通知
        if (templateType == 1) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string6", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("date4", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing2", body.toString()),
                    new WxMaSubscribeMessage.Data("amount3", orderInfo.getActualPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "元"),
                    new WxMaSubscribeMessage.Data("thing1", transOrderType(orderInfo.getOrderType()))
            ));
        }
        //订单评价提醒
        if (templateType == 2) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string1", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("time2", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing3", body.toString()),
                    new WxMaSubscribeMessage.Data("amount4", orderInfo.getActualPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "元"),
                    new WxMaSubscribeMessage.Data("thing5", transOrderType(orderInfo.getOrderType()))
            ));
        }
        //退款
        if (templateType == 3) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string4", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("date3", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing1", body.toString()),
                    new WxMaSubscribeMessage.Data("amount2", orderInfo.getActualPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "元"),
                    new WxMaSubscribeMessage.Data("thing8", transOrderType(orderInfo.getOrderType()))
            ));
        }
        //秒杀成功通知
        if (templateType == 4) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string1", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("time2", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing3", body.toString()),
                    new WxMaSubscribeMessage.Data("thing4", transOrderType(orderInfo.getOrderType()))
            ));
        }
        //订单配送通知
        if (templateType == 5) {
            paras.setData(Lists.newArrayList(
                    new WxMaSubscribeMessage.Data("character_string1", orderInfo.getOrderSn()),
                    new WxMaSubscribeMessage.Data("date3", DateUtils.format(orderInfo.getAddTime(), DateUtils.DATE_TIME_PATTERN)),
                    new WxMaSubscribeMessage.Data("thing8", body.toString()),
                    new WxMaSubscribeMessage.Data("name6", orderInfo.getShippingName()),
                    new WxMaSubscribeMessage.Data("character_string7", orderInfo.getShippingNo())
            ));
        }
        try {
            maMsgService.sendSubscribeMsg(paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private String transOrderType(int type) {
        switch (type) {
            case 1:
                return "商城订单";
            case 2:
                return "店铺自提订单";
            case 3:
                return "秒杀订单";
            case 4:
                return "积分订单";
            default:
                return "商城订单";
        }
    }
}
