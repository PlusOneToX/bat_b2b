package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorGoodsPromotionRelevanceDO {
    private Integer id;

    private Integer distributorId;

    private String goodsPromotionId;
}