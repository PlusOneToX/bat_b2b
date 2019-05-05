package com.bat.promotion.dao.groupseckill.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class GroupSeckillTypeGoodsDO implements Serializable {
    private Integer groupSeckillId;

    private Short groupSeckillType;

    private Integer goodsId;

    private Integer itemId;

    private BigDecimal groupSeckillPrice;

    private String goodsNo;

    private String itemCode;
}