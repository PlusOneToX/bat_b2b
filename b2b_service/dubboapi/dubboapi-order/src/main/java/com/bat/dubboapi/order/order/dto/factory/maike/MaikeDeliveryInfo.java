package com.bat.dubboapi.order.order.dto.factory.maike;

import java.io.Serializable;
import java.util.Date;

public class MaikeDeliveryInfo implements Serializable {

    private static final long serialVersionUID = -241026044293812596L;
    /**
     * 配送方式 id
     */
    private Integer delivery_method_id;

    /**
     * 是否海外 1、是 0、否
     */
    private Integer is_abroad;

    /**
     * 交货时间
     */
    private Date receipt_time;

    public Integer getDelivery_method_id() {
        return delivery_method_id;
    }

    public void setDelivery_method_id(Integer delivery_method_id) {
        this.delivery_method_id = delivery_method_id;
    }

    public Date getReceipt_time() {
        return receipt_time;
    }

    public void setReceipt_time(Date receipt_time) {
        this.receipt_time = receipt_time;
    }


    public Integer getIs_abroad() {
        return is_abroad;
    }

    public void setIs_abroad(Integer is_abroad) {
        this.is_abroad = is_abroad;
    }
}