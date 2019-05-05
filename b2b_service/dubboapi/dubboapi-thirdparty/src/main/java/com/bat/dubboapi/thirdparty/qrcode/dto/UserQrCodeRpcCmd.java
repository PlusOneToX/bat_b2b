package com.bat.dubboapi.thirdparty.qrcode.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/20 8:50
 */
public class UserQrCodeRpcCmd implements Serializable {
    private Integer distributorId;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }
}
