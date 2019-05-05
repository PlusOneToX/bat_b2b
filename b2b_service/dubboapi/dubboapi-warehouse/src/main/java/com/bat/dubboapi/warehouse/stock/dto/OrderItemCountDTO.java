package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemCountDTO implements Serializable {

    private static final long serialVersionUID = 2668862534169802374L;
    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * 下单数量
     */
    private Integer count;

    /**
     * 货品编码
     */
    private String itemCode;

    /**
     * order_goods主键id
     */
    private Long orderGoodsId;

    /**
     * 拼团活动id
     */
    private Integer spellGroupId;
}
