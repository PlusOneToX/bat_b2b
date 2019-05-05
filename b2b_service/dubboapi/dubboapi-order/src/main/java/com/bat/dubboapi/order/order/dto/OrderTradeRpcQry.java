package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.util.List;

public class OrderTradeRpcQry implements Serializable {

    /**
     * 订单号列表
     */
    private List<Integer> orderIds;
    /**
     * 交易方类型： 1分销商 2 C端客户
     */
    private Short counterpartyType;
    /**
     * 交易类型为1时必填
     */
    private Integer distributorId;
    /**
     * 交易类型为2时必填
     */
    private Integer customerId;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public Short getCounterpartyType() {
        return counterpartyType;
    }

    public void setCounterpartyType(Short counterpartyType) {
        this.counterpartyType = counterpartyType;
    }
}
