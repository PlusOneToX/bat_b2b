package com.bat.goods.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsMinMaxPriceDO;

@Mapper
public interface GoodsMinMaxPriceMapper {

    void createList(List<GoodsMinMaxPriceDO> goodsMinMaxPriceDOS);

    void updateList(List<GoodsMinMaxPriceDO> goodsMinMaxPriceDOS);

    void deleteByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsMinMaxPriceDO> listByGoodsIdsAndScalePriceIds(@Param("goodsIds") List<Integer> goodsIds,
        @Param("scalePriceIds") List<Integer> ScalePriceIds);

    List<GoodsMinMaxPriceDO> listByGoodsIdsAndScalePrice(@Param("goodsIds") List<Integer> goodsIds);

}
