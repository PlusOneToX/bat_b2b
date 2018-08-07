package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DistributorSubAccountUserConfigDOQry {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分账配置名称")
    private String name;

    @ApiModelProperty(value = "分账金额类型 1、按照实付金额 2、按照利润金额")
    private Short amountType;




}