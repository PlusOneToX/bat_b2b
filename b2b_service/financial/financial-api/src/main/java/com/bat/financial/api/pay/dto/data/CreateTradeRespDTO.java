package com.bat.financial.api.pay.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 13:34
 */
@Data
@NoArgsConstructor
@ApiModel
public class CreateTradeRespDTO {

    /**
     * 微信创建返回封装
     */
    @ApiModelProperty(value = "微信")
    WXCreateTradeRespDTO wxResp;

    /**
     * 支付宝创建返回封装
     */
    @ApiModelProperty(value = "阿里")
    AlipayCreateTradeRespDTO alipayResp;

    /**
     * 快钱创建返回封装
     */
    @ApiModelProperty(value = "快钱")
    KuaiQianCreateTradeRespDTO kuaiQianReap;

    @NoArgsConstructor
    @Data
    public static class WXCreateTradeRespDTO {

        // private String outTradeNo;
        @ApiModelProperty(value = "appid")
        private String appId;
        @ApiModelProperty(value = "时间戳")
        private String timeStamp;
        @ApiModelProperty(value = "随机串")
        private String nonceStr;
        // private String prepayId;
        @ApiModelProperty(value = "签名类型")
        private String signType;
        @ApiModelProperty(value = "签名")
        private String paySign;

        /**
         * 返回创建时的商户订单号，前端轮询支付结果时有用
         */
        @JsonProperty("out_trade_no")
        @ApiModelProperty(value = "商户订单号")
        private String outTradeNo;

        /**
         * native支付的二维码
         */
        @JsonProperty("code_url")
        @ApiModelProperty(value = "二维码地址")
        private String codeUrl;

        /**
         * jsapi app native 的预支付交易会话标识
         */
        @JsonProperty("prepay_id")
        @ApiModelProperty(value = "预交易号")
        private String prepayId;

        /**
         * h5支付的重定向地址
         */
        @JsonProperty("h5_url")
        @ApiModelProperty(value = "H5重定向地址")
        private String h5Url;

    }

    @Data
    @NoArgsConstructor
    public static class AlipayCreateTradeRespDTO {

        @JsonProperty("out_trade_no")
        @ApiModelProperty(value = "商户订单号")
        private String outTradeNo;

        @JsonProperty("trade_no")
        @ApiModelProperty(value = "支付宝交易号")
        private String tradeNo;

        /**
         * 面对面支付的二维码
         */
        @JsonProperty("code_url")
        @ApiModelProperty(value = "二维码地址")
        private String codeUrl;

        /**
         * 手机网页支付的表单 和 电脑网站支付表单
         */
        @JsonProperty("from")
        @ApiModelProperty(value = "form表单")
        private String from;

        /**
         * 手机App支付返回串
         */
        @JsonProperty("alipay_sdk")
        @ApiModelProperty(value = "alisdk")
        private String alipaySdk;
    }

    @Data
    @NoArgsConstructor
    public static class KuaiQianCreateTradeRespDTO {

        @JsonProperty("out_trade_no")
        @ApiModelProperty(value = "商户订单号")
        private String outTradeNo;

        /**
         * 人民币支付的重定向地址
         */
        @JsonProperty("h5_url")
        @ApiModelProperty(value = "H5重定向地址")
        private String h5Url;
    }
}
