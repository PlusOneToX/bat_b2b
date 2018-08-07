package com.bat.financial.pay.data.wxv3;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/24 14:03
 */
@NoArgsConstructor
@Data
public class WxRefundTradeReqDTO {

    /**
     * out_trade_no : sdkbat201878920180326144648 notify_url : https://weixin.qq.com/ amount :
     * {"total":1,"currency":"CNY","refund":1} out_refund_no : sdkbat201878920180326144648 reason : 商品已售完
     */

    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("amount")
    private AmountDTO amount;
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    @JsonProperty("reason")
    private String reason;

    @NoArgsConstructor
    @Data
    public static class AmountDTO {
        /**
         * total : 1 currency : CNY refund : 1
         */

        @JsonProperty("total")
        private Integer total;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("refund")
        private Integer refund;
    }
}
