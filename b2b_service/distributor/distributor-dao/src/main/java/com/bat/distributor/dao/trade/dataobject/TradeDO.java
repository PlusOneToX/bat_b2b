package com.bat.distributor.dao.trade.dataobject;

import java.util.Date;

import com.bat.distributor.dao.category.dataobject.CategoryDO;

import lombok.Data;

@Data
public class TradeDO extends CategoryDO {
    private Integer id;

    private String name;

    private String nameEn;

    private Short payWay;

    private Integer settlingTime;

    private String erpSettleAccountNo;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}