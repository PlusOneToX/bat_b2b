package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionRuleConditionPresentDO {
    private Integer id;

    private Integer promotionId;

    private Integer promotionRuleConditionId;

    private Integer goodsId;

    private Integer itemId;

    private Integer count;

    private Integer totalCount;
}