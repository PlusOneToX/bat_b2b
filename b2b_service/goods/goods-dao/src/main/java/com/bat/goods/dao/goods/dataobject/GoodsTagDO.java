package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsTagDO {

    private Integer id;
    private Integer goodsId;
    private String name;
    private String nameEn;

}
