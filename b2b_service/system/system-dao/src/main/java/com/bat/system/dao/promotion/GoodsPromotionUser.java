package com.bat.system.dao.promotion;

import com.bat.system.dao.promotion.dataobject.GoodsPromotionUserDO;

public interface GoodsPromotionUser {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionUserDO record);

    int insertSelective(GoodsPromotionUserDO record);

    GoodsPromotionUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPromotionUserDO record);

    int updateByPrimaryKey(GoodsPromotionUserDO record);
}