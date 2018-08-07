package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsStoreColumnDO {
    private Integer id;
    private Integer columnId;
    private Integer goodsId;
    private Integer sort;
}
