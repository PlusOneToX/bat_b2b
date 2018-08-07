package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserGoodsItemDO {

    private Integer id;
    private Integer goodsId;
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
    private String unit;
    private String itemImg;
    private String moq;
    private Short advanceSaleFlag;
    private Short onwaySaleFlag;
}
