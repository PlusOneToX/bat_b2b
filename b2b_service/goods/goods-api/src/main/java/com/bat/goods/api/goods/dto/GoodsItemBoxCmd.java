package com.bat.goods.api.goods.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsItemBoxCmd", description = "货品装箱信息")
public class GoodsItemBoxCmd {

    @ApiModelProperty(value = "货品装箱id(更新时必填)", required = false, example = "123343")
    private Integer id;
    @ApiModelProperty(value = "货品SKU id", required = false, example = "123343")
    private Integer goodsItemId;
    @ApiModelProperty(value = "箱子名称", required = false, example = "箱子名称")
    private String boxName;
    @ApiModelProperty(value = "箱子类型", required = false, example = "箱子类型")
    private String boxType;
    @ApiModelProperty(value = "箱子长度", required = false, example = "12.67")
    private BigDecimal boxLength;
    @ApiModelProperty(value = "箱子高度", required = false, example = "12.67")
    private BigDecimal boxHeight;
    @ApiModelProperty(value = "箱子宽度", required = false, example = "12.67")
    private BigDecimal boxWidth;
    @ApiModelProperty(value = "箱子重量", required = false, example = "12.67")
    private BigDecimal boxWeight;
    @ApiModelProperty(value = "箱子装箱数量", required = false, example = "12")
    private BigDecimal boxNum;
    @ApiModelProperty(value = "箱子erpID", required = false, example = "12345")
    private String boxErpId;
    @ApiModelProperty(value = "排序号", required = false, example = "12")
    private Integer sort;
    @ApiModelProperty(value = "是否按装箱数量售卖 1、是 0、否", required = false, example = "1")
    private Short defaultFlag;

}
