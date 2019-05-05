package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class PromotionGroupSeckillIdsByAllDistributorDTO implements Serializable {
    List<Integer> promotionIds;
    List<Integer> groupSeckillIds;

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }

    public List<Integer> getGroupSeckillIds() {
        return groupSeckillIds;
    }

    public void setGroupSeckillIds(List<Integer> groupSeckillIds) {
        this.groupSeckillIds = groupSeckillIds;
    }
}
