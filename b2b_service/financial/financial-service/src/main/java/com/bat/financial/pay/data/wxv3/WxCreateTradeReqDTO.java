package com.bat.financial.pay.data.wxv3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 10:24
 */
@NoArgsConstructor
@Data
public class WxCreateTradeReqDTO {

    /**
     * mchid : 1900006XXX out_trade_no : native12177525012014070332333 appid : wxdace645e0bc2cXXX description :
     * Image形象店-深圳腾大-QQ公仔 notify_url : https://weixin.qq.com/ amount : {"total":1,"currency":"CNY"}
     */

    @JsonProperty("mchid")
    private String mchid;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("time_expire")
    private String timeExpire;
    @JsonProperty("appid")
    private String appid;
    @JsonProperty("description")
    private String description;
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("amount")
    private AmountDTO amount;
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
         * total : 1 currency : CNY
         */

        @JsonProperty("total")
        private Integer total;
        @JsonProperty("currency")
        private String currency;
    }

    @NoArgsConstructor
    @Data
    public static class PayerDTO {
        /**
         * "openid": "o4GgauInH_RCEdvrrNGrntXDuXXX"
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
