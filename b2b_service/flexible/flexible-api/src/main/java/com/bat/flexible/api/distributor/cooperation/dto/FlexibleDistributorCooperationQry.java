package com.bat.flexible.api.distributor.cooperation.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FlexibleDistributorCooperationQry extends BasePageParamQry {


    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;


    @ApiModelProperty(value = "分销商编码")
    private String content;

}
