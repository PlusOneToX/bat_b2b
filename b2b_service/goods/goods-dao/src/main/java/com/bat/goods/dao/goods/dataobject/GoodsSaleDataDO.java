package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsSaleDataDO {
    private Integer id;

    private Integer goodsId;

    private Integer saleNum;

    private Integer virtualNum;

    private Short showType;
}