package com.bat.promotion.dao.groupseckill.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
public class UserGoodsItemGroupSeckillDO {
    private Integer id;
    private Integer groupSeckillId;
    private String name;
    private String groupSeckillDesc;
    private Short groupSeckillType;
    private Date startTime;
    private Date endTime;
    private Short groupSeckillStatus;
    private Integer itemId;
    private Short existFlag;
    private Short mtoFlag;
    private Integer maxNum;
    private Integer minNum;
    private BigDecimal groupSeckillPrice;
    private Integer virtualSum;
    private Integer realSum;
}
