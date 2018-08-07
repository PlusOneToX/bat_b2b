package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorGoodsRelevanceNoDO {
    private Integer id;

    private String goodsIds;

    private Integer distributorId;

}