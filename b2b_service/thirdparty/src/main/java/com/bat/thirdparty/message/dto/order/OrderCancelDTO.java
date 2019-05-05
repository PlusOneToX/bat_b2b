package com.bat.thirdparty.message.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/4 8:55
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
