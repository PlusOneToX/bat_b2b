package com.bat.distributor.dao.distributor.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DistributorSpecialGoodsDO {
    private Integer id;

    private Integer distributorId;

    private Integer goodsId;

    private Integer goodsItemId;

    private BigDecimal distributorPrice;

    private Date createTime;

    private Date updateTime;
}