package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionRuleConditionSpecialDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionRuleConditionSpecialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRuleConditionSpecialDO record);

    void insertList(List<PromotionRuleConditionSpecialDO> records);

    int insertSelective(PromotionRuleConditionSpecialDO record);

    PromotionRuleConditionSpecialDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRuleConditionSpecialDO record);

    int updateByPrimaryKey(PromotionRuleConditionSpecialDO record);

    void deleteByPromotionId(Integer promotionId);

    List<PromotionRuleConditionSpecialDO> listByPromotionId(Integer promotionId);

    List<PromotionRuleConditionSpecialDO> listByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<PromotionRuleConditionSpecialDO> listByConditionIds(@Param("conditionIds") List<Integer> conditionIds);

    List<PromotionRuleConditionSpecialDO> listByConditionIdsAndGoodsIds(
        @Param("conditionIds") List<Integer> conditionIds, @Param("goodsIds") List<Integer> goodsIds);
}