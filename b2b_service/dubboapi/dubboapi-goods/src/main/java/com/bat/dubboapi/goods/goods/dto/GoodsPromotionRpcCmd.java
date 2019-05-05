package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsPromotionRpcCmd implements Serializable {

    private Integer id;
    private List<GoodsPromotionRuleRpcCmd> rules;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<GoodsPromotionRuleRpcCmd> getRules() {
        return rules;
    }

    public void setRules(List<GoodsPromotionRuleRpcCmd> rules) {
        this.rules = rules;
    }
}
