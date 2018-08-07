package com.bat.order.dao.order.dataobject;

import lombok.Data;

@Data
public class OrderGoodsDO2 {
    private Integer id;
    private Integer orderId;
    private Integer distributorId;
    private Integer warehouseId;
    private String orderErpNo;
    private Integer serialNumber;
    private Integer goodsId;
    private Integer itemId;
}