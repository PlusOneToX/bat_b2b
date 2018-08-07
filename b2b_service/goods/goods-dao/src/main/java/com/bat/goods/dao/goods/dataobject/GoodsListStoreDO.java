package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsListStoreDO {
    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private String saleStatus;
    private Short freezeStatus;
    private Short goodsType;
    private Short diyType;
    private Integer sort;
}
