package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GoodsOnWayStockRpcDTO implements Serializable {
    private static final long serialVersionUID = 6447101461640029080L;
    private Integer id;

    private Integer goodsId;

    private Integer itemId;

    private Integer itemErpId;

    private Integer numOnWay;

    private Integer numLock;

    private Date createTime;

    private Date updateTime;

    private String itemCode;

    private String itemName;


}