package com.bat.promotion.dao.promotion;

import java.util.List;

import com.bat.promotion.dao.promotion.dataobject.PromotionDepartmentRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionDepartmentRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionDepartmentRelevanceDO record);

    int insertSelective(PromotionDepartmentRelevanceDO record);

    PromotionDepartmentRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionDepartmentRelevanceDO record);

    int updateByPrimaryKey(PromotionDepartmentRelevanceDO record);

    void deleteByPromotionId(Integer promotionId);

    void createDepartmentRelevanceList(List<PromotionDepartmentRelevanceDO> departmentRelevanceDOS);

    List<Integer> listDepartmentRelevanceIdByPromotionId(Integer promotionId);

    List<PromotionDepartmentRelevanceDO> listByPromotionIds(@Param("promotionIds") List<Integer> promotionIds);
}