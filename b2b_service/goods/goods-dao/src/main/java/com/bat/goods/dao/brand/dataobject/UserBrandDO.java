package com.bat.goods.dao.brand.dataobject;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserBrandDO implements Serializable {

    private Integer id;
    private String name;
    private String nameEn;

}
