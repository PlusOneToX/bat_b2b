package com.bat.order.api.order.dto.common;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品装箱信息")
public class GoodsItemBoxDTO {

    private Integer id;
    @ApiModelProperty(value = "货品SKU id", example = "123343")
    private Integer goodsItemId;
    @ApiModelProperty(value = "箱子名称", example = "箱子名称")
    private String boxName;
    @ApiModelProperty(value = "箱子类型", example = "箱子类型")
    private String boxType;
    @ApiModelProperty(value = "箱子长度", example = "12.67")
    private BigDecimal boxLength;
    @ApiModelProperty(value = "箱子高度", example = "12.67")
    private BigDecimal boxHeight;
    @ApiModelProperty(value = "箱子宽度", example = "12.67")
    private BigDecimal boxWidth;
    @ApiModelProperty(value = "箱子重量", example = "12.67")
    private BigDecimal boxWeight;
    @ApiModelProperty(value = "箱子装箱数量", example = "12")
    private BigDecimal boxNum;
    @ApiModelProperty(value = "排序号", example = "12")
    private Integer sort;
    @ApiModelProperty(value = "是否按装箱数量售卖 1、是 0、否", example = "1")
    private Short defaultFlag;

}
