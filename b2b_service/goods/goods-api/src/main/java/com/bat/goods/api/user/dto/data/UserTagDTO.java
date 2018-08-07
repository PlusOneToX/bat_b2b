package com.bat.goods.api.user.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品SPU标签信息")
public class UserTagDTO {

    @ApiModelProperty(value = "商品标签中文名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "商品标签英文名称", example = "bat")
    private String nameEn;

}
