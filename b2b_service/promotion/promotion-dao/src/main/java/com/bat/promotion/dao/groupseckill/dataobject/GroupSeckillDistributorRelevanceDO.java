package com.bat.promotion.dao.groupseckill.dataobject;

import lombok.Data;

@Data
public class GroupSeckillDistributorRelevanceDO {
    private Integer id;

    private Integer groupSeckillId;

    private Integer distributorId;

    private String name;
    private String companyName;

}