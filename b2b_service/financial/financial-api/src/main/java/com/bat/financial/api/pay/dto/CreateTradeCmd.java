package com.bat.financial.api.pay.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 20:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "CreateTradeCmd", description = "创建交易")
public class CreateTradeCmd extends BaseTrade {

    @ApiModelProperty(value = "订单id xxxx,xxxx", example = "100000")
    private String orderId;

    @ApiModelProperty(value = "平台用户id 微信为openid jsapi 小程序必传", example = "o4GgauInH_RCEdvrrNGrntXDuXXX")
    private String platformUserId;

    @ApiModelProperty(value = "付款人Id (B2B 分销客户id C端 C端客户id)", required = true, example = "10086")
    private Integer payerId;

    @ApiModelProperty(value = "付款人名称 (B2B 分销客户名称 C端 C端客户名称)", example = "中国")
    private String payerName;

    /**
     * @ApiModelProperty(value = "支付场景描述 微信H5必传") 有默认值
     */
    private SceneInfo sceneInfo;
    /**
     * @ApiModelProperty(value = "结算信息 服务商") 有默认值
     */
    private SettleInfo settleInfo = new SettleInfo();

    /**
     * @ApiModelProperty(value = "标题", required = true, example = "销售订单支付") 有默认值
     */
    private String title;

    /**
     * @ApiModelProperty(value = "描述", required = true, example = "销售订单微信支付") 有默认值
     */
    private String description;
    /**
     * @ApiModelProperty(value = "金额 由订单服务确定(不传)", required = false, example = "0.01")
     */
    private BigDecimal amount;

    /**
     * H5支付 取消以及 完成支付后的重定向地址 以前端传的优先。没有的话 调转 https://www.bat.com/wxpay/notify
     */
    @ApiModelProperty(value = "H5支付 取消以及 完成支付后的重定向地址 以前端传的优先 ", example = "")
    private String redirectUrl;

    /**
     * H5支付 取消以及 完成支付后的重定向地址 以前端传的优先。没有的话 调转 https://www.bat.com/wxpay/notify
     */
    @ApiModelProperty(value = "阿里H5支付 取消后的重定向地址 以前端传的优先 ", example = "")
    private String quitUrl;

    /**
     * 商户号（服务商存的是子商户号）
     */
    private String mchid;

    /**
     * 服务商商户号（非最终收款）
     */
    private String spMchid;

}
