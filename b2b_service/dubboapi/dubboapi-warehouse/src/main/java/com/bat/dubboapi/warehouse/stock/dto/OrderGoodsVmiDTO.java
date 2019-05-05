package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsVmiDTO implements Serializable {


    private static final long serialVersionUID = 1501276528319156388L;
    private Long orderGoodsId;

    /**
     * VMI扣减
     */
    private Integer vmiCount;
}
