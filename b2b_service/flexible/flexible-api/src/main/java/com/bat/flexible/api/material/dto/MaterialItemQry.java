package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialItemQry {

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    @ApiModelProperty(value = "货品编码")
    private String itemCode;
}
