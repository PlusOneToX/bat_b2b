package com.bat.goods.api.goods.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品(SKU)扩展信息")
public class GoodsItemDataDTO {

    private Integer id;
    @ApiModelProperty(value = "货品SKU id", required = true, example = "123343")
    private Integer goodsItemId;
    @ApiModelProperty(value = "对应erp系列：F_PAEZ_XL", required = true, example = "F_PAEZ_XL")
    private String series;
    @ApiModelProperty(value = "对应erp应用设备：F_PAEZ_YYSB", required = true, example = "F_PAEZ_YYSB")
    private String appliedDevice;
    @ApiModelProperty(value = "对应erp产品型号en：F_PAEZ_CPXH", required = true, example = "F_PAEZ_CPXH")
    private String modelEn;
    @ApiModelProperty(value = "对应erp系列en：F_PAEZ_XL1", required = true, example = "F_PAEZ_XL1")
    private String seriesEn;
    @ApiModelProperty(value = "对应erp应用设备en：F_PAEZ_YYSB1", required = true, example = "F_PAEZ_YYSB1")
    private String appliedDeviceEn;
    @ApiModelProperty(value = "对应erp固定提前期:FFixLeadTime", required = true, example = "FFixLeadTime")
    private Integer purchaseCycle;
    @ApiModelProperty(value = "对应erp计划策略模式:F_PAEZ_MRP1", required = true, example = "F_PAEZ_MRP1")
    private String planStrategy;

}
