package com.bat.dubboapi.order.delivery.dto;


import java.io.Serializable;

/**
 * Created by apple on 2019/7/10.
 */
public class ErpDeliverOrderStatusRequest implements Serializable {

    private String deliverOrderNo;
    /**
     * 单据状态 1.创建，2.已审核，3.取消，4.删除 ，5.提交
     */
    private Short status;
    private String expressNo;
    private String expressType;

    public String getDeliverOrderNo() {
        return deliverOrderNo;
    }

    public void setDeliverOrderNo(String deliverOrderNo) {
        this.deliverOrderNo = deliverOrderNo;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }
}
