package com.bat.promotion.dao.promotion;

import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.promotion.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.promotion.dataobject.*;

@Mapper
public interface UserPromotionMapper {
    List<UserPromotionDO> listPromotion(Map map);

    List<UserPromotionDO> listPromotionByIds(@Param("ids") List<Integer> ids);

    List<PromotionRuleGoodsDO> listPromotionRuleGoodsByPromotionIdsAndGoodsId(
        @Param("promotionIds") List<Integer> promotionIds, @Param("promoTypes") List<String> promoTypes,
        @Param("goodsId") Integer goodsId);

    List<PromotionRuleDO> listPromotionRuleByIds(@Param("ids") List<Integer> ids);

    List<PromotionRuleDO> listOrderPromotionRule(@Param("promotionIds") List<Integer> promotionIds,
        @Param("promoTypes") List<String> promoTypes);

    List<PromotionRuleConditionDO>
        listPromotionRuleConditionByPromotionRuleIds(@Param("promotionRuleIds") List<Integer> promotionRuleIds);

    List<PromotionRuleConditionPresentDO> listPresentByPromotionRuleConditionIds(
        @Param("promotionRuleConditionIds") List<Integer> promotionRuleConditionIds);

    List<PromotionRuleConditionSpecialDO> listSpecialByPromotionRuleConditionIdsAndGoodsId(
        @Param("promotionRuleConditionIds") List<Integer> promotionRuleConditionIds, @Param("goodsId") Integer goodsId);
}