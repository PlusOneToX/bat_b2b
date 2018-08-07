package com.bat.order.dao.order.dataobject;

import lombok.Data;

@Data
public class OrderGoodsThirdCodeDO {
    private Integer id;

    private Integer orderGoodsId;

    private Integer orderId;

    private String code;

    private Short writeOffFlag;

    private Integer distributorId;

    private String platform;
}