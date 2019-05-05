package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionRuleGoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionRuleGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRuleGoodsDO record);

    void insertList(List<PromotionRuleGoodsDO> records);

    int insertSelective(PromotionRuleGoodsDO record);

    PromotionRuleGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRuleGoodsDO record);

    int updateByPrimaryKey(PromotionRuleGoodsDO record);

    void deleteByPromotionId(Integer promotionId);

    List<PromotionRuleGoodsDO> listByPromotionId(Integer promotionId);

    List<PromotionRuleGoodsDO> listByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<Integer> selectGoodsIdsByPromotionRuleId(Integer promotionRuleId);

    List<Integer> selectItemIdsByPromotionRuleId(Integer promotionRuleId);

    List<Integer> promotionGoodsIdsByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<Integer> promotionStepGoodsIdsByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<PromotionRuleGoodsDO> listPromotionRuleGoodsByPromotionIdsAndGoodsIds(
        @Param("promotionIds") List<Integer> promotionIds, @Param("promoTypes") List<String> promoTypes,
        @Param("goodsIds") List<Integer> goodsIds);

    List<Integer> salesPromotionAll();
}