package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionAdminRelevanceDO {
    private Integer id;

    private Integer promotionId;

    private Integer adminId;
}