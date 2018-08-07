package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsItemListDO {
    private Integer id;
    private Integer goodsId;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private Short goodsType;
    private Short diyType;
    private String itemName;
    private String itemNameEn;
    private String itemCode;
    private String barCode;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private Short saleStatus;
    private Short advanceSaleFlag;
    private Short onwaySaleFlag;
    private Integer brandId;
    private String brandName;
    private String brandNameEn;
    private Integer categoryId;
    private String categoryName;
    private String categoryNameEn;
    private String imageUrl1;
    private String imageUrl1en;
    private String itemImg;
    private String moq;
}
