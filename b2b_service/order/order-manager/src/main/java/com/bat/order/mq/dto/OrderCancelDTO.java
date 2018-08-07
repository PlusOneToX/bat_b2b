package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class OrderCancelDTO implements Serializable {
    private Integer orderId;
    /**
     * 1 作废 2 关闭（包括提前关闭）
     */
    private Short erpCancelType;
    /**
     * erp订单编号
     */
    private String orderErpNo;

    /**
     * 取消原因
     */
    private String remark;
}
