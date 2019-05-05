package com.bat.thirdparty.erp.api.request.dto;

/**
 * Created by apple on 2019/7/15.
 */
public class ErpVoucherDetailsEntry {

    private String orderNo;
    private String voucherNo;
    private Long voucherTime;
    private Double amount;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Long getVoucherTime() {
        return voucherTime;
    }

    public void setVoucherTime(Long voucherTime) {
        this.voucherTime = voucherTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
