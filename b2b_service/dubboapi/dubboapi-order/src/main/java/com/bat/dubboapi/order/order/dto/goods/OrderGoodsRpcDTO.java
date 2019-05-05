package com.bat.dubboapi.order.order.dto.goods;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderGoodsRpcDTO implements Serializable {
    private static final long serialVersionUID = -1665535273979063716L;
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
    private String imageUrl;
    private Short goodsType;
    private Short diyType;
    private String specsName;
    private String colorName;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private Integer warehouseId;
    private Integer itemCount;
    private Integer itemInCount;
    private Integer itemVmiCount;
    private Integer itemOnWayCount;
    private Integer deliverCount;
    private Integer unDeliverCount;
    private Date createTime;
    private Date updateTime;

    private Short itemType;

}