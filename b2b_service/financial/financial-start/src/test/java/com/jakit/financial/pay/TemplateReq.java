package com.bat.financial.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/6 20:09
 */
@NoArgsConstructor
@Data
public class TemplateReq {
    @JsonProperty("voucher_type")
    private String voucherType;
    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("publish_start_time")
    private String publishStartTime;
    @JsonProperty("publish_end_time")
    private String publishEndTime;
    @JsonProperty("voucher_valid_period")
    private String voucherValidPeriod;
    @JsonProperty("voucher_available_time")
    private String voucherAvailableTime;
    @JsonProperty("out_biz_no")
    private String outBizNo;
    @JsonProperty("voucher_description")
    private String voucherDescription;
    @JsonProperty("voucher_quantity")
    private Integer voucherQuantity;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("floor_amount")
    private Double floorAmount;
    @JsonProperty("rule_conf")
    private String ruleConf;
    @JsonProperty("notify_uri")
    private String notifyUri;
    @JsonProperty("extension_info")
    private String extensionInfo;

    @Data
    static class ValidPeriod{
        @JsonProperty("type")
        private String type;
        @JsonProperty("start")
        private String start;
        @JsonProperty("end")
        private String end;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("unit")
        private String unit;
    }

    @Data
    static class AvailableTime{
        @JsonProperty("type")
        private String type;
        @JsonProperty("start")
        private String start;
        @JsonProperty("end")
        private String end;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("unit")
        private String unit;
    }
}
