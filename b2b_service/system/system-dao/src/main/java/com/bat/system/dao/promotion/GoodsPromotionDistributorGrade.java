package com.bat.system.dao.promotion;

import com.bat.system.dao.promotion.dataobject.GoodsPromotionDistributorGradeDO;

public interface GoodsPromotionDistributorGrade {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionDistributorGradeDO record);

    int insertSelective(GoodsPromotionDistributorGradeDO record);

    GoodsPromotionDistributorGradeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPromotionDistributorGradeDO record);

    int updateByPrimaryKey(GoodsPromotionDistributorGradeDO record);
}