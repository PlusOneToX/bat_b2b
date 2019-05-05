package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class PromotionDistributorRpcDTO implements Serializable {
    private List<Integer> promotionIds;
    private List<Integer> groupSeckillIds;

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
