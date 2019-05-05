package com.bat.promotion.dao.groupseckill.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class GroupSeckillGoodsDO implements Serializable {
    private Integer id;

    private Integer groupSeckillId;

    private Integer goodsId;

    private Integer itemId;

    private Integer sort;

    private Short existFlag;

    private Short mtoFlag;

    private Integer maxNum;

    private Integer minNum;

    private BigDecimal groupSeckillPrice;

    private Integer virtualSum;

    private Integer realSum;

    private String goodsNo;

    private String itemCode;
}