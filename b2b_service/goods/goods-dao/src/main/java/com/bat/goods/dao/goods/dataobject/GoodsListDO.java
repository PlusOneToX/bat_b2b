package com.bat.goods.dao.goods.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsListDO {
    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private String keywords;
    private String saleStatus;
    private Date saleTime;
    private Short freezeStatus;
    private Date freezeTime;
    private Short goodsType;
    private Short diyType;
    private Date createTime;
    private Date updateTime;
    private Integer brandId;
    private Integer categoryId;
}
