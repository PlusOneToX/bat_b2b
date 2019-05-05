package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionRuleConditionPresentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionRuleConditionPresentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRuleConditionPresentDO record);

    void insertList(List<PromotionRuleConditionPresentDO> records);

    int insertSelective(PromotionRuleConditionPresentDO record);

    PromotionRuleConditionPresentDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRuleConditionPresentDO record);

    int updateByPrimaryKey(PromotionRuleConditionPresentDO record);

    void deleteByPromotionId(Integer promotionId);

    List<PromotionRuleConditionPresentDO> listByPromotionId(Integer promotionId);

    List<PromotionRuleConditionPresentDO> listByConditionId(Integer conditionId);

    List<PromotionRuleConditionPresentDO> listByConditionIds(@Param("conditionIds") List<Integer> conditionIds);
}