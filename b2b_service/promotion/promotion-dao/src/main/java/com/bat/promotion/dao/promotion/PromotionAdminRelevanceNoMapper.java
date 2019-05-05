package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionAdminRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionAdminRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionAdminRelevanceNoDO record);

    int insertSelective(PromotionAdminRelevanceNoDO record);

    PromotionAdminRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionAdminRelevanceNoDO record);

    int updateByPrimaryKey(PromotionAdminRelevanceNoDO record);

    void deleteByPromotionId(Integer promotionId);

    void createAdminRelevanceNoList(List<PromotionAdminRelevanceNoDO> adminRelevanceNoDOS);

    List<Integer> listAdminRelevanceNoIdByPromotionId(Integer promotionId);
}