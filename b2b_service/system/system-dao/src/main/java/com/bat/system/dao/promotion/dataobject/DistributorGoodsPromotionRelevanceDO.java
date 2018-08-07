package com.bat.system.dao.promotion.dataobject;

import lombok.Data;

@Data
public class DistributorGoodsPromotionRelevanceDO {
    private Integer id;

    private Integer distributorId;

    private String goodsPromotionId;
}