package com.bat.flexible.api.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsItemSimplePageBean implements Serializable {

    private static final long serialVersionUID = -5508054128186787017L;

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

    @ApiModelProperty(value = "货品上架状态，1未上架，2审批中，3已上架 ")
    private Short saleStatus;

}
