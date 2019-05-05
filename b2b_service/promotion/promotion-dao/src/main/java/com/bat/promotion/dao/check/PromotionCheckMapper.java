package com.bat.promotion.dao.check;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.check.dataobject.PromotionCheckDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckListDO;

@Mapper
public interface PromotionCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionCheckDO record);

    PromotionCheckDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PromotionCheckDO record);

    List<PromotionCheckListDO> listByUserId(Map map);

    List<PromotionCheckListDO> listByCheckUserId(Map map);

    PromotionCheckDO selectCheckIngByPromotionIdAndPromotionType(@Param(value = "distributorId") Integer distributorId,
        @Param(value = "promotionType") Short promotionType);

    List<PromotionCheckDO> listByPromotionIdsAndPromotionType(@Param("promotionIds") List<Integer> promotionIds,
        @Param("promotionCheckType") Short promotionCheckType);
}