package com.bat.goods.api.user.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品SPU参数信息")
public class UserParamDTO {

    @ApiModelProperty(value = "商品参数名称", example = "bat")
    private String paramName;
    @ApiModelProperty(value = "商品参数英文名称", example = "bat")
    private String paramNameEn;
    @ApiModelProperty(value = "商品参数值名称", example = "bat")
    private String paramValueName;
    @ApiModelProperty(value = "商品参数值英文名称", example = "bat")
    private String paramValueNameEn;

}
