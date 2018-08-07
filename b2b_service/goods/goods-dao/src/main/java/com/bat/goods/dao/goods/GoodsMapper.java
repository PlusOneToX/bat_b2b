package com.bat.goods.dao.goods;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.goods.dao.goods.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.*;

@Mapper
public interface GoodsMapper {

    Integer createGoods(GoodsDO goodsDO);

    void createGoodsClassifyRelevanceList(List<GoodsClassifyRelevanceDO> goodsClassifyRelevanceDOList);

    void createGoodsScalePriceRelevanceList(List<GoodsScalePriceRelevanceDO> goodsScalePriceRelevanceDOList);

    void createGoodsScalePriceRelevanceNoList(List<GoodsScalePriceRelevanceNoDO> goodsScalePriceRelevanceNoDOList);

    void createGoodsDepartmentRelevanceList(List<GoodsDepartmentRelevanceDO> goodsDepartmentRelevanceDOList);

    void createGoodsDepartmentRelevanceNoList(List<GoodsDepartmentRelevanceNoDO> goodsDepartmentRelevanceNoDOList);

    void createGoodsAdminRelevanceList(List<GoodsAdminRelevanceDO> goodsAdminRelevanceDOList);

    void createGoodsAdminRelevanceNoList(List<GoodsAdminRelevanceNoDO> goodsAdminRelevanceNoDOList);

    void createGoodsDistributorRelevanceList(List<GoodsDistributorRelevanceDO> goodsDistributorRelevanceDOList);

    void createGoodsDistributorRelevanceNoList(List<GoodsDistributorRelevanceNoDO> goodsDistributorRelevanceNoDOList);

    void createGoodsTagRelevanceList(List<GoodsTagRelevanceDO> goodsTagRelevanceDOList);

    void createGoodsParamRelevanceList(List<GoodsParamRelevanceDO> goodsParamRelevanceDOList);

    void createGoodsItemList(List<GoodsItemDO> goodsItemDOList);

    Integer createGoodsItem(GoodsItemDO goodsItemDO);

    void createGoodsItemBoxList(List<GoodsItemBoxDO> goodsItemBoxDOList);

    Integer createGoodsItemData(GoodsItemDataDO goodsItemDataDO);

    void createGoodsItemSpecsColorList(List<GoodsItemSpecsColorDO> goodsItemSpecsColorDOList);

    void createGoodsItemScalePriceList(List<GoodsItemScalePriceDO> goodsItemScalePriceDOList);

    List<GoodsListDO> listGoods(Map map);

    List<GoodsItemListDO> listGoodsItem(Map map);

    Integer listGoodsItemCount(Map map);

    List<Integer> allGoodsIds();

    List<Integer> allItemIds();

    List<GoodsScopeDO> allGoodsScope();

    List<GoodsItemIdDO> getAllGoodsItemId();

    List<GoodsScopeDO> listGoodsScopeByBrandId(@Param("brandId") Integer brandId);

    GoodsDO getGoodsById(@Param("id") Integer id);

    Integer getGoodsIdByGoodsNo(@Param("goodsNo") String goodsNo);

    List<GoodsItemIdDO> getGoodsItemIdsByItemCodeOrBarCode(@Param("itemCodeOrBarCode") String itemCodeOrBarCode);

    List<Integer> listGoodsClassifyRelevanceId(@Param("goodsId") Integer goodsId);

    List<GoodsClassifyRelevanceDO> listGoodsClassifyRelevanceIdBygoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    List<GoodsTagDO> listGoodsTagByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsTagDO> listGoodsTagByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    List<GoodsParamDO> listGoodsParamByGoodsId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsScalePriceRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsScalePriceRelevanceIdNo(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDepartmentRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDepartmentRelevanceIdNo(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsAdminRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDistributorGroupRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsAdminRelevanceIdNo(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDistributorRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDistributorRelevanceIdNo(@Param("goodsId") Integer goodsId);

    List<GoodsItemDO> listGoodsItemByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsItemDO> listGoodsItemByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    List<GoodsItemDO> listGoodsItemPage(@Param("start") Integer start, @Param("count") Integer count);

    /**
     * 根据上下架状态查找项目终止的货品
     *
     * @param saleStatus
     * @return
     */
    List<GoodsItemDO> listLifeCycleStopGoodsItem(@Param("saleStatus") Short saleStatus);

    /**
     * 根据货品id修改商品上下架状态
     *
     * @param goodsItemIds
     * @param saleStatus
     */
    void updateSaleStatusByItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds,
        @Param("saleStatus") Short saleStatus);

    List<GoodsItemSyncDO> listGoodsItemSync(@Param("start") Integer start, @Param("count") Integer count);

    List<GoodsItemSyncDO> listGoodsItemSyncByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void updateListGoodsItemSync(List<GoodsItemSyncDO> goodsItemSyncDOS);

    List<Integer> listGoodsItemIdByGoodsId(@Param("goodsId") Integer goodsId);

    GoodsItemDO getGoodsItemById(@Param("id") Integer id);

    List<GoodsItemBoxDO> listGoodsItemBoxByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    List<GoodsItemBoxDO> listGoodsItemBoxByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    void updateGoodsItemBoxs(List<GoodsItemBoxDO> goodsItemBoxDOS);

    List<GoodsItemDataDO> listGoodsItemDataByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    List<GoodsItemSpecsColorListDO>
        listGoodsItemSpecsColorListByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    List<GoodsItemSpecsColorListDO> listGoodsItemSpecsColorListByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    List<GoodsItemScalePriceDO>
        listGoodsItemScalePriceByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    List<GoodsItemScalePriceDO> listGoodsItemScalePriceByGoodsItemIdsAndGoodsItemGradeId(
        @Param("goodsItemIds") List<Integer> goodsItemIds, @Param("goodsItemGradeIds") List<Integer> goodsItemGradeIds);

    void updateListGoodsItemScalePrice(List<GoodsItemScalePriceDO> scalePriceDOS);

    List<GoodsItemScalePriceDO> listGoodsItemScalePriceByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    Integer updateGoods(GoodsDO goodsDO);

    Integer updateGoodsItem(GoodsItemDO goodsItemDO);

    void updateGoodsItemList(List<GoodsItemDO> goodsItemDOList);

    Integer listGoodsItemCountBySaleStatusNotInAndGoodsItemId(@Param("goodsItemId") Integer goodsItemId,
        @Param("saleStatus") Short saleStatus);

    void changeGoodsSaleStatusById(@Param("id") Integer id, @Param("saleStatus") Short saleStatus,
        @Param("saleTime") Date saleTime);

    void changeGoodsItemSaleStatusByGoodsId(@Param("goodsId") Integer goodsId, @Param("saleStatus") Short saleStatus);

    void changeGoodsItemSaleStatusById(@Param("id") Integer id, @Param("saleStatus") Short saleStatus);

    void changeGoodsItemAndGoodsSaleStatusByGoodsItemId(@Param("goodsItemId") Integer goodsItemId,
        @Param("saleStatus") Short saleStatus);

    void changeGoodsFreezeStatusById(@Param("id") Integer id, @Param("freezeStatus") Short freezeStatus);

    void deleteGoods(@Param("id") Integer id);

    void deleteGoodsItemByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsItemById(@Param("id") Integer id);

    void deleteGoodsClassifyRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsScalePriceRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsScalePriceRelevanceNoByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsDepartmentRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsDepartmentRelevanceNoByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsAdminRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsAdminRelevanceNoByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsDistributorRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsDistributorRelevanceNoByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsTagRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsTagRelevanceByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsParamRelevanceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsItemBoxByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    void deleteGoodsItemDataByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    void deleteGoodsMinMaxPriceByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsMinMaxPriceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsPromotionPriceByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsPromotionPriceByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsSaleDataByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsSaleDataByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsStockFlagByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsStockFlagByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsStoreColumnByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsStoreColumnByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsStoreMobileByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsStoreMobileByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsStoreSectionByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsStoreSectionByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsTagByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void deleteGoodsTagByGoodsId(@Param("goodsId") Integer goodsId);

    void deleteGoodsItemSpecsColorByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    void deleteGoodsItemScalePriceByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    void deleteGoodsItemBoxByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    void deleteGoodsItemDataByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    void deleteGoodsItemSpecsColorByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    void deleteGoodsItemScalePriceByGoodsItemId(@Param("goodsItemId") Integer goodsItemId);

    void deleteGoodsItemScalePriceByGoodsId(@Param("goodsId") Integer goodsId);

    List<GoodsListStoreDO> listGoodsStoreColumn(@Param("columnId") Integer columnId);

    List<GoodsListStoreDO> listGoodsStoreSection(@Param("sectionId") Integer sectionId);

    List<GoodsListStoreDO> listGoodsStoreMobile(@Param("mobileId") Integer mobileId,
        @Param("moduleType") Short moduleType);

    List<GoodsListDO> listGoodsByClassifyIds(@Param("classifyIds") List<Integer> classifyIds);

    List<GoodsListDO> listGoodsByIds(@Param("ids") List<Integer> ids);

    List<GoodsItemDO> listAllItem();

    List<GoodsItemSimpleDO> listAssignByDistributorId(@Param("distributorId") Integer distributorId,
        @Param("content") String content);

    List<GoodsItemSimpleDO> listByGoodsTypeAndDiyTypeAndItemIdList(@Param("goodsType") Short goodsType,
        @Param("diyType") Short diyType, @Param("itemIdList") List<Integer> itemIdList,
        @Param("content") String content);

    List<Integer> listClearanceGoodsId();

    List<GoodsSaleDataDO> listGoodsSaleDataByGoodsIds(@Param("goodsIds") List<Integer> goodsIds);

    void updateListGoodsSaleData(List<GoodsSaleDataDO> goodsSaleDataDOS);

    void createGoodsSaleDataList(List<GoodsSaleDataDO> goodsSaleDataDOS);

    List<Integer> listGoodsIdsByGoodsItemIds(@Param("goodsItemIds") List<Integer> goodsItemIds);

    List<Integer> listGoodsByIdsAndGoodsItemNotSaleStatus(@Param("goodsIds") List<Integer> goodsIds,
        @Param("saleStatus") Short saleStatus);

    List<Integer> listGoodsByIdsAndGoodsItemSaleStatus(@Param("goodsIds") List<Integer> goodsIds,
        @Param("saleStatus") Short saleStatus);

    void updateGoodsSaleStatusByGoodsIds(@Param("goodsIds") List<Integer> goodsIds,
        @Param("saleStatus") Short saleStatus);

    List<GoodsItemStatusDO> listGoodsItemStatusByItemIdsAndLifeCycle(@Param("itemIds") List<Integer> itemIds,
        @Param("lifeCycle") Short lifeCycle);

    List<Integer> allVisibleProducts();

    List<GoodsDO> goodsAreVisible(@Param("orderGoodsList") List<Integer> orderGoodsList);

    List<Integer> allVisibleBrands();

    BigDecimal queryRetailPriceByItemId(@Param("itemId") Integer itemId);

    List<Integer> goodsAreVisibleByDepId(@Param("departmentID") Integer departmentID);
}
