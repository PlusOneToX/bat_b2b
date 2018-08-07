package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsScopeDO {
    private Integer id;
    private Integer brandId;
    private Integer categoryId;
    private Short distributorScope;
    private Short distributorScopeNo;
}
