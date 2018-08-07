package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsItemIdDO {
    private Integer id;
    private Integer goodsId;
    private Integer ItemErpId;
    private String goodsNo;
    private String itemCode;
    private String itemName;
}
