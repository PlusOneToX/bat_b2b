package com.bat.order.mq.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrderLableDTO implements Serializable {
    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 分销商id
     */
    private Integer distributorId;

    /**
     * 订单明细
     */
    private List<OrderGoodsDiySimpleDTO> diySimpleDTOList;
}
