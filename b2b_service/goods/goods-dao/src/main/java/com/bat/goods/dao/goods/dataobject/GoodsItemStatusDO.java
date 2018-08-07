package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsItemStatusDO {
    private Integer id;
    private Integer goodsId;
    private Short saleStatus;
    private Short lifeCycle;
}
