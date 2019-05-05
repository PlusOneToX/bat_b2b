package com.bat.dubboapi.promotion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/3/31 10:15
 */
public class CouponCustomerRpcQry implements Serializable {
    private List<Integer> customerIds;
    private String couponCode;
    private Short couponStatus;

    public List<Integer> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Short getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Short couponStatus) {
        this.couponStatus = couponStatus;
    }

    @Override
    public String toString() {
        return "CouponCustomerRpcQry{" +
                "customerIds=" + customerIds +
                ", couponCode='" + couponCode + '\'' +
                ", couponStatus=" + couponStatus +
                '}';
    }
}
