package com.bat.warehouse.dao.inStock;

import java.util.List;

import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.warehouse.dao.inStock.co.GoodsItemInventoryCO;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;

@Mapper
public interface WarehouseInStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseInStockDO record);

    void insertList(List<WarehouseInStockDO> records);

    int insertSelective(WarehouseInStockDO record);

    WarehouseInStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseInStockDO record);

    int updateByPrimaryKey(WarehouseInStockDO record);

    List<GoodsItemInventoryCO> listCOByWarehouseIdAndContent(@Param("warehouseId") Integer warehouseId,
        @Param("content") String content);

    // GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdList(@Param("itemId") Integer itemId, @Param("areaIdList")
    // List<Integer> areaIdList);

    List<GoodsItemInventorySummaryCO> summaryByItemIdListAndAreaIdList(@Param("itemIdList") List<Integer> itemIdList,
        @Param("areaIdList") List<Integer> areaIdList);

    List<WarehouseInStockDO> listByItemIdAndWarehouseIdList(@Param("itemId") Integer itemId,
        @Param("warehouseIdList") List<Integer> warehouseIdList);

    List<WarehouseInStockDO> listByGoodsIdsAndWarehouseIdList(@Param("goodsIds") List<Integer> goodsIds,
        @Param("warehouseIdList") List<Integer> warehouseIdList);

    /**
     * 根据货品id和仓库id列表 汇总查询在库数量
     * 
     * @param itemId
     * @param warehouseIdList
     * @return
     */
    GoodsItemInventorySummaryCO summaryByItemIdAndWarehouseIdList(@Param("itemId") Integer itemId,
        @Param("warehouseIdList") List<Integer> warehouseIdList);

    /**
     * 根据货品id和在库仓库id列表 查询货品可下单等各种数量
     * 
     * @param itemId
     * @param areaIdList
     * @return
     */
    GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdList(@Param("itemId") Integer itemId,
        @Param("areaIdList") List<Integer> areaIdList);

    /**
     * 增加在库锁定数量
     * 
     * @param numLockAdd
     * @param id
     * @return
     */
    Integer addNumInWarehouseLockById(@Param("numLockAdd") Integer numLockAdd, @Param("id") Integer id);

    WarehouseInStockDO findByItemIdAndWarehouseNo(@Param("itemId") Integer itemId,
        @Param("warehouseNo") String warehouseNo);

    List<WarehouseInStockDO> listByCondition(@Param("warehouseId") Integer warehouseId);

    WarehouseInStockDO getByWarehouseIdAndItemId(@Param("warehouseId") Integer warehouseId,
        @Param("itemId") Integer itemId);

    List<WarehouseInStockDO>
        listByWarehouseIdListGroupByItemId(@Param("warehouseIdList") List<Integer> warehouseIdList);

    void updateList(@Param("updateList") List<WarehouseInStockDO> updateList);

    List<WarehouseInStockDO> listByWarehouseIdAndItemCodeList(@Param("warehouseIdList") List<Integer> warehouseIdList,
        @Param("itemCodeList") List<String> itemCodeList);

    List<WarehouseInStockDO> listByItemErpIdList(@Param("itemErpIdList") List<Long> itemErpIdList);

    List<WarehouseInStockDO> listByItemIdsAndWarehouseIds(@Param("itemIds") List<Integer> itemIds,
        @Param("warehouseIds") List<Integer> warehouseIds);

    void deleteByIds(@Param("itemIds") List<Integer> itemIds);

    List<Integer> allItemId();

    List<WarehouseInStockDO> getNoItemInStocks(@Param("itemIds") List<Integer> itemIds);
}