package com.bat.thirdparty.order.api.dto.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ThirdOrderSyncLogDTO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "渠道来源")
    private String platform;

    @ApiModelProperty(value = "渠道订单号")
    private String otherOrderNo;

    @ApiModelProperty(value = "收货人")
    private String receiver;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @ApiModelProperty(value = "调用状态 1、成功 0、失败")
    private Short status;

    @ApiModelProperty(value = "异常原因")
    private String errorMsg;

    @ApiModelProperty(value = "B2B订单号")
    private String orderNo;

    @ApiModelProperty(value = "请求体")
    private String requestBody;


}
