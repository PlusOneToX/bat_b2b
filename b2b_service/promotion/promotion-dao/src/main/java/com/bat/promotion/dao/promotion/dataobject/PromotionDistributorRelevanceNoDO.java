package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionDistributorRelevanceNoDO {
    private Integer id;

    private Integer promotionId;

    private Integer distributorId;
}