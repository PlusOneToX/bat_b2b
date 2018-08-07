package com.bat.distributor.api.platform.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商微信平台分销商关联信息")
public class PlatformDistributorDTO {
    private Integer distributorId;
    @ApiModelProperty(value = "分销商用户名", example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商公司名", example = "bat")
    private String companyName;
}