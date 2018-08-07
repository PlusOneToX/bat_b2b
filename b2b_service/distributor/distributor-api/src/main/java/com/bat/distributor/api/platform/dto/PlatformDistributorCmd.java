package com.bat.distributor.api.platform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商微信平台分销商关联信息")
public class PlatformDistributorCmd {
    @NotNull(message = "P_DISTRIBUTOR_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "12344")
    private Integer distributorId;
    @NotBlank(message = "P_DISTRIBUTOR_DISTRIBUTOR_NAME_NULL")
    @ApiModelProperty(value = "分销商用户名", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商公司名", required = false, example = "bat")
    private String companyName;

}