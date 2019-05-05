package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsPromotionRuleRpcCmd implements Serializable {

    private Short ruleType;
    private Short moneyOrCount;
    private List<Integer> ids;
    private List<GoodsConditionRpcCmd> conditions;

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public Short getMoneyOrCount() {
        return moneyOrCount;
    }

    public void setMoneyOrCount(Short moneyOrCount) {
        this.moneyOrCount = moneyOrCount;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<GoodsConditionRpcCmd> getConditions() {
        return conditions;
    }

    public void setConditions(List<GoodsConditionRpcCmd> conditions) {
        this.conditions = conditions;
    }
}
