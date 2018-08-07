package com.bat.financial.pay.data.wxv3;

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
public class WxRefundNotifyCipherRespDTO {

    /**
     * mchid : 1900000100 transaction_id : 1008450740201411110005820873 out_trade_no : 20150806125346 refund_id :
     * 50200207182018070300011301001 out_refund_no : 7752501201407033233368018 refund_status : SUCCESS success_time :
     * 2018-06-08T10:34:56+08:00 user_received_account : 招商银行信用卡0403 amount :
     * {"total":999,"refund":999,"payer_total":999,"payer_refund":999}
     */

    @JsonProperty("mchid")
    private String mchid;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("refund_id")
    private String refundId;
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    @JsonProperty("refund_status")
    private String refundStatus;
    @JsonProperty("success_time")
    private String successTime;
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    @JsonProperty("amount")
    private AmountDTO amount;

    @NoArgsConstructor
    @Data
    public static class AmountDTO {
        /**
         * total : 999 refund : 999 payer_total : 999 payer_refund : 999
         */

        @JsonProperty("total")
        private Integer total;
        @JsonProperty("refund")
        private Integer refund;
        @JsonProperty("payer_total")
        private Integer payerTotal;
        @JsonProperty("payer_refund")
        private Integer payerRefund;
    }
}
