package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsParamDO {

    private Integer id;
    private String paramName;
    private String paramNameEn;
    private Integer valueId;
    private String paramValueName;
    private String paramValueNameEn;

}
