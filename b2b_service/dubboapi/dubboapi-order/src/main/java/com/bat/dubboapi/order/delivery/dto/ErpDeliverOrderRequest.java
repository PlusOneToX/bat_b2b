package com.bat.dubboapi.order.delivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2019/7/10.
 */
public class ErpDeliverOrderRequest implements Serializable {

    private String deliverOrderNo;
    private String expressType;
    private String expressNo;
    @JsonProperty(value = "DocumentStatus")
    private Short documentStatus;
    private Long createTime;
    private Long checkTime;
    private List<ErpDeliverOrderDetailRequest> deliverOrderDetails = new ArrayList();

    public String getDeliverOrderNo() {
        return deliverOrderNo;
    }

    public void setDeliverOrderNo(String deliverOrderNo) {
        this.deliverOrderNo = deliverOrderNo;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public List<ErpDeliverOrderDetailRequest> getDeliverOrderDetails() {
        return deliverOrderDetails;
    }

    public void setDeliverOrderDetails(List<ErpDeliverOrderDetailRequest> deliverOrderDetails) {
        this.deliverOrderDetails = deliverOrderDetails;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Short getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Short documentStatus) {
        this.documentStatus = documentStatus;
    }
}
