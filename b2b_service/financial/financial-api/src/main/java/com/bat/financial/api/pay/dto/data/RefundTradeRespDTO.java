package com.bat.financial.api.pay.dto.data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/24 15:42
 */
@NoArgsConstructor
@Data
public class RefundTradeRespDTO {

    private Boolean refundStatus = false;

    private String outRefundNo;

    private String onlineTradeNo;

    private Date refundTime;

    WXRefundTradeRespDTO wxResp;

    AlipayRefundTradeRespDTO alipayResp;

    @NoArgsConstructor
    @Data
    public static class WXRefundTradeRespDTO {
        /**
         * amount :
         * {"currency":"CNY","discount_refund":0,"payer_refund":1,"payer_total":1,"refund":1,"settlement_refund":1,"settlement_total":1,"total":1}
         * channel : ORIGINAL create_time : 2018-03-26T14:47:45+08:00 funds_account : UNSETTLED out_refund_no :
         * sdkbat201878920180326144648 out_trade_no : sdkbat201878920180326144648 promotion_detail : [] refund_id :
         * 50000807732018032607482875770 status : PROCESSING transaction_id : 4200000890201803265892063419
         * user_received_account : 支付用户零钱
         */

        @JsonProperty("amount")
        private AmountDTO amount;
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("create_time")
        private String createTime;
        @JsonProperty("success_time")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String successTime;
        @JsonProperty("funds_account")
        private String fundsAccount;
        @JsonProperty("out_refund_no")
        private String outRefundNo;
        @JsonProperty("out_trade_no")
        private String outTradeNo;
        @JsonProperty("promotion_detail")
        private List<?> promotionDetail;
        @JsonProperty("refund_id")
        private String refundId;
        @JsonProperty("status")
        private String status;
        @JsonProperty("transaction_id")
        private String transactionId;
        @JsonProperty("user_received_account")
        private String userReceivedAccount;

        @NoArgsConstructor
        @Data
        public static class AmountDTO {
            /**
             * currency : CNY discount_refund : 0 payer_refund : 1 payer_total : 1 refund : 1 settlement_refund : 1
             * settlement_total : 1 total : 1
             */

            @JsonProperty("currency")
            private String currency;
            @JsonProperty("discount_refund")
            private Integer discountRefund;
            @JsonProperty("payer_refund")
            private Integer payerRefund;
            @JsonProperty("payer_total")
            private Integer payerTotal;
            @JsonProperty("refund")
            private Integer refund;
            @JsonProperty("settlement_refund")
            private Integer settlementRefund;
            @JsonProperty("settlement_total")
            private Integer settlementTotal;
            @JsonProperty("total")
            private Integer total;
            @JsonProperty("from")
            private List<AmountDTO.FromDTO> from;

            @NoArgsConstructor
            @Data
            public static class FromDTO {
                /**
                 * account : AVAILABLE amount : 444
                 */

                @JsonProperty("account")
                private String account;
                @JsonProperty("amount")
                private Integer amount;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class AlipayRefundTradeRespDTO {

        /**
         * 自定义增加参数 用于退款查询
         */
        @JsonProperty("out_request_no")
        @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
        private String outRequestNo;

        /**
         * alipay_trade_refund_response :
         * {"code":"10000","msg":"Success","trade_no":"支付宝交易号","out_trade_no":"6823789339978248","buyer_logon_id":"159****5620","fund_change":"Y","refund_fee":88.88,"refund_currency":"USD","refund_detail_item_list":[{"fund_channel":"ALIPAYACCOUNT","amount":10,"real_amount":11.21,"fund_type":"DEBIT_CARD"}],"store_name":"望湘园联洋店","buyer_user_id":"2088101117955611","send_back_fee":"1.8","refund_preset_paytool_list":{"amount":[12.21],"assert_type_code":"盒马礼品卡:HEMA；抓猫猫红包:T_CAT_COUPON"},"refund_settlement_id":"2018071610032004620239146945","present_refund_buyer_amount":"88.88","present_refund_discount_amount":"88.88","present_refund_mdiscount_amount":"88.88","has_deposit_back":"true"}
         * sign : ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE
         */

        @JsonProperty("alipay_trade_refund_response")
        private AlipayTradeRefundResponseDTO alipayTradeRefundResponse;
        @JsonProperty("sign")
        private String sign;

        @NoArgsConstructor
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AlipayTradeRefundResponseDTO {
            /**
             * code : 10000 msg : Success trade_no : 支付宝交易号 out_trade_no : 6823789339978248 buyer_logon_id : 159****5620
             * fund_change : Y refund_fee : 88.88 refund_currency : USD refund_detail_item_list :
             * [{"fund_channel":"ALIPAYACCOUNT","amount":10,"real_amount":11.21,"fund_type":"DEBIT_CARD"}] store_name :
             * 望湘园联洋店 buyer_user_id : 2088101117955611 send_back_fee : 1.8 refund_preset_paytool_list :
             * {"amount":[12.21],"assert_type_code":"盒马礼品卡:HEMA；抓猫猫红包:T_CAT_COUPON"} refund_settlement_id :
             * 2018071610032004620239146945 present_refund_buyer_amount : 88.88 present_refund_discount_amount : 88.88
             * present_refund_mdiscount_amount : 88.88 has_deposit_back : true
             */

            /**
             * 文档中没有 实际有的参数
             */
            @JsonProperty("gmt_refund_pay")
            private String gmtRefundPay;

            @JsonProperty("code")
            private String code;
            @JsonProperty("msg")
            private String msg;
            @JsonProperty("trade_no")
            private String tradeNo;
            @JsonProperty("out_trade_no")
            private String outTradeNo;
            @JsonProperty("buyer_logon_id")
            private String buyerLogonId;
            @JsonProperty("fund_change")
            private String fundChange;
            @JsonProperty("refund_fee")
            private Double refundFee;
            @JsonProperty("refund_currency")
            private String refundCurrency;
            @JsonProperty("refund_detail_item_list")
            private List<RefundDetailItemListDTO> refundDetailItemList;
            @JsonProperty("store_name")
            private String storeName;
            @JsonProperty("buyer_user_id")
            private String buyerUserId;
            @JsonProperty("send_back_fee")
            private String sendBackFee;
            @JsonProperty("refund_preset_paytool_list")
            private RefundPresetPaytoolListDTO refundPresetPaytoolList;
            @JsonProperty("refund_settlement_id")
            private String refundSettlementId;
            @JsonProperty("present_refund_buyer_amount")
            private String presentRefundBuyerAmount;
            @JsonProperty("present_refund_discount_amount")
            private String presentRefundDiscountAmount;
            @JsonProperty("present_refund_mdiscount_amount")
            private String presentRefundMdiscountAmount;
            @JsonProperty("has_deposit_back")
            private String hasDepositBack;

            @NoArgsConstructor
            @Data
            public static class RefundPresetPaytoolListDTO {
                /**
                 * amount : [12.21] assert_type_code : 盒马礼品卡:HEMA；抓猫猫红包:T_CAT_COUPON
                 */

                @JsonProperty("amount")
                private List<String> amount;
                @JsonProperty("assert_type_code")
                private String assertTypeCode;
            }

            @NoArgsConstructor
            @Data
            public static class RefundDetailItemListDTO {
                /**
                 * fund_channel : ALIPAYACCOUNT amount : 10 real_amount : 11.21 fund_type : DEBIT_CARD
                 */

                @JsonProperty("fund_channel")
                private String fundChannel;
                @JsonProperty("amount")
                private String amount;
                @JsonProperty("real_amount")
                private String realAmount;
                @JsonProperty("fund_type")
                private String fundType;
            }
        }
    }

}
