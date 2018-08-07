package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class GoodsItemBoxDO {
    private Integer id;
    private Integer goodsItemId;
    private String boxName;
    private String boxType;
    private BigDecimal boxLength;
    private BigDecimal boxHeight;
    private BigDecimal boxWidth;
    private BigDecimal boxWeight;
    private BigDecimal boxNum;
    private String boxErpId;
    private Integer sort;
    private Short defaultFlag;
    private Date createTime;
    private Date updateTime;
}
