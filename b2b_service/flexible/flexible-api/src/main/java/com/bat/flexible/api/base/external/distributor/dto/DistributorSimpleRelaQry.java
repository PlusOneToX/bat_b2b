package com.bat.flexible.api.base.external.distributor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributorSimpleRelaQry {

    @ApiModelProperty(value = "关联表主键id",notes = "非分销商id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;


    @ApiModelProperty(value = "公司名称")
    private String distributorCompanyName;
}
