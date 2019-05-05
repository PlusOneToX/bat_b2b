package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorInfoRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
    /**
     * ERP余额是否同步 1.是 0.否
     */
    private Short erpBalanceFlag;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Short getErpBalanceFlag() {
        return erpBalanceFlag;
    }

    public void setErpBalanceFlag(Short erpBalanceFlag) {
        this.erpBalanceFlag = erpBalanceFlag;
    }
}
