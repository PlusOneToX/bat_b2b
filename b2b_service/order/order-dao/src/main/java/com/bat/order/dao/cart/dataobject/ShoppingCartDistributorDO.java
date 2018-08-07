package com.bat.order.dao.cart.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ShoppingCartDistributorDO {
    private Integer id;

    private Integer distributorId;

    private Integer goodsId;

    private String goodsNo;

    private String goodsName;

    private Integer itemId;

    private String itemCode;
    private String barCode;

    private String itemName;
    private String imageUrl;
    private String specsName;
    private String colorName;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    private Short goodsType;

    private Short diyType;
    private Short itemType;

    private Integer goodsPromotionId;

    private Integer orderPromotionId;

    private Integer groupSeckillId;

    private Short openFlag;

    private Integer itemCount;

    private String language;

    private Date createTime;

    private Date updateTime;

    private String unit;

}