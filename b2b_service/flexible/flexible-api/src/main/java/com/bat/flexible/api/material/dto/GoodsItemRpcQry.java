package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsItemRpcQry implements Serializable {

    @ApiModelProperty(value = "货品id")
    private Integer id;
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "货品名称")
    private String itemName;

    @ApiModelProperty(value = "货品名称英文")
    private String itemNameEn;

    @ApiModelProperty(value = "货品编码")
    private String itemCode;

    @ApiModelProperty(value = "货品barCode")
    private String barCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品名称英文")
    private String goodsNameEn;

    @ApiModelProperty(value = "商品编码")
    private String goodsNo;

    @ApiModelProperty(value = "商品类型")
    private Short goodsType;

    @ApiModelProperty(value = "定制类型")
    private Short diyType;

    @ApiModelProperty(value = "规格值id")
    private Integer specsId;

    @ApiModelProperty(value = "规格值名称")
    private String specsName;

    @ApiModelProperty(value = "规格值英文")
    private String specsNameEn;

    @ApiModelProperty(value = "颜色id")
    private Integer colorId;

    @ApiModelProperty(value = "颜色名称")
    private String colorName;

    @ApiModelProperty(value = "颜色名称英文")
    private String colorNameEn;

    @ApiModelProperty(value = "品牌id")
    private Integer brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

    @ApiModelProperty(value = "品牌名称英文")
    private String brandNameEn;

    @ApiModelProperty(value = "商品分类英文")
    private String categoryNameEn;

    @ApiModelProperty(value = "商品主图图片")
    private String imageUrl1;

    @ApiModelProperty(value = "商品主图英文")
    private String imageUrl1en;

    @ApiModelProperty(value = "货品图片")
    private String itemImg;


}
