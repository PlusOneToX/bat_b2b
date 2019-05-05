package com.bat.dubboapi.distributor.subaccount.dto;

import java.io.Serializable;


public class DistributorSubAccountUserConfigRpcDTOQry implements Serializable {


    private static final long serialVersionUID = -3312450630374852783L;

    private Integer id;

    private Integer distributorId;

    private String name;

    private Short amountType;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Short getAmountType() {
        return amountType;
    }

    public void setAmountType(Short amountType) {
        this.amountType = amountType;
    }


}