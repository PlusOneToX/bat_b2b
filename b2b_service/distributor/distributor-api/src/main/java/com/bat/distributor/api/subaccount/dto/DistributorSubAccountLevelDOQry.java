package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DistributorSubAccountLevelDOQry {

    @ApiModelProperty(value = "等级id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "序号")
    private Integer sequence;


}