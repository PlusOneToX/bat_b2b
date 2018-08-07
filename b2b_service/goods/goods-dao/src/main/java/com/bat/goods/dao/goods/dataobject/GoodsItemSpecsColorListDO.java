package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsItemSpecsColorListDO {
    private Integer id;
    private Integer goodsItemId;
    private Short attributeType;
    private Integer attributeValueId;
    private String attributeValueName;
    private String attributeValueNameEn;
}
