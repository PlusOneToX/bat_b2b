package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorCategoryRelevanceDO {
    private Integer id;

    private Integer distributorId;

    private String categoryIds;

}