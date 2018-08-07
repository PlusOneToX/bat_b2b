package com.bat.goods.dao.goods.dataobject;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserGoodsRpcDO implements Serializable {

    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private String imageUrl1;
    private String imageUrl1en;
    private Short goodsType;
    private Short diyType;

}
