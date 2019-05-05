package com.bat.dubboapi.distributor.distributor.dto.data;


import java.io.Serializable;

public class DistributorContactsRpcDTO implements Serializable {

    private String name;

    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}