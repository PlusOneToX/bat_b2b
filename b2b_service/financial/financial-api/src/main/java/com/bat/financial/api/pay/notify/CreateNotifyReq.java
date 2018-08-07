package com.bat.financial.api.pay.notify;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/25 16:38
 */
@NoArgsConstructor
@Data
@ToString
public class CreateNotifyReq {
    /**
     * 收款类型 平台收款 分销商自己收款
     */
    private Short tradeMode;
    /**
     * 微信支付才会有这个参数
     * 
     * 应用类型 1、微信公众号 2、微信小程序
     */
    private Short appType;
    /**
     * C端标志
     */
    private Short customerFlag;
    /**
     * 组织id 平台收款必传
     */
    private Integer organizationId;
    /**
     * 收款人id 分销商自己收款 必传
     */
    private Integer payeeId;

    /**
     * 微信V2版 api 返回xml参数
     */
    String wxReqV2;

    WXCreateNotifyV3Req wxReqV3;

    AlipayCreateNotifyReq alipayReq;

    KuaiQianCreateNotifyReq kuaiQianReq;

    @NoArgsConstructor
    @Data
    public static class WXCreateNotifyV3Req {
        /**
         * id : EV-2018022511223320873 create_time : 2015-05-20T13:29:35+08:00 resource_type : encrypt-resource
         * event_type : TRANSACTION.SUCCESS resource :
         * {"algorithm":"AEAD_AES_256_GCM","ciphertext":"...","nonce":"...","original_type":"transaction","associated_data":""}
         * summary : 支付成功
         */

        @JsonProperty("id")
        private String id;
        @JsonProperty("create_time")
        private String createTime;
        @JsonProperty("resource_type")
        private String resourceType;
        @JsonProperty("event_type")
        private String eventType;
        @JsonProperty("resource")
        private ResourceDTO resource;
        @JsonProperty("summary")
        private String summary;

        @NoArgsConstructor
        @Data
        public static class ResourceDTO {
            /**
             * algorithm : AEAD_AES_256_GCM ciphertext : ... nonce : ... original_type : transaction associated_data :
             */

            @JsonProperty("algorithm")
            private String algorithm;
            @JsonProperty("ciphertext")
            private String ciphertext;
            @JsonProperty("nonce")
            private String nonce;
            @JsonProperty("original_type")
            private String originalType;
            @JsonProperty("associated_data")
            private String associatedData;
        }
    }

    @NoArgsConstructor
    @Data
    public static class AlipayCreateNotifyReq {

        Map<String, String> signMap = new HashMap<>();

        /**
         * gmt_create : 2018-05-26 16:44:51 charset : UTF-8 seller_email :  subject : 测试 sign :
         * bPE4Au1DZPkShcoT3O64zaLIdbjI11tIkBwqOY5mauqB21glBPOrpV+Ci4pSJokBynJ274ajeDLDzA+THEWSt+WgSBsz413Uuu8t5L9NMfCW/lPh7+yt/+qnfGlh6E3Uyy5bQreeJAm2Pr21htzrhqRKDUFix0G/If9RMU1m5wW8uHEu2ISkVefBFmGlPRw0uy32XRFBl01uB101E7IDF/SVXaDDi+c9HoUlxc5SPr14mENDPxZ2utHZTQGGo9LH6cMppHB22YaHmxtNKNTV7qrwhW76+nMyCnE/FNsdhjcI2v/oMR1LgAooU9xHVDPeAjCAarptuRpv6e0Xk6aNqA==
         * buyer_id : 2088302179713571 invoice_amount : 0.01 notify_id : 2018052600222164455013571402360929
         * fund_bill_list : [{"amount":"0.01","fundChannel":"PCREDIT"}] notify_type : trade_status_sync trade_status :
         * TRADE_SUCCESS receipt_amount : 0.01 app_id : 2018022162645163 buyer_pay_amount : 0.01 sign_type : RSA2
         * seller_id : 2088721995363115 gmt_payment : 2018-05-26 16:44:55 notify_time : 2018-05-26 16:58:39 version :
         * 1.0 out_trade_no : alce1397473713748951040 total_amount : 0.01 trade_no : 2018052622001413571403954886
         * auth_app_id : 2018022162645163 buyer_logon_id : 189****2083 point_amount : 0.00
         */

        @JsonProperty("gmt_create")
        private String gmtCreate;
        @JsonProperty("charset")
        private String charset;
        @JsonProperty("seller_email")
        private String sellerEmail;
        @JsonProperty("subject")
        private String subject;
        @JsonProperty("sign")
        private String sign;
        @JsonProperty("buyer_id")
        private String buyerId;
        @JsonProperty("invoice_amount")
        private String invoiceAmount;
        @JsonProperty("notify_id")
        private String notifyId;
        @JsonProperty("fund_bill_list")
        private String fundBillList;
        @JsonProperty("notify_type")
        private String notifyType;
        @JsonProperty("trade_status")
        private String tradeStatus;
        /**
         * 实收金额。商户在交易中实际收到的款项，单位为人民币（元）。支持小数点后两位。
         */
        @JsonProperty("receipt_amount")
        private String receiptAmount;
        @JsonProperty("app_id")
        private String appId;
        @JsonProperty("buyer_pay_amount")
        private String buyerPayAmount;
        @JsonProperty("sign_type")
        private String signType;
        @JsonProperty("seller_id")
        private String sellerId;
        @JsonProperty("gmt_payment")
        private String gmtPayment;
        @JsonProperty("notify_time")
        private String notifyTime;
        @JsonProperty("version")
        private String version;
        @JsonProperty("out_trade_no")
        private String outTradeNo;
        /**
         * 订单金额。本次交易支付的订单金额，单位为人民币（元）。支持小数点后两位
         */
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("trade_no")
        private String tradeNo;
        @JsonProperty("auth_app_id")
        private String authAppId;
        @JsonProperty("buyer_logon_id")
        private String buyerLogonId;
        @JsonProperty("point_amount")
        private String pointAmount;

        /**
         * https://opendocs.alipay.com/open/194/103296/
         */
        // "[{\"amount\":\"9.50\",\"merchantContribute\":\"9.50\",\"name\":\"5折折扣券\",\"otherContribute\":\"0.00\",\"type\":\"ALIPAY_BIZ_VOUCHER\",\"voucherId\":\"201804060007300257130AK8JB7X\"}]"
        // "[{\"amount\":\"0.10\",\"merchantContribute\":\"0.00\",\"name\":\"消费金红包\",\"otherContribute\":\"0.10\",\"type\":\"ALIPAY_CASH_VOUCHER\",\"voucherId\":\"201803050007300223880BHKH3KJ\"}]",
        @JsonProperty("voucher_detail_list")
        private String voucherDetailList;
    }

    @NoArgsConstructor
    @Data
    public static class KuaiQianCreateNotifyReq {
        /**
         * payResult : 10 merchantAcctId : 1021024484701 orderId : 2018052009410301 dealId : 3411469393 fee : 1 language
         * : 1 version : v2.0 bankDealId : WG82301201805200944297620305 bankId : CCB payType : 10 orderAmount : 1
         * orderTime : 20180520094107 payAmount : 1 dealTime : 20180520095014 errCode : signType : 4 signMsg :
         * FbBOrXH6bBBd9Y6aytUeQMpi5j8b7FVO/GSgMQ56MJguZKvLCZULIv6fGAHun5VgGlAZ+g8tTI/niyZLVP8oK7rRKCo7nXc1lztC5+n5BIh67jT1Gn3PzXYQLxyy3gh0MsJ5fEs1BRc/Z/D9XW9r7Lmvlpusoeuy6pRgbEZI/0WnIebzPw9wGDTQwTsYpcPIqfPd15DB3VsLB86DVtJhlfaCoG0LVfFelAMTP1d3OR5Hm40p9W8XN7yvlpUm/ZsHjeyb4JjpCJ9/AFXMou3TYrltnjbaQa+zlkD4SWNXQD4go+gqWj+FXeJrwqf8k5uk3C3SXSx31jj6bqgXRIxRQg==
         * ext2 : ext1 :
         */

        @JsonProperty("payResult")
        private String payResult;
        @JsonProperty("merchantAcctId")
        private String merchantAcctId;
        @JsonProperty("orderId")
        private String orderId;
        @JsonProperty("dealId")
        private String dealId;
        @JsonProperty("fee")
        private String fee;
        @JsonProperty("language")
        private String language;
        @JsonProperty("version")
        private String version;
        @JsonProperty("bankDealId")
        private String bankDealId;
        @JsonProperty("bankId")
        private String bankId;
        @JsonProperty("payType")
        private String payType;
        @JsonProperty("orderAmount")
        private String orderAmount;
        @JsonProperty("orderTime")
        private String orderTime;
        @JsonProperty("payAmount")
        private String payAmount;
        @JsonProperty("dealTime")
        private String dealTime;
        @JsonProperty("errCode")
        private String errCode;
        @JsonProperty("signType")
        private String signType;
        @JsonProperty("signMsg")
        private String signMsg;
        @JsonProperty("ext2")
        private String ext2;
        @JsonProperty("ext1")
        private String ext1;
    }

}
