package com.bat.dubboapi.thirdparty.erp.dto.system;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 15:34
 */
public class DepartmentErpRpcQry implements Serializable {
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
