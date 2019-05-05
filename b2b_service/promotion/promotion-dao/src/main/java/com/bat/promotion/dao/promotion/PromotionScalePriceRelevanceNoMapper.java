package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionScalePriceRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionScalePriceRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionScalePriceRelevanceNoDO record);

    int insertSelective(PromotionScalePriceRelevanceNoDO record);

    PromotionScalePriceRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionScalePriceRelevanceNoDO record);

    int updateByPrimaryKey(PromotionScalePriceRelevanceNoDO record);

    void deleteByPromotionId(Integer promotionId);

    void createScalePriceRelevanceNoList(List<PromotionScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS);

    List<Integer> listScalePriceRelevanceNoIdByPromotionId(Integer promotionId);
}