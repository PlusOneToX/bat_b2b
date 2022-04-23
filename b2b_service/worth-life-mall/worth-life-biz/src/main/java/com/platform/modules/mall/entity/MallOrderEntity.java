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
package com.platform.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单表实体
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@Data
@TableName("MALL_ORDER")
public class MallOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序
     */
    private Integer fromType;
    /**
     * 订单类型 1：商城订单 2：店铺自提订单 3：秒杀订单 4：积分订单
     */
    private Integer orderType;
    /**
     * 订单编号
     * 生成规则：XG（如果客户下单后管理员在后台修改价格）+ 用户下单来源类型 + 时间戳（年月日时分秒毫秒）
     */
    private String orderSn;
    /**
     * 会员ID
     */
    private String userId;
    /**
     * 订单状态 -1:秒杀成功(这里只是秒杀成功，还需要用户下单补充订单信息) 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 401:没发货，退款　402:已收货，退款退货
     */
    private Integer orderStatus;
    /**
     * 发货状态 商品配送情况;1:未发货 2:已发货 3:已收货 4:退货
     */
    private Integer shippingStatus;
    /**
     * 付款状态 1:未付款 2:付款中 3:已付款 4:退款
     */
    private Integer payStatus;
    /**
     * 支付方式 1：微信支付  2：余额支付  3：积分支付
     */
    private Integer payType;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 地区
     */
    private String district;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 邮编
     */
    private String postalCode;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 留言
     */
    private String postscript;
    /**
     * 快递公司ID
     */
    private String shippingId;
    /**
     * 快递公司名称
     */
    private String shippingName;
    /**
     * 快递公司CODE
     */
    private String shippingCode;
    /**
     * 快递单号
     */
    private String shippingNo;
    /**
     * 快递费用
     */
    private BigDecimal shippingFee;
    /**
     * 支付PREPAY_ID
     */
    private String prepayId;
    /**
     * 实际支付的金额
     */
    private BigDecimal actualPrice;
    /**
     * 积分抵扣金额
     */
    private BigDecimal integralMoney;
    /**
     * 订单总价
     */
    private BigDecimal orderPrice;
    /**
     * 下单时间
     */
    private Date addTime;
    /**
     * 发货时间
     */
    private Date confirmTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 使用的优惠券ID
     */
    private String couponId;
    /**
     * 优惠价格
     */
    private BigDecimal couponPrice;
    /**
     * 父级订单ID
     */
    private String parentId;
    /**
     * 店铺ID
     */
    private String shopsId;
    /**
     * 秒杀商品
     */
    private String goodsId;
    /**
     * 订单过期时间
     */
    private Date expireTime;
    /**
     * 使用的优惠券title
     */
    @TableField(exist = false)
    private String couponTitle;
    /**
     * 店铺
     */
    @TableField(exist = false)
    private String shopsSn;
    /**
     * 店铺
     */
    @TableField(exist = false)
    private String shopsName;
    /**
     * 秒杀商品
     */
    @TableField(exist = false)
    private String goodsName;
    /**
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 详情商品
     */
    @TableField(exist = false)
    private List<MallOrderGoodsEntity> orderGoodsEntityList;
    /**
     * 商品总数
     */
    @TableField(exist = false)
    private Integer goodsCount;
    /**
     * 评价总数
     */
    @TableField(exist = false)
    private Integer commentCount;
    /**
     * 订单状态的处理
     */
    @TableField(exist = false)
    private String orderStatusText;
    /**
     * 可操作的选项
     */
    @TableField(exist = false)
    private Map<String, Object> handleOption;

    public String getOrderStatusText() {
        if (null != orderStatus && StringUtils.isEmpty(orderStatusText)) {
            orderStatusText = "未付款";
            switch (orderStatus) {
                case 0:
                    orderStatusText = "未付款";
                    break;
                case 201:
                    orderStatusText = "等待发货";
                    break;
                case 300:
                    orderStatusText = "待收货";
                    break;
                case 301:
                    orderStatusText = "已完成";
                    break;
                case 200:
                    orderStatusText = "已付款";
                    break;
                case 100:
                    orderStatusText = "已过期";
                    break;
                case 101:
                    orderStatusText = "已取消";
                    break;
                case 401:
                    orderStatusText = "已取消";
                    break;
                case 402:
                    orderStatusText = "已退货";
                    break;
                default:
            }
        }
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public Map<String, Object> getHandleOption() {
        handleOption = new HashMap<>(16);
        //取消操作
        handleOption.put("cancel", false);
        //删除操作
        handleOption.put("delete", false);
        //支付操作
        handleOption.put("pay", false);
        //评论操作
        handleOption.put("comment", false);
        //确认收货操作
        handleOption.put("delivery", false);
        //完成订单操作
        handleOption.put("confirm", false);
        //退换货操作
        handleOption.put("return", false);
        //再次购买
        handleOption.put("buy", false);

        //订单流程：　下单成功－》支付订单－》发货－》收货－》评论
        //订单相关状态字段设计，采用单个字段表示全部的订单状态
        //1xx 表示订单取消和删除等状态 0订单创建成功等待付款，　101订单已取消，　102订单已删除
        //2xx 表示订单支付状态　201订单已付款，等待发货
        //3xx 表示订单物流相关状态　300订单已发货， 301用户确认收货
        //4xx 表示订单退换货相关的状态　401 没有发货，退款　402 已收货，退款退货

        //如果订单已经取消，则可删除和再次购买
        if (orderStatus == 101 || orderStatus == 100) {
            handleOption.put("delete", true);
            handleOption.put("buy", true);
        }

        //如果订单没有被取消，且没有支付，则可支付，可取消
        if (orderStatus == 0) {
            handleOption.put("cancel", true);
            handleOption.put("pay", true);
        }

        //如果订单已付款，没有发货，则可退款操作
        if (orderStatus == 201) {
            handleOption.put("cancel", true);
        }

        //如果订单已经发货，没有收货，则可收货操作
        if (orderStatus == 300) {
            handleOption.put("confirm", true);
        }

        //如果订单已经支付，且已经收货，则可完成交易、评论和再次购买
        if (orderStatus == 301) {
            handleOption.put("comment", true);
            handleOption.put("buy", true);
        }
        return handleOption;
    }

    public void setHandleOption(Map<String, Object> handleOption) {
        this.handleOption = handleOption;
    }

}
