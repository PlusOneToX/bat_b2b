package com.bat.warehouse.dao.onWay.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class GoodsOnWayStockDO implements Serializable {
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