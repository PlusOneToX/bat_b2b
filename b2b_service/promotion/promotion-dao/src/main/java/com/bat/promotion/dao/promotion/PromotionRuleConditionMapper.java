package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionRuleConditionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionRuleConditionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRuleConditionDO record);

    void insertList(List<PromotionRuleConditionDO> records);

    int insertSelective(PromotionRuleConditionDO record);

    PromotionRuleConditionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRuleConditionDO record);

    int updateByPrimaryKey(PromotionRuleConditionDO record);

    void deleteByPromotionId(Integer promotionId);

    List<PromotionRuleConditionDO> listByPromotionId(Integer promotionId);

    List<PromotionRuleConditionDO> listByPromotionRuleIds(@Param("promotionRuleIds") List<Integer> promotionRuleIds);

    List<PromotionRuleConditionDO> listByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<PromotionRuleConditionDO> listByIds(@Param("ids") List<Integer> ids);

}