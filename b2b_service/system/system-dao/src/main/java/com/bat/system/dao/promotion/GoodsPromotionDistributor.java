package com.bat.system.dao.promotion;

import com.bat.system.dao.promotion.dataobject.GoodsPromotionDistributorDO;

public interface GoodsPromotionDistributor {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionDistributorDO record);

    int insertSelective(GoodsPromotionDistributorDO record);

    GoodsPromotionDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPromotionDistributorDO record);

    int updateByPrimaryKey(GoodsPromotionDistributorDO record);
}