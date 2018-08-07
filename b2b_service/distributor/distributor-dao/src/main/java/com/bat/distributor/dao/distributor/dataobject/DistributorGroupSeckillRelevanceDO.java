package com.bat.distributor.dao.distributor.dataobject;

public class DistributorGroupSeckillRelevanceDO {
    private Integer id;

    private Integer distributorId;

    private String groupSeckillIds;

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

    public String getGroupSeckillIds() {
        return groupSeckillIds;
    }

    public void setGroupSeckillIds(String groupSeckillIds) {
        this.groupSeckillIds = groupSeckillIds == null ? null : groupSeckillIds.trim();
    }
}