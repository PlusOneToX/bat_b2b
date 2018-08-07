package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SubAccountAdminConfigCmd {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分账时效类型 1、实时 2、延迟")
    private Short agingType;

    @ApiModelProperty(value = "延迟分账时间（单位小时）、时效类型为延迟时有值")
    private BigDecimal delayTime;

    @ApiModelProperty(value = "分账分销商等级名称列表")
    private List<String> levelNameList;

}
