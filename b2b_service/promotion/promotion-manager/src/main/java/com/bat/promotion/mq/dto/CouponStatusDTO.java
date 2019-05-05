package com.bat.promotion.mq.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/7/9 16:22
 */
@Data
public class CouponStatusDTO implements Serializable {
    /**
     * 优惠券码
     */
    private List<String> couponNos;
    /**
     * 优惠券状态
     */
    private Short couponStatus;
}
