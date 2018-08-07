package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsStoreMobileDO {
    private Integer id;

    private Integer mobileId;

    private Integer goodsId;

    private Integer sort;

    private Short moduleType;

}