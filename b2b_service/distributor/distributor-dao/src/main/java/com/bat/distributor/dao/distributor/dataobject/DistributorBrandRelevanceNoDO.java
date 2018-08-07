package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorBrandRelevanceNoDO {
    private Integer id;

    private Integer distributorId;

    private String brandIds;

}