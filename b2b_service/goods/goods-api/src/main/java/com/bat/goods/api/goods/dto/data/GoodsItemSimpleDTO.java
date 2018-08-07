package com.bat.goods.api.goods.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsItemSimpleDTO {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String goodsNo;


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 货品编码
     */
    @ApiModelProperty(value = "货品编码")
    private String itemCode;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称")
    private String itemName;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    @ApiModelProperty(value = "默认销售价")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "材质名称、不一定有值")
    private String materialName;

}
