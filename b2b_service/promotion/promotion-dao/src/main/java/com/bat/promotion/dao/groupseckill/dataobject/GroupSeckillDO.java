package com.bat.promotion.dao.groupseckill.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class GroupSeckillDO {
    private Integer id;

    private String name;

    private String groupSeckillDesc;

    private Short groupSeckillType;

    private Date startTime;

    private Date endTime;

    private Short applyStatus;

    private Short groupSeckillStatus;

    private Integer sort;

    private Short advanceFlag;

    private Short distributorScope;

    private Short distributorScopeNo;

    private Date createTime;

    private Date updateTime;

}