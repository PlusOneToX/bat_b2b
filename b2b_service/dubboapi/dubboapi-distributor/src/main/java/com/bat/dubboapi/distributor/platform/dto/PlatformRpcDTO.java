package com.bat.dubboapi.distributor.platform.dto;


import java.io.Serializable;

public class PlatformRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String platformNo;

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

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }
}
