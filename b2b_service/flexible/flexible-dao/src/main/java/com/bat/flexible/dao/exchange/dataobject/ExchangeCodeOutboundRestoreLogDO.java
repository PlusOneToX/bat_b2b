package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeCodeOutboundRestoreLogDO {
    private Integer id;

    private String boxCode;

    private String newOutboundNo;

    private String oldOutboundNo;

    private Integer distributorOrderId;

    private Integer distributorOrderGoodsId;

    private Integer distributorId;

    private String distributorName;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode == null ? null : boxCode.trim();
    }

    public String getNewOutboundNo() {
        return newOutboundNo;
    }

    public void setNewOutboundNo(String newOutboundNo) {
        this.newOutboundNo = newOutboundNo == null ? null : newOutboundNo.trim();
    }

    public String getOldOutboundNo() {
        return oldOutboundNo;
    }

    public void setOldOutboundNo(String oldOutboundNo) {
        this.oldOutboundNo = oldOutboundNo == null ? null : oldOutboundNo.trim();
    }

    public Integer getDistributorOrderId() {
        return distributorOrderId;
    }

    public void setDistributorOrderId(Integer distributorOrderId) {
        this.distributorOrderId = distributorOrderId;
    }

    public Integer getDistributorOrderGoodsId() {
        return distributorOrderGoodsId;
    }

    public void setDistributorOrderGoodsId(Integer distributorOrderGoodsId) {
        this.distributorOrderGoodsId = distributorOrderGoodsId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName == null ? null : distributorName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}