package com.bat.financial.api.pay.dto.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 17:16
 */
@NoArgsConstructor
@Data
@ApiModel
public class QueryTradeRespDTO {

    /**
     * 交易结果
     */
    @ApiModelProperty(
        value = "交易状态SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（付款码支付） USERPAYING：用户支付中（付款码支付）PAYERROR：支付失败(其他原因，如银行返回失败) ACCEPT：已接收，等待扣款 ERROR 未知错误")

    private String tradeState;

    WXQueryTradeRespDTO wxResp;

    AlipayQueryTradeRespDTO alipayResp;

    KuaiQianQueryTradeRespDTO kuaiQianResp;

    @NoArgsConstructor
    @Data
    public static class WXQueryTradeRespDTO {
        /**
         * amount : {"currency":"CNY","payer_currency":"CNY","payer_total":1,"total":1} appid : wx65f4056d3c04f9df
         * attach : bank_type : OTHERS mchid : 1607647316 out_trade_no : bat20187 payer :
         * {"openid":"oK9Buv01xGmN_-CQtg5O4GAKeNko"} promotion_detail : [] success_time : 2018-05-22T16:42:21+08:00
         * trade_state : SUCCESS trade_state_desc : 支付成功 trade_type : NATIVE transaction_id :
         * 4200001011201805229872557981
         */

        @JsonProperty("amount")
        private AmountDTO amount;
        @JsonProperty("appid")
        private String appid;
        @JsonProperty("attach")
        private String attach;
        @JsonProperty("bank_type")
        private String bankType;
        @JsonProperty("mchid")
        private String mchid;
        @JsonProperty("out_trade_no")
        private String outTradeNo;
        @JsonProperty("promotion_detail")
        private List<?> promotionDetail;
        @JsonProperty("success_time")
        private String successTime;
        @JsonProperty("trade_state")
        private String tradeState;
        @JsonProperty("trade_state_desc")
        private String tradeStateDesc;
        @JsonProperty("trade_type")
        private String tradeType;
        @JsonProperty("transaction_id")
        private String transactionId;
        @JsonProperty("payer")
        @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
        private PayerDTO payer;
        @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
        @JsonProperty("scene_info")
        private SceneInfoDTO sceneInfo;

        @NoArgsConstructor
        @Data
        public static class AmountDTO {
            /**
             * currency : CNY payer_currency : CNY payer_total : 1 total : 1
             */

            @JsonProperty("currency")
            private String currency;
            @JsonProperty("payer_currency")
            private String payerCurrency;
            @JsonProperty("payer_total")
            private Integer payerTotal;
            @JsonProperty("total")
            private Integer total;
        }

        @NoArgsConstructor
        @Data
        public static class PayerDTO {
            /**
             * openid : oK9Buv01xGmN_-CQtg5O4GAKeNko
             */

            @JsonProperty("openid")
            private String openid;
        }

        @NoArgsConstructor
        @Data
        public static class SceneInfoDTO {

            @JsonProperty("payer_client_ip")
            private String payerClientIp;

            @JsonProperty("h5_info")
            @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
            private H5InfoDTO h5Info;

            @JsonProperty("device_id")
            @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
            private String deviceId;

            @NoArgsConstructor
            @Data
            public static class H5InfoDTO {
                @JsonProperty("type")
                private String type;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class AlipayQueryTradeRespDTO {
        /**
         * alipay_trade_query_response :
         * {"code":"10000","msg":"Success","buyer_logon_id":"189******83","buyer_pay_amount":"0.01","buyer_user_id":"2088302179713571","fund_bill_list":[{"amount":"0.01","fund_channel":"PCREDIT"}],"invoice_amount":"0.01","out_trade_no":"alce1396757191930478592","point_amount":"0.00","receipt_amount":"0.01","send_pay_date":"2018-05-24
         * 17:17:40","total_amount":"0.01","trade_no":"2018052422001413571403596182","trade_status":"TRADE_SUCCESS"}
         * sign :
         * qnI2s158a3pS1OC14DzbWbr7+py5QCqkBMyeGSgur5RLjDq/IwKufzZLrhNJBvi7YTgsnVyBDpb33WDmqJBVuebAmkeAi5zRZog9k6zcMKrw3yCyssJVbnuWjQ9B8oIwClGXb5Rq4F9ze9tFonICN1xSYeKg1DqM+D2j95XVlcQViPXpLwGK81HbfwUlxAnILY4fTbQvBYBvYS0+TUWHXmnET5gXp74o330lEV89QBabkBgzPoBw9zTa8zdXJ08bTRHzPbLjMgFQQ9uYsLQ3c91hPVLR9lMxu/Vi3wj5HsBryCZp1yFVJlRIG/WbrFcoEXWihNtokcA3cuyegLImpA==
         */

        @JsonProperty("alipay_trade_query_response")
        private AlipayTradeQueryResponseDTO alipayTradeQueryResponse;
        @JsonProperty("sign")
        private String sign;

        @NoArgsConstructor
        @Data
        public static class AlipayTradeQueryResponseDTO {
            /**
             * code : 10000 msg : Success buyer_logon_id : 189******83 buyer_pay_amount : 0.01 buyer_user_id :
             * 2088302179713571 fund_bill_list : [{"amount":"0.01","fund_channel":"PCREDIT"}] invoice_amount : 0.01
             * out_trade_no : alce1396757191930478592 point_amount : 0.00 receipt_amount : 0.01 send_pay_date :
             * 2018-05-24 17:17:40 total_amount : 0.01 trade_no : 2018052422001413571403596182 trade_status :
             * TRADE_SUCCESS
             */

            @JsonProperty("code")
            private String code;
            @JsonProperty("msg")
            private String msg;
            @JsonProperty("buyer_logon_id")
            private String buyerLogonId;
            @JsonProperty("buyer_pay_amount")
            private String buyerPayAmount;
            @JsonProperty("buyer_user_id")
            private String buyerUserId;
            @JsonProperty("buyer_user_name")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            private String buyerUserName;
            @JsonProperty("buyer_user_type")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            private String buyerUserType;
            @JsonProperty("fund_bill_list")
            private List<AlipayTradeQueryResponseDTO.FundBillListDTO> fundBillList;
            @JsonProperty("invoice_amount")
            private String invoiceAmount;
            @JsonProperty("out_trade_no")
            private String outTradeNo;
            @JsonProperty("point_amount")
            private String pointAmount;
            @JsonProperty("receipt_amount")
            private String receiptAmount;
            @JsonProperty("send_pay_date")
            private String sendPayDate;
            @JsonProperty("total_amount")
            private String totalAmount;
            @JsonProperty("trade_no")
            private String tradeNo;
            @JsonProperty("trade_status")
            private String tradeStatus;

            @NoArgsConstructor
            @Data
            public static class FundBillListDTO {
                /**
                 * amount : 0.01 fund_channel : PCREDIT
                 */

                @JsonProperty("amount")
                private String amount;
                @JsonProperty("fund_channel")
                private String fundChannel;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class KuaiQianQueryTradeRespDTO {
        /**
         * recordCount : pageCount : orderDetail :
         * [{"orderId":"kqce1397459540264730624","orderAmount":1,"orderTime":"20180526154812","dealTime":"20180526154848","payResult":"10","payType":"28","payAmount":1,"fee":0,"dealId":"104620558","signInfo":"HXu8njUgem3vr0LmV5uNle1%2Fmzn4FEIztezjiyuD3qaSX0HRRxLqstRuj26XXrZJiVHOOsBt1YbuPL3jUiqpUcE8r%2FwcijsvspLl80xv5oyQsMVF%2B4TRCRXw1eU5jlCtIo3QClNN8vY0HoUBcYbXL9FO9D8SCWSULLkiFWIDuILDp50I%2ByliokTfVz%2B%2FN%2F8KgyfB0c2H8wyI7hPasGe6Ik8Ge144milgO45i8eJC6I07W79ncV2A9NqmCtViqAtQtNPH1Y0zQHTkl5sSxXiBr28eklNza%2B%2B9hE3ahm1GqDf8OzUY2hiw1cFNUdj7ZjCLdQ8GWQ%2F4FT5SWqzV5SzLag%3D%3D"}]
         * signMsg :
         * BKSsqnsLpmZfghAzhF8vkIEoLR1FxtligQx%2FLiTMXhTifsKtWgIZDOPoqYnBpzTo1ebBcrFLhqUcvkUi%2BDF4UrDmOZwdfX%2Fb4ZC2ExWe7MQwfc2iKXMwPW7CrxcOccqLWfMPvCOJaTjDBThbIT7l%2FwKI22sdCtArBBJS1K0baTLtSZmYEPQywRqJx24M8pr9zRnaQ4fU6spR13rUMbUf6%2FkIRFJHgQRyid5EhcFmSW9JEcFsii28C9MAkCrdrYYfdHTKd8sXL4jh9IRPHTwqs7uL8TVypuJY3TYyq45he3Zja3esgOcEZorJzuc0k73v0u4nc4Nqsy4M%2BBYE8PY0gg%3D%3D
         * signType : 2 pageSize : merchantAcctId : 1001214035601 errCode : currentPage : version : v2.0
         */

        @JsonProperty("recordCount")
        private String recordCount;
        @JsonProperty("pageCount")
        private String pageCount;
        @JsonProperty("orderDetail")
        private List<OrderDetailDTO> orderDetail;
        @JsonProperty("signMsg")
        private String signMsg;
        @JsonProperty("signType")
        private Integer signType;
        @JsonProperty("pageSize")
        private String pageSize;
        @JsonProperty("merchantAcctId")
        private String merchantAcctId;
        @JsonProperty("errCode")
        private String errCode;
        @JsonProperty("currentPage")
        private String currentPage;
        @JsonProperty("version")
        private String version;

        @NoArgsConstructor
        @Data
        public static class OrderDetailDTO {
            /**
             * orderId : kqce1397459540264730624 orderAmount : 1 orderTime : 20180526154812 dealTime : 20180526154848
             * payResult : 10 payType : 28 payAmount : 1 fee : 0 dealId : 104620558 signInfo :
             * HXu8njUgem3vr0LmV5uNle1%2Fmzn4FEIztezjiyuD3qaSX0HRRxLqstRuj26XXrZJiVHOOsBt1YbuPL3jUiqpUcE8r%2FwcijsvspLl80xv5oyQsMVF%2B4TRCRXw1eU5jlCtIo3QClNN8vY0HoUBcYbXL9FO9D8SCWSULLkiFWIDuILDp50I%2ByliokTfVz%2B%2FN%2F8KgyfB0c2H8wyI7hPasGe6Ik8Ge144milgO45i8eJC6I07W79ncV2A9NqmCtViqAtQtNPH1Y0zQHTkl5sSxXiBr28eklNza%2B%2B9hE3ahm1GqDf8OzUY2hiw1cFNUdj7ZjCLdQ8GWQ%2F4FT5SWqzV5SzLag%3D%3D
             */

            @JsonProperty("orderId")
            private String orderId;
            @JsonProperty("orderAmount")
            private Integer orderAmount;
            @JsonProperty("orderTime")
            private String orderTime;
            @JsonProperty("dealTime")
            private String dealTime;
            @JsonProperty("payResult")
            private String payResult;
            @JsonProperty("payType")
            private String payType;
            @JsonProperty("payAmount")
            private Integer payAmount;
            @JsonProperty("fee")
            private Integer fee;
            @JsonProperty("dealId")
            private String dealId;
            @JsonProperty("signInfo")
            private String signInfo;
        }
    }

}
