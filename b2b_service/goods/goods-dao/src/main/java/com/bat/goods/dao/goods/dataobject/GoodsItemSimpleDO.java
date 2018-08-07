package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsItemSimpleDO {

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品编码
     */
    private String goodsNo;


    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 货品编码
     */
    private String itemCode;

    /**
     * 货品名称
     */
    private String itemName;

    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * 销售价（非真实销售价）
     */
    private BigDecimal salePrice;
}
