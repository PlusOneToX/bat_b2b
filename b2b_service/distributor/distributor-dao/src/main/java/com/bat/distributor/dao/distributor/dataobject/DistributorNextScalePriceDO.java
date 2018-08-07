package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorNextScalePriceDO {
    private Integer id;

    private Integer distributorId;

    private Integer nextScalePriceId;
}