package com.bat.promotion.dao.promotion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.promotion.dataobject.PromotionRuleDO;

@Mapper
public interface PromotionRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRuleDO record);

    void insertList(List<PromotionRuleDO> records);

    int insertSelective(PromotionRuleDO record);

    PromotionRuleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRuleDO record);

    int updateByPrimaryKey(PromotionRuleDO record);

    void deleteByPromotionId(Integer promotionId);

    List<PromotionRuleDO> listByPromotionId(Integer promotionId);

    List<PromotionRuleDO> listByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);

    List<PromotionRuleDO> listByIds(@Param("ids") List<Integer> ids);

    List<PromotionRuleDO> listByIdsAndOrderPromotion(@Param("ids") List<Integer> ids,
        @Param("promotionIds") List<Integer> promotionIds);
}