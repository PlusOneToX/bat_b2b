package com.bat.order.dao.deliver.dataobject;

import java.util.Date;

public class OrderDeliverBillListDO extends OrderDeliverBillDO {

    private String userName;

    private Date orderCreateTime;

    private String orderNo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
