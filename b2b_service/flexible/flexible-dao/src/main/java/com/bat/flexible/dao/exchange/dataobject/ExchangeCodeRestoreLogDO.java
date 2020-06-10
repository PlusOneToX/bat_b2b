package com.bat.flexible.dao.exchange.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class ExchangeCodeRestoreLogDO {
    private Integer id;

    private Integer exchangeCodeId;

    private Date createTime;

    private Integer oldOrderGoodsId;

    private Integer oldOrderId;

    private String oldUserOrderNo;

    private Long oldUserId;

    private Date useTime;


}