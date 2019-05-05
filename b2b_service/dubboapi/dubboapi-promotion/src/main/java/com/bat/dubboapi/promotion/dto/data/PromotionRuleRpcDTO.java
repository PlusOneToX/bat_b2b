package com.bat.dubboapi.promotion.dto.data;


import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class PromotionRuleRpcDTO implements Serializable {
    private Integer id;
    private Integer promotionId;
    private String ruleName;
    private String ruleNameEn;
    private Short ruleType;
    private Short addUpFlag;
    private Short moneyOrCount;
    private List<PromotionRuleConditionRpcDTO> conditions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleNameEn() {
        return ruleNameEn;
    }

    public void setRuleNameEn(String ruleNameEn) {
        this.ruleNameEn = ruleNameEn;
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public Short getAddUpFlag() {
        return addUpFlag;
    }

    public void setAddUpFlag(Short addUpFlag) {
        this.addUpFlag = addUpFlag;
    }

    public Short getMoneyOrCount() {
        return moneyOrCount;
    }

    public void setMoneyOrCount(Short moneyOrCount) {
        this.moneyOrCount = moneyOrCount;
    }

    public List<PromotionRuleConditionRpcDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<PromotionRuleConditionRpcDTO> conditions) {
        this.conditions = conditions;
    }
}
