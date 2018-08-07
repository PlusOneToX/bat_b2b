package com.bat.goods.dao.goods.dataobject;

import java.util.Date;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/30 16:42
 */
@Data
public class UserGoodsListDO {
    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private Short goodsType;
    private Short diyType;
    private Date saleTime;
    private String imageUrl1;
    private String imageUrl1en;
    private Integer saleNum;
    private Integer brandId;
    private Integer categoryId;
    private String brandName;
    private Integer categoryName;
    private String brandNameEn;
    private Integer categoryNameEn;
    private Short underStockFlag;
    private Short whetherOutOfStockInTransit;
    private Integer activity;
    private Integer stock;
}
