package com.bat.order.dao.deliver.dataobject;

import java.util.Date;

public class OrderDeliveryTraceDO {
    private Integer id;

    private Integer orderDeliverBillId;

    private Date acceptTime;

    private String acceptStation;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderDeliverBillId() {
        return orderDeliverBillId;
    }

    public void setOrderDeliverBillId(Integer orderDeliverBillId) {
        this.orderDeliverBillId = orderDeliverBillId;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return acceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation == null ? null : acceptStation.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}