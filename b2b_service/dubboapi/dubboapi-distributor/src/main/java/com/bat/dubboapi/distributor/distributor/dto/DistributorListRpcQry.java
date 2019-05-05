package com.bat.dubboapi.distributor.distributor.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorListRpcQry implements Serializable {
    private List<Integer> distributorIds;

    public List<Integer> getDistributorIds() {
        return distributorIds;
    }

    public void setDistributorIds(List<Integer> distributorIds) {
        this.distributorIds = distributorIds;
    }
}
