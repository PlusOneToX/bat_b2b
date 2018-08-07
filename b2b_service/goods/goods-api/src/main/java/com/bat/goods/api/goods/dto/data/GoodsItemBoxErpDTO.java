package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品erp装箱信息")
public class GoodsItemBoxErpDTO {
    @ApiModelProperty(value = "货品erp编码", example = "8056754333")
    private String itemCode;
    @ApiModelProperty(value = "箱子名称", example = "bat")
    private String boxName;
    @ApiModelProperty(value = "箱子类型", example = "内箱")
    private String boxType;
    @ApiModelProperty(value = "箱子长度", example = "23.561")
    private BigDecimal boxLength;
    @ApiModelProperty(value = "箱子高度", example = "23.561")
    private BigDecimal boxHeight;
    @ApiModelProperty(value = "箱子宽度", example = "23.561")
    private BigDecimal boxWidth;
    @ApiModelProperty(value = "箱子重量", example = "23.561")
    private BigDecimal boxWeight;
    @ApiModelProperty(value = "箱子装箱数量", example = "23.561")
    private BigDecimal boxNum;
    @ApiModelProperty(value = "箱子erpID", example = "235667")
    private String boxErpId;
}
