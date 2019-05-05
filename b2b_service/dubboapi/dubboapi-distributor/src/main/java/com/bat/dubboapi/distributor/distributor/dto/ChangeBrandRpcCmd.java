package com.bat.dubboapi.distributor.distributor.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class ChangeBrandRpcCmd implements Serializable {

    private Integer brandId;

    private Short operationType;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Short getOperationType() {
        return operationType;
    }

    public void setOperationType(Short operationType) {
        this.operationType = operationType;
    }
}
