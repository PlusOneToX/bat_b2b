package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

public class DistributorMsgNeedDataDTO implements Serializable {

    private Integer id;

    private Integer distributorId;

    private String openId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
