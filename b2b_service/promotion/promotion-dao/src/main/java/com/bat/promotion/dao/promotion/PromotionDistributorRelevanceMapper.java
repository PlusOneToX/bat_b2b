package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionDistributorRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionDistributorRelevanceDO record);

    int insertSelective(PromotionDistributorRelevanceDO record);

    PromotionDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionDistributorRelevanceDO record);

    int updateByPrimaryKey(PromotionDistributorRelevanceDO record);

    void deleteByPromotionId(Integer promotionId);

    void createDistributorRelevanceList(List<PromotionDistributorRelevanceDO> distributorRelevanceDOS);

    List<PromotionDistributorRelevanceDO> listDistributorRelevanceByPromotionId(Integer promotionId);
}