package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商基本信息")
public class DistributorIdsDTO {

    private Integer id;
    @ApiModelProperty(value = "用户名(登录名)", example = "bat")
    private String name;
    @ApiModelProperty(value = "公司名", example = "bat")
    private String companyName;

}
