package com.bat.dubboapi.order.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ErpOrderChangeCmd implements Serializable {
    /**
     * erp订单编号
     */
    private String orderNo;
    /**
     * 订单交期时间
     */
    private Date deliverTime;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * erp订单变更单编号
     */
    private String xOrderNo;
    /**
     * erp变更单币种
     */
    private String FSettleCurrId;
    /**
     * erp订单变更明细
     */
    private List<ErpOrderChangeDetailCmd> orderDetails;
}
