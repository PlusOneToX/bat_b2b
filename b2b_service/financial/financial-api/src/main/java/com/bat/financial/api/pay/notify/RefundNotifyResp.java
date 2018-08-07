package com.bat.financial.api.pay.notify;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class RefundNotifyResp {

    WXRefundNotifyResp wxResp;

    // AlipayCreateNotifyResp alipayResp;
    //
    // KuaiQianCreateNotifyResp kuaiQianResp;

    @NoArgsConstructor
    @Data
    public static class WXRefundNotifyResp {
        /**
         * code : SUCCESS message : 成功
         */

        @JsonProperty("code")
        private String code;
        @JsonProperty("message")
        private String message;
        @JsonInclude(JsonInclude.Include.NON_NULL) // 若被注解的字段值为 null，则序列化时忽略该字段。
        @JsonProperty("v2_result_xml")
        private String xml;
    }

    // @NoArgsConstructor
    // @Data
    // public static class AlipayCreateNotifyResp {
    // @JsonProperty("message")
    // private String message;
    // }
    //
    // @NoArgsConstructor
    // @Data
    // public static class KuaiQianCreateNotifyResp {
    // @JsonProperty("message")
    // private String message;
    // }
}
