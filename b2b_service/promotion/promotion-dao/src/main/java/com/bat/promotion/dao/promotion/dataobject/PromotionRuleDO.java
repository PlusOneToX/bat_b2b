package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionRuleDO {
    private Integer id;

    private Integer promotionId;

    private String ruleName;

    private String ruleNameEn;

    private Short ruleType;

    private Short addUpFlag;

    private Short moneyOrCount;
}