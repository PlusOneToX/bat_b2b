package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionScalePriceRelevanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionScalePriceRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionScalePriceRelevanceDO record);

    int insertSelective(PromotionScalePriceRelevanceDO record);

    PromotionScalePriceRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionScalePriceRelevanceDO record);

    int updateByPrimaryKey(PromotionScalePriceRelevanceDO record);

    void deleteByPromotionId(Integer promotionId);

    void createScalePriceRelevanceList(List<PromotionScalePriceRelevanceDO> scalePriceRelevanceDOS);

    List<Integer> listScalePriceRelevanceIdByPromotionId(Integer promotionId);
}