package com.bat.dubboapi.order.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsStockUnLockDTO implements Serializable {

    private static final long serialVersionUID = -1659071978156527855L;
    /**
     * order_goods_stock主键
     */
    private Integer id;

    /**
     * 解锁数量
     */
    private Integer unLockQuantity;
}
