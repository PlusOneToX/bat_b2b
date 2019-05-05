package com.bat.thirdparty.factory.maike.request.devlivery;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiOrderDeliveryCallbackModelRequest {

    @JsonProperty(value = "OrderNo")
    @JSONField(ordinal = 1)
    @NotBlank(message = "订单号不能为空")
    private String OrderNo;

    @JsonProperty(value = "ExpressNo")
    @JSONField(ordinal = 2)
    @NotBlank(message = "快递单号不能为空")
    private String ExpressNo;

    @JsonProperty(value = "ExpressCode")
    @JSONField(ordinal = 3)
    @NotBlank(message = "快递公司编码不能为空")
    private String ExpressCode;

    @JsonProperty(value = "ExpressName")
    @JSONField(ordinal = 4)
    @NotBlank(message = "快递公司名称不能为空")
    private String ExpressName;

    @JsonProperty(value = "ExpressTime")
    @JSONField(ordinal = 5)
    @NotNull(message = "发货时间不能为空")
    private Long ExpressTime;

}
