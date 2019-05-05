package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionDistributorRelevanceDO {
    private Integer id;

    private Integer promotionId;

    private Integer distributorId;

    private String name;
    private String companyName;
}