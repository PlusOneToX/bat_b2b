package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class OrderPromotionRpcDTO implements Serializable {
    private List<PromotionRpcDTO> promotions;
    private List<GoodsItemGroupSeckillRpcDTO> spellGroups;

    public List<PromotionRpcDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionRpcDTO> promotions) {
        this.promotions = promotions;
    }

    public List<GoodsItemGroupSeckillRpcDTO> getSpellGroups() {
        return spellGroups;
    }

    public void setSpellGroups(List<GoodsItemGroupSeckillRpcDTO> spellGroups) {
        this.spellGroups = spellGroups;
    }
}
