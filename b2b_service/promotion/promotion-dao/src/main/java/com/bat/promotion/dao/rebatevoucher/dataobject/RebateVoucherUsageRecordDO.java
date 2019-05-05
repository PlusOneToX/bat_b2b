package com.bat.promotion.dao.rebatevoucher.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class RebateVoucherUsageRecordDO {
    private Integer id;

    private Integer rebateVoucherId;

    private String rebateVoucherNo;

    private BigDecimal useAmount;

    private BigDecimal balance;

    private Integer orderId;

    private String orderNo;

    private Date useTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRebateVoucherId() {
        return rebateVoucherId;
    }

    public void setRebateVoucherId(Integer rebateVoucherId) {
        this.rebateVoucherId = rebateVoucherId;
    }

    public String getRebateVoucherNo() {
        return rebateVoucherNo;
    }

    public void setRebateVoucherNo(String rebateVoucherNo) {
        this.rebateVoucherNo = rebateVoucherNo == null ? null : rebateVoucherNo.trim();
    }

    public BigDecimal getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(BigDecimal useAmount) {
        this.useAmount = useAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }
}