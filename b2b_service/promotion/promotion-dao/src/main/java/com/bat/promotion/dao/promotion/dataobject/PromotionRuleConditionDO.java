package com.bat.promotion.dao.promotion.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PromotionRuleConditionDO {
    private Integer id;

    private Integer promotionId;

    private Integer promotionRuleId;

    private String conditionName;

    private String conditionNameEn;

    private Short specialFlag;

    private Integer oneBuyCount;

    private BigDecimal oneBuyMoney;

    private Short reduceOrPresent;

    private Short reduceType;

    private Short reductionPresentAddFlag;

    private BigDecimal discount;

    private BigDecimal reduction;

    private Integer presentCount;
}