package com.bat.promotion.dao.groupseckill.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class GroupSeckillStatusDO {
    private Integer id;
    private Short groupSeckillStatus;
    private Date updateTime;

}