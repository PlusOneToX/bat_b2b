package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DistributorCustomPriceDTO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "商品编号")
    private String goodsNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "货品编码")
    private String itemCode;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    @ApiModelProperty(value = "货品名称")
    private String itemName;

    @ApiModelProperty(value = "材质名称")
    private String materialName;

    @ApiModelProperty(value = "C端价格")
    private BigDecimal price;

}
