package com.bat.financial.api.pay.dto.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/24 16:52
 */
@NoArgsConstructor
@Data
public class QueryRefundDTO {

    WXQueryRefundDTO wxResp;

    AlipayQueryRefundDTO alipayResp;

    @NoArgsConstructor
    @Data
    public static class WXQueryRefundDTO {
        /**
         * amount :
         * {"currency":"CNY","discount_refund":0,"payer_refund":1,"payer_total":1,"refund":1,"settlement_refund":1,"settlement_total":1,"total":1}
         * channel : ORIGINAL create_time : 2018-03-26T14:47:45+08:00 funds_account : UNSETTLED out_refund_no :
         * sdkbat201878920180326144648 out_trade_no : sdkbat201878920180326144648 promotion_detail : [] refund_id :
         * 50000807732018032607482875770 status : SUCCESS success_time : 2018-03-26T14:47:49+08:00 transaction_id :
         * 4200000890201803265892063419 user_received_account : 支付用户零钱
         */

        @JsonProperty("amount")
        private AmountDTO amount;
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("create_time")
        private String createTime;
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
        @JsonProperty("success_time")
        private String successTime;
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
        }
    }

    @NoArgsConstructor
    @Data
    public static class AlipayQueryRefundDTO {
        /**
         * alipay_trade_fastpay_refund_query_response :
         * {"code":"10000","msg":"Success","trade_no":"2014112611001004680073956707","out_trade_no":"20150320010101001","out_request_no":"20150320010101001","refund_reason":"用户退款请求","total_amount":100.2,"refund_amount":12.33,"refund_royaltys":[{"refund_amount":10,"royalty_type":"transfer","result_code":"SUCCESS","trans_out":"2088102210397302","trans_out_email":"alipay-test03@alipay.com","trans_in":"2088102210397302","trans_in_email":"zen_gwen@hotmail.com"}],"gmt_refund_pay":"2014-11-27
         * 15:45:57","refund_detail_item_list":[{"fund_channel":"ALIPAYACCOUNT","amount":10,"real_amount":11.21,"fund_type":"DEBIT_CARD"}],"send_back_fee":"88","refund_settlement_id":"2018071610032004620239146945","present_refund_buyer_amount":"88.88","present_refund_discount_amount":"88.88","present_refund_mdiscount_amount":"88.88","deposit_back_info":{"has_deposit_back":"true","dback_status":"S","dback_amount":1.01,"bank_ack_time":"2018-06-02
         * 14:03:48","est_bank_receipt_time":"2018-06-02
         * 14:03:48"},"refund_channel_status":"SUCCESS","refund_channel_list":"OUTSIDEGOVCARD"} sign :
         * ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE
         */

        @JsonProperty("alipay_trade_fastpay_refund_query_response")
        private AlipayTradeFastpayRefundQueryResponseDTO alipayTradeFastpayRefundQueryResponse;
        @JsonProperty("sign")
        private String sign;

        @NoArgsConstructor
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AlipayTradeFastpayRefundQueryResponseDTO {
            /**
             * code : 10000 msg : Success trade_no : 2014112611001004680073956707 out_trade_no : 20150320010101001
             * out_request_no : 20150320010101001 refund_reason : 用户退款请求 total_amount : 100.2 refund_amount : 12.33
             * refund_royaltys :
             * [{"refund_amount":10,"royalty_type":"transfer","result_code":"SUCCESS","trans_out":"2088102210397302","trans_out_email":"alipay-test03@alipay.com","trans_in":"2088102210397302","trans_in_email":"zen_gwen@hotmail.com"}]
             * gmt_refund_pay : 2014-11-27 15:45:57 refund_detail_item_list :
             * [{"fund_channel":"ALIPAYACCOUNT","amount":10,"real_amount":11.21,"fund_type":"DEBIT_CARD"}] send_back_fee
             * : 88 refund_settlement_id : 2018071610032004620239146945 present_refund_buyer_amount : 88.88
             * present_refund_discount_amount : 88.88 present_refund_mdiscount_amount : 88.88 deposit_back_info :
             * {"has_deposit_back":"true","dback_status":"S","dback_amount":1.01,"bank_ack_time":"2018-06-02
             * 14:03:48","est_bank_receipt_time":"2018-06-02 14:03:48"} refund_channel_status : SUCCESS
             * refund_channel_list : OUTSIDEGOVCARD
             */

            @JsonProperty("code")
            private String code;
            @JsonProperty("msg")
            private String msg;
            @JsonProperty("trade_no")
            private String tradeNo;
            @JsonProperty("out_trade_no")
            private String outTradeNo;
            @JsonProperty("out_request_no")
            private String outRequestNo;
            @JsonProperty("refund_reason")
            private String refundReason;
            @JsonProperty("total_amount")
            private String totalAmount;
            @JsonProperty("refund_amount")
            private String refundAmount;
            @JsonProperty("refund_royaltys")
            private List<RefundRoyaltysDTO> refundRoyaltys;
            @JsonProperty("gmt_refund_pay")
            private String gmtRefundPay;
            @JsonProperty("refund_detail_item_list")
            private List<RefundDetailItemListDTO> refundDetailItemList;
            @JsonProperty("send_back_fee")
            private String sendBackFee;
            @JsonProperty("refund_settlement_id")
            private String refundSettlementId;
            @JsonProperty("present_refund_buyer_amount")
            private String presentRefundBuyerAmount;
            @JsonProperty("present_refund_discount_amount")
            private String presentRefundDiscountAmount;
            @JsonProperty("present_refund_mdiscount_amount")
            private String presentRefundMdiscountAmount;
            @JsonProperty("deposit_back_info")
            private DepositBackInfoDTO depositBackInfo;
            @JsonProperty("refund_channel_status")
            private String refundChannelStatus;
            @JsonProperty("refund_channel_list")
            private String refundChannelList;

            @NoArgsConstructor
            @Data
            public static class DepositBackInfoDTO {
                /**
                 * has_deposit_back : true dback_status : S dback_amount : 1.01 bank_ack_time : 2018-06-02 14:03:48
                 * est_bank_receipt_time : 2018-06-02 14:03:48
                 */

                @JsonProperty("has_deposit_back")
                private String hasDepositBack;
                @JsonProperty("dback_status")
                private String dbackStatus;
                @JsonProperty("dback_amount")
                private String dbackAmount;
                @JsonProperty("bank_ack_time")
                private String bankAckTime;
                @JsonProperty("est_bank_receipt_time")
                private String estBankReceiptTime;
            }

            @NoArgsConstructor
            @Data
            public static class RefundRoyaltysDTO {
                /**
                 * refund_amount : 10 royalty_type : transfer result_code : SUCCESS trans_out : 2088102210397302
                 * trans_out_email : alipay-test03@alipay.com trans_in : 2088102210397302 trans_in_email :
                 * zen_gwen@hotmail.com
                 */

                @JsonProperty("refund_amount")
                private String refundAmount;
                @JsonProperty("royalty_type")
                private String royaltyType;
                @JsonProperty("result_code")
                private String resultCode;
                @JsonProperty("trans_out")
                private String transOut;
                @JsonProperty("trans_out_email")
                private String transOutEmail;
                @JsonProperty("trans_in")
                private String transIn;
                @JsonProperty("trans_in_email")
                private String transInEmail;
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
