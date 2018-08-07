package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserGoodsItemBoxDO {

    private Integer id;
    private Integer goodsItemId;
    private String boxName;
    private String boxType;
    private BigDecimal boxLength;
    private BigDecimal boxHeight;
    private BigDecimal boxWidth;
    private BigDecimal boxWeight;
    private BigDecimal boxNum;
    private Integer sort;
    private Short defaultFlag;

}
