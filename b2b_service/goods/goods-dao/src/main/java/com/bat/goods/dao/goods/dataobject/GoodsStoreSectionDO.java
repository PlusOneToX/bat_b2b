package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsStoreSectionDO {
    private Integer id;
    private Integer sectionId;
    private Integer goodsId;
    private Integer sort;
}
