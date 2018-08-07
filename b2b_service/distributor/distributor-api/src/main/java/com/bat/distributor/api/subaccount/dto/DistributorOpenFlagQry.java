package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "分销商ID和状态")
public class DistributorOpenFlagQry {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "12424")
    private Integer distributorId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "D_COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "下级业务员的等级ID、通过下级查询上级业务员列表")
    private Integer sonLevelId;
}
