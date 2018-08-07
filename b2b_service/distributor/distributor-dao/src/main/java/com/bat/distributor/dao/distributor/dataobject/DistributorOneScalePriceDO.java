package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorOneScalePriceDO {
    private Integer id;

    private Integer distributorId;

    private Integer brandId;

    private Integer categoryId;

    private Integer scalePriceId;

    private Integer distributionScalePriceId;

    private Short operationType;
}