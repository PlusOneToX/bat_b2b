package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/6/15 13:59
 */
public class DistributorPayWayRpcDTO implements Serializable {
    private Integer distributorId;

    private Short distributionPayWay;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getDistributionPayWay() {
        return distributionPayWay;
    }

    public void setDistributionPayWay(Short distributionPayWay) {
        this.distributionPayWay = distributionPayWay;
    }
}
