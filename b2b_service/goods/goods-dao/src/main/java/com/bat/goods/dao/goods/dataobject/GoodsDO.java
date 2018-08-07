package com.bat.goods.dao.goods.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsDO {
    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private String keywords;
    private String introduce;
    private String introduceEn;
    private Integer brandId;
    private Integer categoryId;
    private Short saleStatus;
    private Date saleTime;
    private Short freezeStatus;
    private Date freezeTime;
    private Short goodsType;
    private Short diyType;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private String imageUrl5;
    private String imageUrl6;
    private String imageUrl1en;
    private String imageUrl2en;
    private String imageUrl3en;
    private String imageUrl4en;
    private String imageUrl5en;
    private String imageUrl6en;
    private String contentUrl;
    private String contentUrlEn;
    private Short distributorScope;
    private Short distributorScopeNo;
    private Date createTime;
    private Date updateTime;
}
