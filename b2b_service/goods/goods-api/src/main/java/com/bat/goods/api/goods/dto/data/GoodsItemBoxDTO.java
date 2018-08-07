package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品装箱信息")
public class GoodsItemBoxDTO {

    private Integer id;
    @ApiModelProperty(value = "货品SKU id", required = true, example = "123343")
    private Integer goodsItemId;
    @ApiModelProperty(value = "箱子名称", required = true, example = "箱子名称")
    private String boxName;
    @ApiModelProperty(value = "箱子类型", required = true, example = "箱子类型")
    private String boxType;
    @ApiModelProperty(value = "箱子长度", required = true, example = "12.67")
    private BigDecimal boxLength;
    @ApiModelProperty(value = "箱子高度", required = true, example = "12.67")
    private BigDecimal boxHeight;
    @ApiModelProperty(value = "箱子宽度", required = true, example = "12.67")
    private BigDecimal boxWidth;
    @ApiModelProperty(value = "箱子重量", required = true, example = "12.67")
    private BigDecimal boxWeight;
    @ApiModelProperty(value = "箱子装箱数量", required = true, example = "12")
    private BigDecimal boxNum;
    @ApiModelProperty(value = "箱子erpID", required = true, example = "12345")
    private String boxErpId;
    @ApiModelProperty(value = "排序号", required = true, example = "12")
    private Integer sort;
    @ApiModelProperty(value = "是否按装箱数量售卖 1、是 0、否", required = true, example = "1")
    private Short defaultFlag;
    @ApiModelProperty(value = "创建时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
