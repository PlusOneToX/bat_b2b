package com.bat.dubboapi.thirdparty.erp.dto.system;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 15:34
 */
public class UserErpRpcQry implements Serializable {
    private String erpUserNo;

    public String getErpUserNo() {
        return erpUserNo;
    }

    public void setErpUserNo(String erpUserNo) {
        this.erpUserNo = erpUserNo;
    }
}
