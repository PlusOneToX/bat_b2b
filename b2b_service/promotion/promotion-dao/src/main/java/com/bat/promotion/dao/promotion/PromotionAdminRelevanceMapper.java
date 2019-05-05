package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionAdminRelevanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionAdminRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionAdminRelevanceDO record);

    int insertSelective(PromotionAdminRelevanceDO record);

    PromotionAdminRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionAdminRelevanceDO record);

    int updateByPrimaryKey(PromotionAdminRelevanceDO record);

    void deleteByPromotionId(Integer promotionId);

    void createAdminRelevanceList(List<PromotionAdminRelevanceDO> adminRelevanceDOS);

    List<Integer> listAdminRelevanceIdByPromotionId(Integer promotionId);
}