package com.bat.dubboapi.warehouse.stock.api;

import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.stock.dto.*;

import java.util.List;
import java.util.Set;

public interface WarehouseStockServiceRpc {

    /**
     * 新增商品/货品、初始化货品库存
     *
     * @param goodsMsgRpcDTO
     * @return
     */
    Response initGoodsStock(GoodsMsgRpcDTO goodsMsgRpcDTO);


    /**
     * 根据货品id和区域id列表查询库存
     *
     * @param areaIdList 区域id列表
     * @param itemId     货品id
     * @return
     */
    public Response getStockInventoryByAreaIdListAndItemId(List<Integer> areaIdList, Integer itemId);


    Response updateWarehouseStock(ErpStockChangeCmd erpStockChangeCmd);


    Response deductLockStock(Long orderId, String orderGoodsType, List<OrderItemCountDTO> itemCountDTOList, String warehouseNo);


    /**
     * 删除货品、移除库存
     *
     * @param itemIdList
     * @return
     */
    Response deleteGoodsStock(List<Integer> itemIdList);

    /**
     * 删除货品、移除库存(根据商品Ids)
     *
     * @param goodsIdList
     * @return
     */
    Response deleteGoodsStockByGoodsIds(List<Integer> goodsIdList);

    /**
     * 查询分销商的 库存
     *
     * @param itemIdList          货品id列表、必传
     * @param areaIdList          销售区域id列表、不传也可以
     * @param distributorId       分销商id
     * @param notSupportOnWayList 不支持在途的货品id列表 （id必须要正在itemIdList里面）若为null表示itemIdList都支持在途库存
     * @return inStockUsableCount在库可下单量  onWayUsableCount 在途可下单量
     */
    Response<List<GoodsItemInventorySummaryRpcDTO>> summaryByItemIdListAndAreaIdListAndDistributorId(List<Integer> itemIdList, List<Integer> areaIdList,
                                                                                                     Integer distributorId, List<Integer> notSupportOnWayList);

    /**
     * 锁定库存、返回汇总情况
     *
     * @param itemCountList       货品id列表、下单数量、库存类型 必传
     * @param areaIdList          销售区域id列表、不传也可以
     * @param distributorId       分销商id
     * @param notSupportOnWayList 不支持在途的货品id列表 （id必须要正在itemIdList里面）若为null表示itemIdList都支持在途库存
     * @return
     */
    Response<List<ItemStockLockDTO>> summaryLockStock(List<GoodsItemCountDTO> itemCountList, List<Integer> areaIdList,
                                                      Integer distributorId, List<Integer> notSupportOnWayList);

    /**
     * 反锁库存（锁库之后、处理业务失败、解除库存锁定）
     *
     * @param itemStockLockDTOList
     * @return
     */
    Response unLockStock(List<ItemStockLockDTO> itemStockLockDTOList);

    /**
     * 解锁和锁定库存、返回锁定库存的汇总情况
     *
     * @param itemCountList       货品id列表、下单数量、库存类型 必传
     * @param areaIdList          销售区域id列表、不传也可以
     * @param distributorId       分销商id
     * @param notSupportOnWayList 不支持在途的货品id列表 （id必须要正在itemIdList里面）若为null表示itemIdList都支持在途库存
     * @param unLockItemList      解锁的货品列表
     * @return
     */
    Response<List<ItemStockLockDTO>> summaryLockStockAndUnLockStock(List<GoodsItemCountDTO> itemCountList, List<Integer> areaIdList,
                                                                    Integer distributorId, List<Integer> notSupportOnWayList, List<ItemStockLockDTO> unLockItemList);

    /**
     * redis同步库存数据到mysql
     *
     * @return
     */
    Response stockRedisSyncMysqlJobHandler();

    /**
     * ERP同步库存到B2B
     *
     * @return
     */
    Response stockErpSyncB2BJobHandler();

    /**
     * 根据仓库id和货品id查询在库库存
     *
     * @param warehouseId
     * @param itemId
     * @return
     */
    Response<WarehouseInStockRpcDTO> getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId);

    /**
     * 根据货品id查询VMI库存
     *
     * @param itemId
     * @return
     */
    Response<GoodsVmiStockRpcDTO> getVmiByItemId(Integer itemId);

    /**
     * 根据货品id查询VMI库存
     *
     * @param itemIdSet
     * @return
     */
    Response<List<GoodsVmiStockRpcDTO>> listVmiByItemIdList(Set<Integer> itemIdSet);

//    /**
//     * 根据货品id查询在途库存
//     * @param itemId
//     * @return
//     */
//    Response<GoodsOnWayStockRpcDTO> getOnWayByItemId(Integer itemId);

// /**
// * 根据货品id列表查询在途库存
// * @param itemIdSet
// * @return
// */
// Response<List<GoodsOnWayStockRpcDTO>> listOnWayByItemIdList(Set<Integer> itemIdSet);

    /**
     * 根据在库rediskey来查询在库库存列表
     *
     * @param instockKeySet warehouseId+"_"+itemId
     * @return
     */
    Response<List<WarehouseInStockRpcDTO>> listInstockByRedisKeySet(Set<String> instockKeySet);

    /**
     * 根据货品ids获取库存信息
     *
     * @param itemIds
     * @return
     */
    Response<WarehouseStockInfoRpcDTO> getStockByItemIds(List<Integer> itemIds);


}
