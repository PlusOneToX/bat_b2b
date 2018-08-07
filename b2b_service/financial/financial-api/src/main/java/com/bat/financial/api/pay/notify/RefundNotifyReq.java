package com.bat.financial.api.pay.notify;

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
public class RefundNotifyReq {
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

    WXRefundNotifyV3Req wxReqV3;

    @NoArgsConstructor
    @Data
    public static class WXRefundNotifyV3Req {
        /**
         * id : EV-2018022511223320873 create_time : 2018-06-08T10:34:56+08:00 resource_type : encrypt-resource
         * event_type : REFUND.SUCCESS summary : 退款成功 resource :
         * {"algorithm":"AEAD_AES_256_GCM","original_type":"refund","ciphertext":"...","nonce":"...","associated_data":""}
         */

        @JsonProperty("id")
        private String id;
        @JsonProperty("create_time")
        private String createTime;
        @JsonProperty("resource_type")
        private String resourceType;
        @JsonProperty("event_type")
        private String eventType;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("resource")
        private ResourceDTO resource;

        @NoArgsConstructor
        @Data
        public static class ResourceDTO {
            /**
             * algorithm : AEAD_AES_256_GCM original_type : refund ciphertext : ... nonce : ... associated_data :
             */

            @JsonProperty("algorithm")
            private String algorithm;
            @JsonProperty("original_type")
            private String originalType;
            @JsonProperty("ciphertext")
            private String ciphertext;
            @JsonProperty("nonce")
            private String nonce;
            @JsonProperty("associated_data")
            private String associatedData;
        }
    }

}
