package com.bat.flexible.api.distributor.cooperation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class FlexibleDistributorCooperationCmd {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message="COMMON_OPNE_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "合作类型 1、全部渠道类型 2、部分渠道合作、默认是2")
    private Short cooperationType;

    @ApiModelProperty(value = "是否默认选中 1、是 0、否")
    private Short defaultChoose;

    @ApiModelProperty(value = "备注")
    private String remark;

}
