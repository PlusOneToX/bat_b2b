package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsItemSyncDO {
    private Integer id;
    private Integer goodsId;
    private String itemCode;
    private Integer itemErpId;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
}
