package com.bat.dubboapi.distributor.distributor.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class DistributorChangeBrandRpcCmd implements Serializable {

    private Integer distributorId;

    private List<ChangeBrandRpcCmd> brandCmds;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public List<ChangeBrandRpcCmd> getBrandCmds() {
        return brandCmds;
    }

    public void setBrandCmds(List<ChangeBrandRpcCmd> brandCmds) {
        this.brandCmds = brandCmds;
    }
}
