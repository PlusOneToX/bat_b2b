package com.bat.thirdparty.msgcenter.dao.dataobject;

public class MsgCenterDistributorGroupRelevanceDO {
    private Integer id;

    private Integer centerId;

    private Integer distributorGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public Integer getDistributorGroupId() {
        return distributorGroupId;
    }

    public void setDistributorGroupId(Integer distributorGroupId) {
        this.distributorGroupId = distributorGroupId;
    }
}