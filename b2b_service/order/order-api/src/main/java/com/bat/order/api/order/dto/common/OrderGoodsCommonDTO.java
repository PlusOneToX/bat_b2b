package com.bat.order.api.order.dto.common;

import lombok.Data;

@Data
public class OrderGoodsCommonDTO {

    /**
     * order_goods主键
     */
    private Integer id;

    private Integer orderId;

    private Integer serialNumber;

    private Integer goodsId;

    private String goodsName;

    private String goodsNo;

    private Integer itemId;

    private String itemCode;

    private String barCode;

    private String itemName;

    private String specsName;

    private String colorName;

    private Integer warehouseId;

    private Integer itemCount;

    private Integer itemInCount;

    private Integer itemVmiCount;

    private Integer itemOnWayCount;

    private Integer deliverCount;

    private Integer unDeliverCount;
}
