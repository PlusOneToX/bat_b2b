package com.bat.order.dao.deliver.dataobject;

import java.util.Date;

public class OrderDeliverBillDO {
    private Integer id;

    private Integer orderId;

    private Integer distributionId;

    private String distributionCode;

    private String distributionName;

    private Short deliverStatus;

    private String logisticsNo;

    private String logisticsStatus;

    private String deliverErpNo;

    private String deliverStkNo;

    private Short push;

    private Short pushStatus;

    private Date deliverTime;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Integer distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistributionCode() {
        return distributionCode;
    }

    public void setDistributionCode(String distributionCode) {
        this.distributionCode = distributionCode;
    }

    public String getDistributionName() {
        return distributionName;
    }

    public void setDistributionName(String distributionName) {
        this.distributionName = distributionName == null ? null : distributionName.trim();
    }

    public Short getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Short deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus == null ? null : logisticsStatus.trim();
    }

    public String getDeliverErpNo() {
        return deliverErpNo;
    }

    public void setDeliverErpNo(String deliverErpNo) {
        this.deliverErpNo = deliverErpNo == null ? null : deliverErpNo.trim();
    }

    public String getDeliverStkNo() {
        return deliverStkNo;
    }

    public void setDeliverStkNo(String deliverStkNo) {
        this.deliverStkNo = deliverStkNo == null ? null : deliverStkNo.trim();
    }

    public Short getPush() {
        return push;
    }

    public void setPush(Short push) {
        this.push = push;
    }

    public Short getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Short pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}