package com.bat.dubboapi.order.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsStockSimpleDTO implements Serializable {
    private static final long serialVersionUID = 6643118905881289120L;

    /**
     * order_goods_stock主键
     */
    private Integer id;

    /**
     * 行序号、对应ERP的销售订单明细内码
     */
    private Integer serialNumber;

    /**
     * 订单id
     */
    private Integer orderId;


    private Integer orderGoodsId;

    private Integer goodsId;

    private Integer itemId;

    private Integer stockId;

    /**
     * 下单锁的仓库、库存变更需要调整（ERP出库不按照B2B出）、具体仓库需要按照下面的lockType来处理
     */
    private Integer warehouseId;


    private Integer lockNum;

    /**
     *锁定类型 1.在库 2.在途 3.vmi
     */
    private Short lockType;

    /**
     * ERP单号
     */
    private String orderErpNo;
}
