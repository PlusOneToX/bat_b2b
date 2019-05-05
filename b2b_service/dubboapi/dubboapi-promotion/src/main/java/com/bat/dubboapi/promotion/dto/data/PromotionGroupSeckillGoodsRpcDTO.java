package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class PromotionGroupSeckillGoodsRpcDTO implements Serializable {
    List<Integer> promotionStepGoodsIds;
    List<Integer> promotionGoodsIds;
    List<Integer> groupGoodsIds;
    List<Integer> seckillGoodsIds;

    public List<Integer> getPromotionGoodsIds() {
        return promotionGoodsIds;
    }

    public void setPromotionGoodsIds(List<Integer> promotionGoodsIds) {
        this.promotionGoodsIds = promotionGoodsIds;
    }

    public List<Integer> getGroupGoodsIds() {
        return groupGoodsIds;
    }

    public void setGroupGoodsIds(List<Integer> groupGoodsIds) {
        this.groupGoodsIds = groupGoodsIds;
    }

    public List<Integer> getSeckillGoodsIds() {
        return seckillGoodsIds;
    }

    public void setSeckillGoodsIds(List<Integer> seckillGoodsIds) {
        this.seckillGoodsIds = seckillGoodsIds;
    }

    public List<Integer> getPromotionStepGoodsIds() {
        return promotionStepGoodsIds;
    }

    public void setPromotionStepGoodsIds(List<Integer> promotionStepGoodsIds) {
        this.promotionStepGoodsIds = promotionStepGoodsIds;
    }
}
