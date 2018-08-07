package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class GoodsItemDO {
    private Integer id;
    private Integer goodsId;
    private String itemName;
    private String itemNameEn;
    private String itemCode;
    private Integer itemErpId;
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
    private Short lifeCycle;
    private String promotionStatus;
    private Short onwaySaleFlag;
    private Short saleStatus;
    private Date saleTime;
    private Date createTime;
    private Date updateTime;
}
