package com.bat.thirdparty.quanyi.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdQuanyiUnCancelCmd {

    @ApiModelProperty(value = "权益id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "作废原因")
    private String reason;

}