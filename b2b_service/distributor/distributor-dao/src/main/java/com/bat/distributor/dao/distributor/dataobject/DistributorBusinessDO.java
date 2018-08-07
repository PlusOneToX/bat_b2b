package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorBusinessDO {
    private Integer id;

    private Integer distributorId;

    private Integer salesId;
    private Integer coordinatorId;

    private String distributorGroupIds;

    private Integer distributorCategoryId;

    private Short autoDelivery;

    private Short canExportPrice;

    private Short promotionScope;

    private String promotionTypes;

    private Short rxShopSwitch;

    private Short logisticsSmsSwitch;

    private Short onWayFlag;

    private Date createTime;

    private Date updateTime;
}