package com.bat.promotion.dao.promotion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.promotion.dao.promotion.dataobject.PromotionDistributorRelevanceNoDO;

@Mapper
public interface PromotionDistributorRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionDistributorRelevanceNoDO record);

    int insertSelective(PromotionDistributorRelevanceNoDO record);

    PromotionDistributorRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionDistributorRelevanceNoDO record);

    int updateByPrimaryKey(PromotionDistributorRelevanceNoDO record);

    void deleteByPromotionId(Integer promotionId);

    void createDistributorRelevanceNoList(List<PromotionDistributorRelevanceNoDO> distributorRelevanceNoDOS);

    List<Integer> listDistributorRelevanceNoIdByPromotionId(Integer promotionId);
}