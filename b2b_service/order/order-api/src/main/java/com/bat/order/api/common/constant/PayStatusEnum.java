package com.bat.order.api.common.constant;

/**
 * 付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款
 */
public enum PayStatusEnum {

    NoPay((short) 1,"未付款"),
    PayPart((short) 2,"部分付款"),
    HasPay((short) 3,"已付款"),
    PartReturn((short) 4,"部分退款"),
    RetuenApply((short) 5,"退款申请中"),
    HasReturn((short) 6,"已退款"),
    ;

    private Short payStatus;

    private String payStatusStr;

    PayStatusEnum(Short payStatus, String payStatusStr) {
        this.payStatus = payStatus;
        this.payStatusStr = payStatusStr;
    }

    public static String statusStr(Short status) {
        if(status==null){
            return "";
        }
        for (PayStatusEnum statusEnum : values()) {
            if (statusEnum.getPayStatus() == status.shortValue()) {
                return statusEnum.getPayStatusStr();
            }
        }
        return "";
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusStr() {
        return payStatusStr;
    }

    public void setPayStatusStr(String payStatusStr) {
        this.payStatusStr = payStatusStr;
    }
}
