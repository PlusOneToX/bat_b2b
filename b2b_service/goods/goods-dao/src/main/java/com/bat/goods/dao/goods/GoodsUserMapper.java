package com.bat.goods.dao.goods;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.goods.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;

@Mapper
public interface GoodsUserMapper {

    List<GoodsBrandCategoryDO> listGoodsBrandCategoryByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    List<UserGoodsListDO> listUserGoodsList(Map map);

    List<UserGoodsListDO> listGoodsList(Map map);

    List<UserGoodsListDO> listUserGoodsListByClassify(Map map);

    List<UserGoodsListDO> listGoodsListByClassify(Map map);

    List<ClassifyDO> listClassifyByColumnId(@Param("columnId") Integer columnId);

    List<ClassifyDO> listClassifyBySectionId(@Param("sectionId") Integer sectionId);

    List<ClassifyDO> listClassifyByMobileId(@Param("mobileId") Integer mobileId);

    UserGoodsDO selectUserGoodsByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsTagDO> listUserTagByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsParamDO> listUserParamByGoodsId(@Param("goodsId") Integer goodsId);

    List<UserGoodsItemDO> listUserGoodsItemByGoodsIdAndItemId(@Param("goodsId") Integer goodsId,
        @Param("itemId") Integer itemId);

    List<GoodsItemScalePriceDO> listGoodsItemScalePriceByItemIdsAndScalePriceIds(
        @Param("itemIds") List<Integer> itemIds, @Param("scalePriceIds") List<Integer> ScalePriceIds);

    List<GoodsItemScalePriceDO> listGoodsItemRetailPriceByItemIds(@Param("itemIds") List<Integer> itemIds,
        @Param("retailFlag") Short retailFlag);

    List<GoodsStockFlagDO> queryGoodsStockFlagAll();

    Integer queryGoodsIdByItemId(@Param("itemId") Integer itemId);

    Integer updateGoodsStockFlag(@Param("itemId") Integer itemId, @Param("goodsId") Integer goodsId);

    Integer delGoodsStockFlagByIds(@Param("delIds") List<Integer> delIds);
}
