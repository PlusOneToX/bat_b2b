package com.bat.warehouse.api.inStock;

import java.util.List;
import java.util.Set;

import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.inStock.dto.GoodsItemStockFlagCmd;
import com.bat.warehouse.api.inStock.dto.WarehouseInStockPageQry;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventoryCO;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;

public interface WarehouseInStockServiceI {
    PageInfo<GoodsItemInventoryCO> page(WarehouseInStockPageQry warehouseInStockPageQry);

    /**
     * 根据货品id和仓库id列表查询在库库存
     * 
     * @param orderItemId
     * @param warehouseIdList
     * @return
     */
    List<WarehouseInStockDO> listByItemIdAndWarehouseIdList(Integer orderItemId, List<Integer> warehouseIdList);

    /**
     *
     * @param itemIdList
     *            货品id列表
     * @param areaIdList
     *            销售区域id列表
     * @param distributorId
     *            分销商id
     * @param fromRpc
     *            true表示来源rpc远程调用 false非rpc
     * @param notSupportOnWayList
     *            不支持在途的货品id列表
     * @return
     */
    List<GoodsItemInventorySummaryCO> summaryByItemIdListAndAreaIdListAndDistributorId(List<Integer> itemIdList,
        List<Integer> areaIdList, Integer distributorId, Boolean fromRpc, List<Integer> notSupportOnWayList);

    /**
     * 根据区域id列表或者在库仓库id列表查询物料库存信息
     * 
     * @param itemId
     * @param areaIdList
     * @param warehouseIdList
     * @return
     */
    GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdListAndWarehouseIdList(Integer itemId, List<Integer> areaIdList,
        List<Integer> warehouseIdList);

    void update(WarehouseInStockDO warehouseInStockDO);

    /**
     * 根据在库库存id增加在库锁定数量
     * 
     * @param numLock
     * @param key
     * @return
     */
    Integer addNumInWarehouseLockById(Integer numLock, Integer key);

    Response importSql(String url);

    /**
     * 同步ERP库存(根据货品ID列表)
     * 
     * @param itemErpIdList
     *            需要同步的erpId 列表
     * @return
     */
    Response syncStock(List<Long> itemErpIdList);

    /**
     * 全量同步ERP库存
     * 
     * @return
     */
    Response syncStock(String tenantNo);

    /**
     * 后台查询货品库存
     */
    List<GoodsItemInventorySummaryCO> listByAdmin(List<Integer> itemIdList, Integer warehouseId);

    void testLock(Integer index);

    WarehouseInStockDO findByItemIdAndWarehouseNo(Integer itemId, String warehouseNo);

    /**
     * 根据货品id删除库存
     * 
     * @param itemId
     */
    void deleteByItemId(Integer itemId);

    /**
     * 根据仓库id和货品id查询在库数量
     * 
     * @param warehouseId
     * @param itemId
     * @return
     */
    WarehouseInStockDO getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId);

    /**
     * 只保存redis缓存
     * 
     * @param warehouseInStockDO
     */
    void updateRedisCacheOnly(WarehouseInStockDO warehouseInStockDO);

    /**
     * 初始化库存（VMI和在途没有数据、初始化记录、删除已删除的货品库存数据包括缓存数据）
     * 
     * @return
     */
    Response initStock(String tenantNo);

    /**
     * 根据仓库id列表按照货品id分组查询
     * 
     * @param warehouseIdList
     * @return
     */
    List<WarehouseInStockDO> listByWarehouseIdListGroupByItemId(List<Integer> warehouseIdList);

    /**
     * 根据仓库id列和货品编码查询库存数据
     * 
     * @param warehouseIdList
     * @param itemCodeList
     * @return
     */
    List<WarehouseInStockDO> listByWarehouseIdAndItemCodeList(List<Integer> warehouseIdList, List<String> itemCodeList);

    /**
     * 根据条件查询
     * 
     * @param warehouseId
     *            仓库id
     * @return
     */
    List<WarehouseInStockDO> listByCondition(Integer warehouseId);

    /**
     * 批量修改mysql数据（不更新缓存）
     * 
     * @param updateList
     */
    void updateList(List<WarehouseInStockDO> updateList);

    /**
     * 根据redis key查询在库锁库列表
     * 
     * @param instockKeySet
     * @return
     */
    List<WarehouseInStockDO> listInstockByRedisKeySet(Set<String> instockKeySet);

    /**
     * 根据redis key查询在库锁库列表(包容性查询)
     * 
     * @param instockKeySet
     * @return
     */
    List<WarehouseInStockDO> incListInstockByRedisKeySet(Set<String> instockKeySet);

    /**
     * 重置锁库数量
     */
    void resetLockStock(Integer itemId);

    /**
     * 初始化重置锁库数量
     */
    void initResetLockStock();

    /**
     * 初始化货品是否缺货
     */
    void initItemUnderStockFlag();

    /**
     * 修改货品是否缺货
     * 
     * @param goodsItemStockFlagCmd
     */
    void dealwithGoodsUnderStockFlag(String tenantNo, GoodsItemStockFlagCmd goodsItemStockFlagCmd);

    /**
     * 根据商品ids获取库存信息
     * 
     * @param itemIds
     * @return
     */
    List<WarehouseInStockDO> listByItemIds(List<Integer> itemIds);

    /**
     * 根据商品Ids移除库存（包括缓存数据）
     * 
     * @param goodsIds
     * @return
     */
    void deleteByGoodsIds(List<Integer> goodsIds);
}
