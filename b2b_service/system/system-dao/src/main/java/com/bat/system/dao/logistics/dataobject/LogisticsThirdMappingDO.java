package com.bat.system.dao.logistics.dataobject;

public class LogisticsThirdMappingDO {
    private Integer id;

    private Integer logisticsId;

    private Short thirdType;

    private Short status;

    private String thirdDeliveryNo;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public Short getThirdType() {
        return thirdType;
    }

    public void setThirdType(Short thirdType) {
        this.thirdType = thirdType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getThirdDeliveryNo() {
        return thirdDeliveryNo;
    }

    public void setThirdDeliveryNo(String thirdDeliveryNo) {
        this.thirdDeliveryNo = thirdDeliveryNo == null ? null : thirdDeliveryNo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}