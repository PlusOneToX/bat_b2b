package com.bat.order.api.common.constant;

/**
 * 订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成
 */
public enum OrderStatusEnum {

    WillConfirm((short) 1,"待确认"),
    HasConfirm((short) 2,"已确认"),
    HasRefuse((short) 3,"已拒绝"),
    HasCancel((short) 4,"已取消"),
    HasFinish((short) 5,"已完成"),
    ;

    private Short orderStatus;

    private String orderStatusStr;

    OrderStatusEnum(Short orderStatus, String orderStatusStr) {
        this.orderStatus = orderStatus;
        this.orderStatusStr = orderStatusStr;
    }

    public static String statusStr(Short status) {
        if(status==null){
            return "";
        }
        for (OrderStatusEnum statusEnum : values()) {
            if (statusEnum.getOrderStatus() == status.shortValue()) {
                return statusEnum.getOrderStatusStr();
            }
        }
        return "";
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusStr() {
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }
}
