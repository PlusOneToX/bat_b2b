package com.bat.flexible.dao.model.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class ModelMaterialRelevanceBaseCO {

    @ApiModelProperty(value = "是否缺货 1、是 0、否")
    private Short underStockFlag;

    @ApiModelProperty(value = "长度")
    private BigDecimal length;

    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    @ApiModelProperty(value = "外框图")
    private String outImage;

    @ApiModelProperty(value = "底图")
    private String floorImage;

    @ApiModelProperty(value = "切割类型 1纵切 2横切")
    private Short cutType;

    @ApiModelProperty(value = "纵切高度")
    private Integer slittingHeight;

    @ApiModelProperty(value = "纵切高度留白")
    private Integer slittingWhite;

    @ApiModelProperty(value = "横切宽度")
    private Integer crosscuttingWidth;

    @ApiModelProperty(value = "横切宽度留白")
    private Integer crosscuttingWhite;
}
