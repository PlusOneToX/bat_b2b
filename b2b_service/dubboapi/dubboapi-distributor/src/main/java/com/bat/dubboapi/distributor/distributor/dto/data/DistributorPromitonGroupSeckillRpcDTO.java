package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorPromitonGroupSeckillRpcDTO implements Serializable {
    /**
     * 促销活动ids
     */
    List<Integer> promotionIds;
    /**
     * 拼团秒杀活动ids
     */
    List<Integer> groupSeckilIds;
    /**
     * 分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)
     */
    Short distributionPromotionFlag;
    /**
     * 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
     */
    Short promotionScope;
    /**
     * 可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动
     */
    String promotionTypes;

    public Short getPromotionScope() {
        return promotionScope;
    }

    public void setPromotionScope(Short promotionScope) {
        this.promotionScope = promotionScope;
    }

    public String getPromotionTypes() {
        return promotionTypes;
    }

    public void setPromotionTypes(String promotionTypes) {
        this.promotionTypes = promotionTypes;
    }

    public Short getDistributionPromotionFlag() {
        return distributionPromotionFlag;
    }

    public void setDistributionPromotionFlag(Short distributionPromotionFlag) {
        this.distributionPromotionFlag = distributionPromotionFlag;
    }

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }

    public List<Integer> getGroupSeckilIds() {
        return groupSeckilIds;
    }

    public void setGroupSeckilIds(List<Integer> groupSeckilIds) {
        this.groupSeckilIds = groupSeckilIds;
    }
}
