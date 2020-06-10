package com.bat.flexible.api.model.dto.rela;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class ModelMaterialRelevanceDTO {

    @ApiModelProperty(value = "型号材质关联关系主键id")
    private Integer id;

    @ApiModelProperty(value = "型号id",notes = "新增必填")
    private Integer modelId;

    @ApiModelProperty(value = "材质id",notes = "新增必填")
    private Integer materialId;

    @ApiModelProperty(value = "长度",notes = "单位mm")
    private BigDecimal length;

    @ApiModelProperty(value = "宽",notes = "单位mm")
    private BigDecimal width;

    @ApiModelProperty(value = "高度",notes = "单位mm")
    private BigDecimal height;

    @ApiModelProperty(value = "重量",notes = "g")
    private BigDecimal weight;

    @ApiModelProperty(value = "外框图")
    private String outImage;

    @ApiModelProperty(value = "内框图")
    private String floorImage;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "第三方SKU编码（工厂）")
    private String thirdSku;

    @ApiModelProperty(value = "说明")
    private String warnMsg;

    @ApiModelProperty(value = "BOM编码")
    private String bomCode;

    @ApiModelProperty(value = "切割类型",notes = "1纵切 2横切、折叠屏手机才需要")
    private Short cutType;

    @ApiModelProperty(value = "纵切高度",notes = "折叠屏手机才需要")
    private Integer slittingHeight;

    @ApiModelProperty(value = "纵切高度留白",notes = "折叠屏手机才需要")
    private Integer slittingWhite;

    @ApiModelProperty(value = "横切宽度",notes = "折叠屏手机才需要")
    private Integer crosscuttingWidth;

    @ApiModelProperty(value = "横切宽度留白",notes = "折叠屏手机才需要")
    private Integer crosscuttingWhite;

    @ApiModelProperty(value = "缺货状态",notes ="1、缺货 0、正常" )
    private Short underStockFlag;

    @ApiModelProperty(value = "上边框 、TPU必填")
    private BigDecimal topFrame;

    @ApiModelProperty(value = "下边框 、TPU必填")
    private BigDecimal underFrame;

    @ApiModelProperty(value = "左边框 、TPU必填")
    private BigDecimal leftFrame;

    @ApiModelProperty(value = "右边框 、TPU必填")
    private BigDecimal rightFrame;
}
