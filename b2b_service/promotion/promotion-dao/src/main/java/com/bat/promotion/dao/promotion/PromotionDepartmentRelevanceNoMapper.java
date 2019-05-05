package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionDepartmentRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromotionDepartmentRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionDepartmentRelevanceNoDO record);

    int insertSelective(PromotionDepartmentRelevanceNoDO record);

    PromotionDepartmentRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionDepartmentRelevanceNoDO record);

    int updateByPrimaryKey(PromotionDepartmentRelevanceNoDO record);

    void deleteByPromotionId(Integer promotionId);

    void createDepartmentRelevanceNoList(List<PromotionDepartmentRelevanceNoDO> departmentRelevanceNoDOS);

    List<Integer> listDepartmentRelevanceNoIdByPromotionId(Integer promotionId);
}