package com.bat.dubboapi.goods.brand.dto.data;


import java.io.Serializable;

public class UserBrandRpcDTO implements Serializable {

    private Integer id;
    private String name;
    private String nameEn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
