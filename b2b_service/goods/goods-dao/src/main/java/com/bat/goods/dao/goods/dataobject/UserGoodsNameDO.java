package com.bat.goods.dao.goods.dataobject;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserGoodsNameDO implements Serializable {
    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String itemCode;
}
