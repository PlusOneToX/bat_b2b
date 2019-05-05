package com.bat.dubboapi.promotion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class OrderPromotionRpcQry implements Serializable {
    private List<Integer> promotionIds;
    private List<Integer> spellGroupIds;

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }

    public List<Integer> getSpellGroupIds() {
        return spellGroupIds;
    }

    public void setSpellGroupIds(List<Integer> spellGroupIds) {
        this.spellGroupIds = spellGroupIds;
    }
}
