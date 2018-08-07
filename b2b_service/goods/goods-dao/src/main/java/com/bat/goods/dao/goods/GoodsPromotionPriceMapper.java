package com.bat.goods.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsPromotionPriceDO;

@Mapper
public interface GoodsPromotionPriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionPriceDO record);

    void insertList(List<GoodsPromotionPriceDO> records);

    void deleteByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteByPromotionId(@Param("promotionId") Integer promotionId);

    void deleteByGroupSeckillId(@Param("groupSeckillId") Integer groupSeckillId);

    GoodsPromotionPriceDO selectByPrimaryKey(Integer id);

    List<GoodsPromotionPriceDO> selectAll();

    List<GoodsPromotionPriceDO> listByGoodsIdsAndPromotionIds(@Param("goodsIds") List<Integer> goodsIds,
        @Param("promotionIds") List<Integer> promotionIds);

    List<GoodsPromotionPriceDO> listByGoodsIdsAndGroupSeckillIds(@Param("goodsIds") List<Integer> goodsIds,
        @Param("groupSeckillIds") List<Integer> groupSeckillIds);

    int updateByPrimaryKey(GoodsPromotionPriceDO record);
}