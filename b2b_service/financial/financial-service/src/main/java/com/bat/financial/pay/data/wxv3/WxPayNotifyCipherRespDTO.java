package com.bat.financial.pay.data.wxv3;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/25 17:40
 */
@NoArgsConstructor
@Data
public class WxPayNotifyCipherRespDTO {

    /**
     * transaction_id : 1217752501201407033233368018 amount :
     * {"payer_total":100,"total":100,"currency":"CNY","payer_currency":"CNY"} mchid : 1230000109 trade_state : SUCCESS
     * bank_type : CMC promotion_detail :
     * [{"amount":100,"wechatpay_contribute":0,"coupon_id":"109519","scope":"GLOBAL","merchant_contribute":0,"name":"单品惠-6","other_contribute":0,"currency":"CNY","stock_id":"931386","goods_detail":[{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100},{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100}]},{"amount":100,"wechatpay_contribute":0,"coupon_id":"109519","scope":"GLOBAL","merchant_contribute":0,"name":"单品惠-6","other_contribute":0,"currency":"CNY","stock_id":"931386","goods_detail":[{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100},{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100}]}]
     * success_time : 2018-06-08T10:34:56+08:00 payer : {"openid":"oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"} out_trade_no :
     * 1217752501201407033233368018 appid : wxd678efh567hg6787 trade_state_desc : 支付成功 trade_type : MICROPAY attach :
     * 自定义数据 scene_info : {"device_id":"013467007045764"}
     */

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("amount")
    private AmountDTO amount;
    @JsonProperty("mchid")
    private String mchid;
    @JsonProperty("trade_state")
    private String tradeState;
    @JsonProperty("bank_type")
    private String bankType;
    @JsonProperty("promotion_detail")
    private List<PromotionDetailDTO> promotionDetail;
    @JsonProperty("success_time")
    private String successTime;
    @JsonProperty("payer")
    private PayerDTO payer;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("appid")
    private String appid;
    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("attach")
    private String attach;
    @JsonProperty("scene_info")
    private SceneInfoDTO sceneInfo;

    @NoArgsConstructor
    @Data
    public static class AmountDTO {
        /**
         * payer_total : 100 total : 100 currency : CNY payer_currency : CNY
         */

        @JsonProperty("payer_total")
        private Integer payerTotal;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("payer_currency")
        private String payerCurrency;
    }

    @NoArgsConstructor
    @Data
    public static class PayerDTO {
        /**
         * openid : oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
         */

        @JsonProperty("openid")
        private String openid;
    }

    @NoArgsConstructor
    @Data
    public static class SceneInfoDTO {
        /**
         * device_id : 013467007045764
         */

        @JsonProperty("device_id")
        private String deviceId;
    }

    @NoArgsConstructor
    @Data
    public static class PromotionDetailDTO {
        /**
         * amount : 100 wechatpay_contribute : 0 coupon_id : 109519 scope : GLOBAL merchant_contribute : 0 name : 单品惠-6
         * other_contribute : 0 currency : CNY stock_id : 931386 goods_detail :
         * [{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100},{"goods_remark":"商品备注信息","quantity":1,"discount_amount":1,"goods_id":"M1006","unit_price":100}]
         */

        @JsonProperty("amount")
        private Integer amount;
        @JsonProperty("wechatpay_contribute")
        private Integer wechatpayContribute;
        @JsonProperty("coupon_id")
        private String couponId;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("merchant_contribute")
        private Integer merchantContribute;
        @JsonProperty("name")
        private String name;
        @JsonProperty("other_contribute")
        private Integer otherContribute;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("stock_id")
        private String stockId;
        @JsonProperty("goods_detail")
        private List<GoodsDetailDTO> goodsDetail;

        @NoArgsConstructor
        @Data
        public static class GoodsDetailDTO {
            /**
             * goods_remark : 商品备注信息 quantity : 1 discount_amount : 1 goods_id : M1006 unit_price : 100
             */

            @JsonProperty("goods_remark")
            private String goodsRemark;
            @JsonProperty("quantity")
            private Integer quantity;
            @JsonProperty("discount_amount")
            private Integer discountAmount;
            @JsonProperty("goods_id")
            private String goodsId;
            @JsonProperty("unit_price")
            private Integer unitPrice;
        }
    }
}
