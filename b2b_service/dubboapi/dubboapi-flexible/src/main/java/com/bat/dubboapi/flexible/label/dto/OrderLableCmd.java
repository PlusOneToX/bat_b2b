package com.bat.dubboapi.flexible.label.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderLableCmd implements Serializable {

    private static final long serialVersionUID = 1967512068388548609L;
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
