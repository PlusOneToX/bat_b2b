package com.bat.promotion.dao.promotion.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PromotionRuleConditionSpecialDO {
    private Integer id;

    private Integer promotionId;

    private Integer promotionRuleConditionId;

    private Integer goodsId;

    private Integer itemId;

    private BigDecimal specialPrice;

}