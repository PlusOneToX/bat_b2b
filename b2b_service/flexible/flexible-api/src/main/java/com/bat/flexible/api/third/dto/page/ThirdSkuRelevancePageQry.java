package com.bat.flexible.api.third.dto.page;


import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ThirdSkuRelevancePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;


}
