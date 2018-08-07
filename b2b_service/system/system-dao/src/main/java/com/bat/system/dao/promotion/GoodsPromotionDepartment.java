package com.bat.system.dao.promotion;

import com.bat.system.dao.promotion.dataobject.GoodsPromotionDepartmentDO;

public interface GoodsPromotionDepartment {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionDepartmentDO record);

    int insertSelective(GoodsPromotionDepartmentDO record);

    GoodsPromotionDepartmentDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPromotionDepartmentDO record);

    int updateByPrimaryKey(GoodsPromotionDepartmentDO record);
}