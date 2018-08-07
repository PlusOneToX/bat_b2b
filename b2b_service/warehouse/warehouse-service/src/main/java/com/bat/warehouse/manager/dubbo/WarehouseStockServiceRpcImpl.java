package com.bat.warehouse.manager.dubbo;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.manager.common.error.*;
import com.bat.warehouse.manager.dubbo.convertor.WarehouseStockDubboConvertor;
import com.bat.warehouse.manager.dubbo.executor.WarehouseStockDubboCmdExe;
import com.bat.warehouse.manager.dubbo.validtor.WarehouseStockDubboValidator;
import com.bat.warehouse.manager.inStock.executor.WarehouseStockSyncCmdExe;
import com.bat.warehouse.manager.message.MessageSendService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorBusinessRpcDTO;
import com.bat.dubboapi.goods.stock.api.GoodsStockFlagServiceRpc;
import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.*;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseDubboApiException;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.BeanUtils;
import com.bat.warehouse.api.base.util.MessageUtils;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseDubboApiErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseInStockErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseNameErrorCode;

@DubboService
public class WarehouseStockServiceRpcImpl implements WarehouseStockServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockServiceRpcImpl.class);

    @Autowired
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    @Autowired
    private WarehouseInStockServiceI warehouseInStockServiceI;

    @Autowired
    private WarehouseServiceI warehouseServiceI;

    @DubboReference(check = false, retries = 0, timeout = 6000)
    private DistributorServiceRpc distributorServiceRpc;

    @Autowired
    private WarehouseStockDubboCmdExe warehouseStockDubboCmdExe;

    @Autowired
    private WarehouseStockSyncCmdExe warehouseStockSyncCmdExe;

    @DubboReference(retries = 0, check = false, timeout = 6000)
    private GoodsStockFlagServiceRpc goodsStockFlagServiceRpc;

    @Autowired
    private MessageSendService messageSendService;

    /**
     * 初始化库存
     *
     * @param goodsMsgRpcDTO
     * @return
     */
    @Override
    @Transactional
    public Response initGoodsStock(GoodsMsgRpcDTO goodsMsgRpcDTO) {
        LOGGER.info("初始化库存：{}", JSON.toJSONString(goodsMsgRpcDTO));
        try {
            // 参数校验、得到货品的ERPid列表
            WarehouseStockDubboValidator.checkItemErp(goodsMsgRpcDTO);
            messageSendService.initGoodsStock(goodsMsgRpcDTO);
            return Response.buildSuccess();
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            LOGGER.error("货品初始化库存异常：{}", e.getMsg());
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("货品库存初始化系统异常：{}", e.getMessage());
            return Response.buildFailure(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_INIT_EXCEPTION,
                MessageUtils.get(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_INIT_EXCEPTION));
        }

    }

    @Override
    public Response getStockInventoryByAreaIdListAndItemId(List<Integer> areaIdList, Integer itemId) {
        if (itemId == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        GoodsItemInventorySummaryCO summaryCO =
            warehouseInStockServiceI.summaryByItemIdAndAreaIdListAndWarehouseIdList(itemId, areaIdList, null);
        return Response.of(summaryCO);
    }

    @Override
    @Transactional
    public Response updateWarehouseStock(ErpStockChangeCmd erpStockChangeCmd) {
        LOGGER.info("erp------>库存变更接口,请求参数：" + JSON.toJSONString(erpStockChangeCmd));
        try {
            return warehouseStockDubboCmdExe.updateWarehouseStock(erpStockChangeCmd);
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("erp------>库存变更接口系统异常,{}", e.getMessage());
            return Response.buildFailure(WarehouseDubboApiErrorCode.W_WAREHOUSE_STOCK_CHANGE_EXCEPTION,
                MessageUtils.get(WarehouseDubboApiErrorCode.W_WAREHOUSE_STOCK_CHANGE_EXCEPTION));
        }
    }

    /**
     * 没有使用
     *
     * @param orderId
     * @param orderGoodsType
     * @param itemCountDTOList
     * @param warehouseNo
     * @return
     */
    @Override
    @Transactional
    public Response deductLockStock(Long orderId, String orderGoodsType, List<OrderItemCountDTO> itemCountDTOList,
        String warehouseNo) {
        LOGGER.info("erp发货---库存解锁：" + JSON.toJSONString(itemCountDTOList) + ",orderId=" + orderId);
        /*Iterator<Map.Entry<String, ErpDeliverOrderDetailCmd>> iterator= deliverOrderMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ErpDeliverOrderDetailCmd> entry = iterator.next();
            ErpDeliverOrderDetailCmd erpDeliverOrderDetailCmd = entry.getValue();
        
        }*/

        return null;
    }

    private void checkInStockCount(List<OrderItemCountDTO> itemCountDTOList, Boolean isInventoryEnough,
        List<Integer> warehouseIdList) {
        if (!isInventoryEnough) {
            return;
        }
        itemCountDTOList.stream().forEach(orderItemCountDTO -> {
            GoodsItemInventorySummaryCO summaryCO = warehouseInStockServiceI
                .summaryByItemIdAndAreaIdListAndWarehouseIdList(orderItemCountDTO.getItemId(), null, warehouseIdList);
            if (summaryCO.getInStockUsableCount() < orderItemCountDTO.getCount()) {// 判断库存数量是否异常，异常情况回滚数据
                // 4.0.7加上是否库存足够判断
                throw WarehouseDubboApiException.buildException(
                    MessageUtils.get("user.warehouse.OrderItemStockNotEnoughMsg1") + orderItemCountDTO.getItemCode()
                        + MessageUtils.get("user.warehouse.OrderItemStockNotEnoughMsg2"));
            }
        });
    }

    private void addToResultList(List<OrderGoodsVmiDTO> resultVmiDTOList, Long orderGoodsId, Integer orderItemCount) {
        OrderGoodsVmiDTO orderGoodsVmiDTO = new OrderGoodsVmiDTO();
        orderGoodsVmiDTO.setOrderGoodsId(orderGoodsId);
        orderGoodsVmiDTO.setVmiCount(orderItemCount);
        resultVmiDTOList.add(orderGoodsVmiDTO);
    }

    @Override
    public Response deleteGoodsStock(List<Integer> itemIdList) {
        LOGGER.info("删除货品库存：" + JSON.toJSONString(itemIdList));
        itemIdList.stream().forEach(itemId -> {
            warehouseInStockServiceI.deleteByItemId(itemId);
            goodsVmiStockServiceI.deleteByItemId(itemId);
        });
        // 删除在库库存标记列表
        goodsStockFlagServiceRpc.deleteByItemIdList(itemIdList);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteGoodsStockByGoodsIds(List<Integer> goodsIds) {
        try {
            warehouseStockDubboCmdExe.deleteGoodsStockByGoodsIds(goodsIds);
            return Response.buildSuccess();
        } catch (Exception e) {
            return Response.buildFailure(WarehouseDubboServiceErrorCode.DUBBO_WAREHOUSE_STOCK_DELETE_ERROR,
                MessageUtils.get(WarehouseDubboServiceErrorCode.DUBBO_WAREHOUSE_STOCK_DELETE_ERROR));
        }
    }

    @Override
    public Response<List<GoodsItemInventorySummaryRpcDTO>> summaryByItemIdListAndAreaIdListAndDistributorId(
        List<Integer> itemIdList, List<Integer> areaIdList, Integer distributorId, List<Integer> notSupportOnWayList) {
        List<GoodsItemInventorySummaryCO> list =
            warehouseInStockServiceI.summaryByItemIdListAndAreaIdListAndDistributorId(itemIdList, areaIdList,
                distributorId, true, notSupportOnWayList);
        if (list == null || list.size() == 0) {
            return Response.buildSuccess();
        }
        List<GoodsItemInventorySummaryRpcDTO> resultList =
            BeanUtils.copyList(list, GoodsItemInventorySummaryRpcDTO.class);
        return Response.of(resultList);
    }

    /**
     * 锁定库存、返回汇总情况
     *
     * @param itemCountList
     *            货品id列表、下单数量、库存类型 必传
     * @param areaIdList
     *            销售区域id列表、不传也可以
     * @param distributorId
     *            分销商id
     * @param notSupportOnWayList
     *            不支持在途的货品id列表 （id必须要正在itemIdList里面）若为null表示itemIdList都支持在途库存
     * @return
     */
    @Override
    @Transactional
    public Response<List<ItemStockLockDTO>> summaryLockStock(List<GoodsItemCountDTO> itemCountList,
        List<Integer> areaIdList, Integer distributorId, List<Integer> notSupportOnWayList) {
        // 分布式锁列表、锁库完毕要释放
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        try {
            // 校验锁库参数
            WarehouseStockDubboValidator.checkLockParam(itemCountList, areaIdList, distributorId, notSupportOnWayList);
            if (CollectionUtils.isEmpty(areaIdList)) {
                // 假如没有传分销商所属销售区域id、要去查询,要去分销商去拿;
                com.bat.dubboapi.distributor.common.Response<DistributorBusinessRpcDTO> rpcDTOResponse =
                    distributorServiceRpc.getDistributorBusiness(distributorId);
                DistributorBusinessRpcDTO distributorBusinessRpcDTO = rpcDTOResponse.getData();
                if (distributorBusinessRpcDTO == null) {
                    throw WarehouseDubboApiException
                        .buildException(WarehouseCommonErrorCode.COMMON_DISTRIBUTOR_ID_ERROR);
                }
                areaIdList = distributorBusinessRpcDTO.getSalesAreaIds();
                if (CollectionUtils.isEmpty(areaIdList)) {
                    throw WarehouseDubboApiException
                        .buildException(WarehouseInStockErrorCode.W_DISTRIBUTOR_SALE_AREA_NULL);
                }
            }
            List<WarehouseDO> warehouseDOList =
                warehouseServiceI.listByAreaIdListAndOpenFlag(areaIdList, WarehouseCommonConstant.COMMON_OPEN_FLAG_YES);
            if (CollectionUtils.isEmpty(warehouseDOList)) {
                throw WarehouseDubboApiException
                    .buildException(WarehouseInStockErrorCode.W_DISTRIBUTOR_RELEVANCE_WAREHOUSE_NULL);
            }
            List<Integer> warehouseIdList =
                warehouseDOList.stream().map(WarehouseDO::getId).collect(Collectors.toList());
            List<ItemStockLockDTO> resultList = new ArrayList<>();
            List<WarehouseInStockDO> allInStockDOList = new ArrayList<>();
            List<GoodsVmiStockDO> allGoodsVmiStockDOList = new ArrayList<>();
            List<Integer> itemIds = new ArrayList<>();
            itemCountList.forEach(goodsItemCountDTO -> {
                if (!itemIds.contains(goodsItemCountDTO.getItemId())) {
                    itemIds.add(goodsItemCountDTO.getItemId());
                }
                // 获取redis锁
                // 在库数量
                int inStockCount =
                    goodsItemCountDTO.getInStockCount() == null ? 0 : goodsItemCountDTO.getInStockCount();
                // 下单的在途数量
                int onWayCount = goodsItemCountDTO.getOnWayCount() == null ? 0 : goodsItemCountDTO.getOnWayCount();
                // 锁库（货品id粒度）、返回在库库存列表 （包含在途库存）
                List<WarehouseInStockDO> warehouseInStockDOList = warehouseStockDubboCmdExe
                    .LockAndReturnInStockList(goodsItemCountDTO.getItemId(), autoReleaseLockList, warehouseIdList);
                // 在途（暂停使用）
                // GoodsOnWayStockDO goodsOnWayStockDO =
                // goodsOnWayStockServiceI.getByItemId(goodsItemCountDTO.getItemId());
                // goodsItemStockFlagDetailCmd.setGoodsOnWayStockDO(goodsOnWayStockDO);
                // VMI
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(goodsItemCountDTO.getItemId());
                // 库存数据，第一个数据为在库可下单量，第二个数据为在途可下单量
                List<Integer> stockQuantity =
                    WarehouseStockDubboConvertor.calculateInStockUsable(warehouseInStockDOList, goodsVmiStockDO);
                LOGGER.info("在库可下单量：{} ,在途可下单量,{}", stockQuantity.get(0), stockQuantity.get(1));
                Integer inStockUsableQuantity = stockQuantity.get(0);
                Integer onWayUsableQuantity = stockQuantity.get(1);
                // 判断在库库存
                if (inStockCount > 0 && !goodsItemCountDTO.getSupportOversoldFlag()
                    && inStockCount > inStockUsableQuantity) {
                    throw WarehouseDubboApiException.buildException(
                        warehouseInStockDOList.get(0).getItemName() + "在库库存不足、在库可下单量只有:" + inStockUsableQuantity);
                }
                // 判断在途库存
                if (onWayCount > 0 && goodsItemCountDTO.getSupportOversoldFlag() && onWayCount > onWayUsableQuantity) {
                    throw WarehouseDubboApiException.buildException(
                        warehouseInStockDOList.get(0).getItemName() + "在途库存不足、在途可下单量只有:" + onWayUsableQuantity);
                }
                // 返回的结果
                ItemStockLockDTO itemStockLockDTO = new ItemStockLockDTO();
                itemStockLockDTO.setItemId(goodsItemCountDTO.getItemId());
                // VMI锁定记录
                VminStockLockDTO vminStockLockDTO = new VminStockLockDTO();
                // 在途在库锁库
                List<WarehouseInStockLockDTO> inStockLockDTOList = new ArrayList<>();
                List<WarehouseInStockLockDTO> onWayLockDTOList = new ArrayList<>();
                lockInAndOnWayStock(goodsItemCountDTO, itemStockLockDTO, inStockCount, onWayCount, vminStockLockDTO,
                    inStockLockDTOList, onWayLockDTOList, warehouseInStockDOList, goodsVmiStockDO, allInStockDOList,
                    allGoodsVmiStockDOList);
                if (inStockLockDTOList.size() > 0) {
                    itemStockLockDTO.setInStockLockDTOList(inStockLockDTOList);
                }
                if (onWayLockDTOList.size() > 0) {
                    itemStockLockDTO.setOnWayLockDTOList(onWayLockDTOList);
                }
                resultList.add(itemStockLockDTO);
            });
            updateRedisCache(allInStockDOList, allGoodsVmiStockDOList);
            // 释放锁
            warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
            // 刷新货品库存是否有货数据
            messageSendService.freshItemUnderStockFlag(itemIds);
            return Response.of(resultList);
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(e.getCode(), e.getMessage());
        } catch (WarehouseException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("锁库系统异常，{}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(WarehouseCommonErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(WarehouseCommonErrorCode.SYSTEM_EXCEPTION));
        } finally {
            // 最终都需释放锁
            if (!CollectionUtils.isEmpty(autoReleaseLockList)) {
                warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
            }
        }
    }

    /**
     * 更新库存缓存数据（在库在途vmi）
     */
    private void updateRedisCache(List<WarehouseInStockDO> allInStockDOList,
        List<GoodsVmiStockDO> allGoodsVmiStockDOList) {
        // 更新在库在途缓存数据
        if (!CollectionUtils.isEmpty(allInStockDOList)) {
            allInStockDOList.forEach(warehouseInStockDO -> {
                warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
            });
        }
        // 更新VMI缓存数据
        if (!CollectionUtils.isEmpty(allGoodsVmiStockDOList)) {
            allGoodsVmiStockDOList.forEach(goodsVmiStockDO -> {
                goodsVmiStockServiceI.updateRedisCacheOnly(goodsVmiStockDO);
            });
        }
    }

    /**
     * 锁定在库
     *
     * @param goodsItemCountDTO
     * @param itemStockLockDTO
     * @param inStockCount
     * @param vminStockLockDTO
     * @param warehouseInStockDOList
     * @param goodsVmiStockDO
     * @return
     */
    private void lockInAndOnWayStock(GoodsItemCountDTO goodsItemCountDTO, ItemStockLockDTO itemStockLockDTO,
        Integer inStockCount, Integer onWayCount, VminStockLockDTO vminStockLockDTO,
        List<WarehouseInStockLockDTO> inStockLockDTOList, List<WarehouseInStockLockDTO> onWayLockDTOList,
        List<WarehouseInStockDO> warehouseInStockDOList, GoodsVmiStockDO goodsVmiStockDO,
        List<WarehouseInStockDO> allInStockDOList, List<GoodsVmiStockDO> allGoodsVmiStockDOList) {
        AtomicReference<Integer> finalInStockCount = new AtomicReference(inStockCount);
        AtomicReference<Integer> finalOnWayCount = new AtomicReference(onWayCount);
        Date updateTime = new Date();
        // 1.处理在库在途库存数据
        warehouseInStockDOList.forEach(warehouseInStockDO -> {
            // 在途在库数量处理完，不在循环处理库存数据
            if (finalInStockCount.get() == 0 && finalOnWayCount.get() == 0) {
                return;
            }
            // 在库可下单量=在库数量-在库锁定数量-预留数量-在途锁定数量
            int inStockQuantity = warehouseInStockDO.getNumInWarehouse() - warehouseInStockDO.getNumInWarehouseLock()
                - warehouseInStockDO.getNumReserved() - warehouseInStockDO.getNumOnWayLock();
            // 在途可下单量计算
            // 当inStockQuantity <0 时,在途可下单量 = 在途数量 + inStockQuantity
            // 当inStockQuantity >0 时,在途可下单量 = 在途数量
            Integer onWayQuantity = 0;
            if (inStockQuantity < 0) {
                onWayQuantity = warehouseInStockDO.getNumOnWay() + inStockQuantity;
            } else {
                onWayQuantity = warehouseInStockDO.getNumOnWay();
            }
            // 该仓库在库要锁的数量
            Integer inStockLockCount = 0;
            // 该仓库在途要锁的数量
            Integer onWayLockCount = 0;
            // 处理在库数量
            if (finalInStockCount.get() > 0 && inStockQuantity > 0) {
                if (inStockQuantity >= finalInStockCount.get()) {// 在库数量充足
                    inStockLockCount = finalInStockCount.get();
                    finalInStockCount.set(0);
                } else if (inStockQuantity > 0) {// 在库数量不充足
                    inStockLockCount = inStockQuantity;
                    finalInStockCount.set(finalInStockCount.get() - inStockQuantity);
                }
            }
            // 处理在途数量
            if (finalOnWayCount.get() > 0 && onWayQuantity > 0) {
                if (onWayQuantity >= finalOnWayCount.get()) {// 在库数量充足
                    onWayLockCount = finalOnWayCount.get();
                    finalOnWayCount.set(0);
                } else if (onWayQuantity > 0) {// 在库数量不充足
                    onWayLockCount = onWayQuantity;
                    finalOnWayCount.set(finalOnWayCount.get() - inStockQuantity);
                }
            }
            if (inStockLockCount > 0) {
                // 在库锁
                warehouseInStockDO.setNumInWarehouseLock(warehouseInStockDO.getNumInWarehouseLock() + inStockLockCount);
                // 在库锁库记录
                WarehouseInStockLockDTO inStockLockDTO = new WarehouseInStockLockDTO();
                inStockLockDTO.setStockId(warehouseInStockDO.getId());
                inStockLockDTO.setNumInWarehouseLock(inStockLockCount);
                inStockLockDTO.setWarehouseId(warehouseInStockDO.getWarehouseId());
                inStockLockDTOList.add(inStockLockDTO);
            }
            if (onWayLockCount > 0) {
                // 在途锁
                warehouseInStockDO.setNumOnWayLock(warehouseInStockDO.getNumOnWayLock() + onWayLockCount);
                // 在途锁库记录
                WarehouseInStockLockDTO onWayLockDTO = new WarehouseInStockLockDTO();
                onWayLockDTO.setStockId(warehouseInStockDO.getId());
                onWayLockDTO.setNumOnWayLock(onWayLockCount);
                onWayLockDTO.setWarehouseId(warehouseInStockDO.getWarehouseId());
                onWayLockDTOList.add(onWayLockDTO);
            }
            // 在库或在途锁有更新情况，需更新缓存库存数据
            if (inStockLockCount > 0 || onWayLockCount > 0) {
                warehouseInStockDO.setUpdateTime(updateTime);
                if (!allInStockDOList.contains(warehouseInStockDO)) {
                    allInStockDOList.add(warehouseInStockDO);
                }
            }
        });
        // 2.购买的在库数量未处理完时，查看VMI数量是否可用（VMI库存数量为共享库存）
        if (finalInStockCount.get() > 0 && goodsVmiStockDO != null) {
            int vmiStockQuantity = goodsVmiStockDO.getNumVmi() - goodsVmiStockDO.getNumLock();
            Integer vmiStockLockCount = 0;
            if (vmiStockQuantity > 0) {
                if (vmiStockQuantity >= finalInStockCount.get()) {// vmi数量充足
                    vmiStockLockCount = finalInStockCount.get();
                    finalInStockCount.set(0);
                } else if (vmiStockQuantity > 0) {// vmi数量不充足
                    vmiStockLockCount = vmiStockQuantity;
                    finalInStockCount.set(finalInStockCount.get() - vmiStockQuantity);
                }
            }
            if (vmiStockLockCount > 0) {
                goodsVmiStockDO.setNumLock(goodsVmiStockDO.getNumLock() + vmiStockLockCount);
                goodsVmiStockDO.setUpdateTime(updateTime);
                if (!allGoodsVmiStockDOList.contains(goodsVmiStockDO)) {
                    allGoodsVmiStockDOList.add(goodsVmiStockDO);
                }
                // VMI数量锁库记录
                vminStockLockDTO.setLockNum(vmiStockLockCount);
                vminStockLockDTO.setStockId(goodsVmiStockDO.getId());
                // 设置优先级最高的库存id
                vminStockLockDTO.setWarehouseId(warehouseInStockDOList.get(0).getWarehouseId());
                itemStockLockDTO.setVmiLock(vminStockLockDTO);
            }
        }
        // 3.购买的数量未处理完成且允许超卖情况，需锁到第一个仓库的在途数量上
        if ((finalInStockCount.get() > 0 || finalOnWayCount.get() > 0) && goodsItemCountDTO.getSupportOversoldFlag()) {
            Integer overStockLock = finalInStockCount.get() + finalOnWayCount.get();
            WarehouseInStockDO warehouseInStockDO = warehouseInStockDOList.get(0);
            Optional<WarehouseInStockLockDTO> first = inStockLockDTOList.stream()
                .filter(inStockLockDTO -> inStockLockDTO.getStockId().equals(warehouseInStockDO.getId())).findFirst();
            WarehouseInStockLockDTO inStockLockDTO = null;
            if (!first.isPresent()) {
                inStockLockDTO = new WarehouseInStockLockDTO();
                inStockLockDTO.setStockId(warehouseInStockDO.getId());
                inStockLockDTO.setNumInWarehouseLock(overStockLock);
                inStockLockDTO.setWarehouseId(warehouseInStockDO.getWarehouseId());
                inStockLockDTOList.add(inStockLockDTO);
            } else {
                inStockLockDTO = first.get();
                if (inStockLockDTO.getNumInWarehouseLock() != null) {
                    inStockLockDTO.setNumInWarehouseLock(inStockLockDTO.getNumInWarehouseLock() + overStockLock);
                } else {
                    inStockLockDTO.setNumInWarehouseLock(overStockLock);
                }
            }
            // 在途锁
            if (warehouseInStockDO.getNumInWarehouseLock() != null) {
                warehouseInStockDO.setNumInWarehouseLock(warehouseInStockDO.getNumInWarehouseLock() + overStockLock);
            } else {
                warehouseInStockDO.setNumInWarehouseLock(overStockLock);
            }
            warehouseInStockDO.setUpdateTime(updateTime);
            if (!allInStockDOList.contains(warehouseInStockDO)) {
                allInStockDOList.add(warehouseInStockDO);
            }
        }
    }

    /**
     * 锁定在途（与锁在库一样的逻辑（去除了VMI逻辑）） 注释里的在库 都不是正确的意思 是在途
     *
     * @param warehouseDOList
     * @param goodsItemCountDTO
     *            (用于判断是否支持超卖 没有实际意义)
     * @param itemStockLockDTO
     * @param inStockCount
     *            （订单在途货品数量）
     * @param vminStockLockDTO
     * @param warehouseInStockDOList
     * @param goodsVmiStockDO
     * @return
     */
    private List<WarehouseInStockLockDTO> lockOnWayStock(List<WarehouseDO> warehouseDOList,
        GoodsItemCountDTO goodsItemCountDTO, ItemStockLockDTO itemStockLockDTO, Integer inStockCount,
        VminStockLockDTO vminStockLockDTO, List<WarehouseInStockDO> warehouseInStockDOList,
        GoodsVmiStockDO goodsVmiStockDO) {
        // // 在途 分仓库
        // WarehouseInStockLockDTO inStockLockDTO = inStockLockDTOMap
        // .getOrDefault(warehouseInStockDO.getId(), new WarehouseInStockLockDTO(0, 0, 0, 0));
        // inStockLockDTO.setStockId(warehouseInStockDO.getId());
        // inStockLockDTO.setWarehouseId(warehouseInStockDO.getWarehouseId());
        // inStockLockDTO.setNumOnWayLock(inStockLockDTO.getNumOnWayLock() + Math.max(onWayCount, 0));
        // inStockLockDTO
        // .setNumInWarehouseLock(inStockLockDTO.getNumInWarehouseLock() + Math.max(lockCount, 0));

        // goodsOnWayStockDO.setNumLock(goodsOnWayStockDO.getNumLock() + onWayCount);
        // goodsOnWayStockDO.setUpdateTime(new Date());
        // goodsOnWayStockServiceI.updateRedisCacheOnly(goodsOnWayStockDO);
        // warehouse.setNumOnWayLock(warehouse.getNumOnWayLock() + onWayCount);
        // OnWayStockLockDTO onWayStockLockDTO = new OnWayStockLockDTO();
        // onWayStockLockDTO.setStockId(warehouse.getId());
        // onWayStockLockDTO.setWarehouseId(warehouse.getWarehouseId());
        // onWayStockLockDTO.setLockNum(onWayCount);
        // itemStockLockDTO.setOnWayLock(onWayStockLockDTO);
        List<WarehouseInStockLockDTO> inStockLockDTOList = new ArrayList<>();
        for (int z = 0; z < warehouseInStockDOList.size(); z++) {
            // 是否最后一个仓库
            boolean lastWarehouseFlag = z == warehouseDOList.size() - 1;
            WarehouseInStockDO warehouseInStockDO = warehouseInStockDOList.get(z);
            // 该仓库可用数量(在途)
            int warehouseUsable = warehouseInStockDO.getNumOnWay() - warehouseInStockDO.getNumOnWayLock();
            if (warehouseUsable <= 0 && !lastWarehouseFlag) {
                // 该仓库不足、且非最后一个、先不扣减这个仓库库存
                continue;
            }
            // 该仓库要锁的数量
            Integer lockCount = null;
            // 1、该仓库的可下单量大于用户下单量
            if (warehouseUsable >= inStockCount) {
                lockCount = inStockCount;
                // 货品剩余的待锁定下单量
                inStockCount = 0;
            }
            // 2、该仓库的可下单量小于用户的可下单量、且非最后一个仓库
            if (lockCount == null && !lastWarehouseFlag) {
                // 锁这个仓库最大的可下单量
                lockCount = warehouseUsable;
                // 货品剩余的待锁定下单量
                inStockCount = inStockCount - lockCount;
            }
            lockCount = lockCount == null ? 0 : lockCount;
            // 集合引用类型变量 之前已经更新过在库锁
            warehouseInStockDO.setNumOnWayLock(warehouseInStockDO.getNumOnWayLock() + lockCount);
            warehouseInStockDO.setUpdateTime(new Date());
            if (lockCount > 0) {
                // 大于0才加
                WarehouseInStockLockDTO inStockLockDTO = new WarehouseInStockLockDTO();
                inStockLockDTO.setStockId(warehouseInStockDO.getId());
                inStockLockDTO.setNumOnWayLock(lockCount);
                inStockLockDTO.setWarehouseId(warehouseInStockDO.getWarehouseId());
                inStockLockDTOList.add(inStockLockDTO);
            }
            // 在库更新过一遍 在途又更新一次 应该是没有问题
            warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
            if (inStockCount == 0) {
                // 满足库存、跳出循环
                break;
            }
        }
        return inStockLockDTOList;
    }

    /**
     * 解锁锁定库存
     *
     * @param itemStockLockDTOList
     * @return
     */
    @Override
    public Response unLockStock(List<ItemStockLockDTO> itemStockLockDTOList) {
        // 分布式锁列表、锁库完毕要释放
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        try {
            // 在途在库数据合并
            mergeDto(itemStockLockDTOList);
            WarehouseStockDubboValidator.checkUnLockParam(itemStockLockDTOList);
            // 设置在库的id（如果传过来是空的、这个有可能是redis存的时候id为空）
            warehouseStockDubboCmdExe.setInStockId(itemStockLockDTOList);
            Date updateTime = new Date();
            List<WarehouseInStockDO> warehouseInStockDOList = new ArrayList<>();
            List<GoodsVmiStockDO> goodsVmiStockDOList = new ArrayList<>();
            List<Integer> itemIds = new ArrayList<>();
            itemStockLockDTOList.forEach(itemStockLockDTO -> {
                Integer itemId = itemStockLockDTO.getItemId();
                if (!itemIds.contains(itemId)) {
                    itemIds.add(itemId);
                }
                VminStockLockDTO vmiLock = itemStockLockDTO.getVmiLock();
                // 在途在库同一仓库已经合并
                List<WarehouseInStockLockDTO> inStockLockDTOList = itemStockLockDTO.getInStockLockDTOList();
                List<Integer> warehouseIdList = new ArrayList<>();
                // 找到在库的库存id
                if (!CollectionUtils.isEmpty(inStockLockDTOList)) {
                    warehouseIdList.addAll(inStockLockDTOList.stream().map(WarehouseInStockLockDTO::getWarehouseId)
                        .distinct().collect(Collectors.toList()));
                }
                // 定义库存id和解锁的数量 key warehouse_in_stock主键、value解锁数量
                Map<Integer, Integer> inStockMap = new HashMap<>();
                Map<Integer, Integer> onWayLockMap = new HashMap<>();
                // 锁定、返回需要解锁的值
                List<WarehouseInStockDO> inStockDOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(warehouseIdList)) {
                    inStockDOS = warehouseStockDubboCmdExe.LockAndReturnInStockList(itemId, autoReleaseLockList,
                        warehouseIdList);
                }
                if (!CollectionUtils.isEmpty(inStockLockDTOList)) {
                    // 在库在途解锁
                    inStockLockDTOList.forEach(warehouseInStockLockDTO -> {
                        if (warehouseInStockLockDTO.getNumInWarehouseLock() != null
                            && warehouseInStockLockDTO.getNumInWarehouseLock() > 0) {
                            // 可能同一个库存、两个明细、解锁数量相加
                            Integer count = inStockMap.getOrDefault(warehouseInStockLockDTO.getStockId(), 0);
                            count = count + warehouseInStockLockDTO.getNumInWarehouseLock();
                            inStockMap.put(warehouseInStockLockDTO.getStockId(), count);
                        }
                        if (warehouseInStockLockDTO.getNumOnWayLock() != null
                            && warehouseInStockLockDTO.getNumOnWayLock() > 0) {
                            Integer count = onWayLockMap.getOrDefault(warehouseInStockLockDTO.getStockId(), 0);
                            count = count + warehouseInStockLockDTO.getNumOnWayLock();
                            onWayLockMap.put(warehouseInStockLockDTO.getStockId(), count);
                        }
                    });
                    if (!CollectionUtils.isEmpty(inStockDOS)) {
                        inStockDOS.stream().forEach(warehouseInStockDO -> {
                            Integer unLockInStockCount = inStockMap.getOrDefault(warehouseInStockDO.getId(), 0);
                            Integer unLockOnWayCount = onWayLockMap.getOrDefault(warehouseInStockDO.getId(), 0);
                            // 在库解锁
                            Integer lockInStockCount = warehouseInStockDO.getNumInWarehouseLock() - unLockInStockCount;
                            if (lockInStockCount > 0) {
                                warehouseInStockDO.setNumInWarehouseLock(lockInStockCount);
                                inStockMap.remove(warehouseInStockDO.getId());
                            } else {
                                warehouseInStockDO.setNumInWarehouseLock(0);
                                inStockMap.put(warehouseInStockDO.getId(), Math.abs(lockInStockCount));
                            }
                            // 在途解锁
                            Integer lockOnWayCount = warehouseInStockDO.getNumOnWayLock() - unLockOnWayCount;
                            if (lockOnWayCount > 0) {
                                warehouseInStockDO.setNumOnWayLock(lockOnWayCount);
                                onWayLockMap.remove(warehouseInStockDO.getId());
                            } else {
                                warehouseInStockDO.setNumOnWayLock(0);
                                onWayLockMap.put(warehouseInStockDO.getId(), Math.abs(lockOnWayCount));
                            }
                            warehouseInStockDO.setUpdateTime(updateTime);
                            LOGGER.info("解锁货品库存：" + JSON.toJSONString(warehouseInStockDO));
                            warehouseInStockDOList.add(warehouseInStockDO);
                        });
                    }
                }
                // vmi库存解锁
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemId);
                if (vmiLock != null) {
                    // 处理正常的vmi解锁
                    AtomicReference<Integer> lockNum =
                        new AtomicReference<>(goodsVmiStockDO.getNumLock() - vmiLock.getLockNum());
                    if (lockNum.get() > 0) {
                        goodsVmiStockDO.setNumLock(lockNum.get());
                    } else {
                        goodsVmiStockDO.setNumLock(0);
                    }
                    goodsVmiStockDO.setUpdateTime(updateTime);
                    goodsVmiStockDOList.add(goodsVmiStockDO);
                }
                Map<Integer, WarehouseInStockDO> warehouseInStockDOMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(inStockDOS)) {
                    warehouseInStockDOMap.putAll(inStockDOS.stream()
                        .collect(Collectors.toMap(WarehouseInStockDO::getId, inStockDO -> inStockDO)));
                }
                // 当锁库数据还未处理完成情况还需继续判断在库在途是否还有锁库可释放
                if (!CollectionUtils.isEmpty(inStockMap)) {
                    inStockMap.forEach((k, v) -> {
                        WarehouseInStockDO warehouseInStockDO = warehouseInStockDOMap.get(k);
                        if ((warehouseInStockDO.getNumInWarehouseLock() == null
                            || warehouseInStockDO.getNumInWarehouseLock() == 0)
                            && (warehouseInStockDO.getNumOnWayLock() == null
                                || warehouseInStockDO.getNumOnWayLock() == 0)
                            && (goodsVmiStockDO == null || goodsVmiStockDO.getNumLock() == 0)) {
                            return;
                        }
                        // 首先在途有锁情况
                        if (v > 0 && warehouseInStockDO != null && warehouseInStockDO.getNumOnWayLock() != null
                            && warehouseInStockDO.getNumOnWayLock() > 0) {
                            int lockNum = warehouseInStockDO.getNumOnWayLock() - v;
                            if (lockNum > 0) {
                                warehouseInStockDO.setNumOnWayLock(lockNum);
                                v = 0;
                            } else {
                                warehouseInStockDO.setNumOnWayLock(0);
                                v = v - warehouseInStockDO.getNumOnWayLock();
                            }
                            if (!warehouseInStockDOList.contains(warehouseInStockDO)) {
                                warehouseInStockDO.setUpdateTime(updateTime);
                                warehouseInStockDOList.add(warehouseInStockDO);
                            }
                        }
                        // 其次VMI有锁情况
                        if (v > 0 && goodsVmiStockDO != null && goodsVmiStockDO.getNumLock() != null
                            && goodsVmiStockDO.getNumLock() > 0) {
                            int lockNum = goodsVmiStockDO.getNumLock() - v;
                            if (lockNum > 0) {
                                goodsVmiStockDO.setNumLock(lockNum);
                                v = 0;
                            } else {
                                goodsVmiStockDO.setNumLock(0);
                                v = v - goodsVmiStockDO.getNumLock();
                            }
                            if (!goodsVmiStockDOList.contains(goodsVmiStockDO)) {
                                goodsVmiStockDO.setUpdateTime(updateTime);
                                goodsVmiStockDOList.add(goodsVmiStockDO);
                            }
                        }
                        // 最后在库有锁情况
                        if (warehouseInStockDO != null && warehouseInStockDO.getNumInWarehouseLock() != null
                            && warehouseInStockDO.getNumInWarehouseLock() > 0) {
                            int lockNum = warehouseInStockDO.getNumInWarehouseLock() - v;
                            if (lockNum > 0) {
                                warehouseInStockDO.setNumInWarehouseLock(lockNum);
                            } else {
                                warehouseInStockDO.setNumInWarehouseLock(0);
                            }
                            if (!warehouseInStockDOList.contains(warehouseInStockDO)) {
                                warehouseInStockDO.setUpdateTime(updateTime);
                                warehouseInStockDOList.add(warehouseInStockDO);
                            }
                        }
                    });
                }
            });
            updateRedisCache(warehouseInStockDOList, goodsVmiStockDOList);
            LOGGER.info("解锁货品库存列表：" + JSON.toJSONString(warehouseInStockDOList));
            warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
            // 刷新货品库存是否有货数据
            messageSendService.freshItemUnderStockFlag(itemIds);
            return Response.buildSuccess();
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(WarehouseDubboApiErrorCode.W_ITEM_UN_LOCK_STOCK_ERROR, MessageUtils.get(WarehouseDubboApiErrorCode.W_ITEM_UN_LOCK_STOCK_ERROR));
        } finally {
            // 释放锁
            if (!CollectionUtils.isEmpty(autoReleaseLockList)) {
                warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
            }
        }
    }

    /**
     * 把相同仓库的 相同stockId 在库在途数据对象合并(合并到原先的在库对象中)
     *
     * @param itemStockLockDTOList
     */
    private void mergeDto(List<ItemStockLockDTO> itemStockLockDTOList) {
        itemStockLockDTOList.forEach(itemStockLockDTO -> {
            Map<String, WarehouseInStockLockDTO> inStockLockDTOMap = new HashMap<>();
            List<WarehouseInStockLockDTO> inStockLockDTOList = itemStockLockDTO.getInStockLockDTOList();
            List<WarehouseInStockLockDTO> onWayLockDTOList = itemStockLockDTO.getOnWayLockDTOList();
            // 同一货品同一仓库在途在库合并
            if (!CollectionUtils.isEmpty(inStockLockDTOList)) {
                inStockLockDTOList.forEach(stockLockDTO -> {
                    WarehouseInStockLockDTO inStockLockDTO = inStockLockDTOMap
                        .get(String.valueOf(stockLockDTO.getWarehouseId()) + String.valueOf(stockLockDTO.getStockId()));
                    if (inStockLockDTO == null) {
                        inStockLockDTO = new WarehouseInStockLockDTO();
                        org.springframework.beans.BeanUtils.copyProperties(stockLockDTO, inStockLockDTO);
                        inStockLockDTOMap.put(
                            String.valueOf(stockLockDTO.getWarehouseId()) + String.valueOf(stockLockDTO.getStockId()),
                            inStockLockDTO);
                    } else {
                        inStockLockDTO.setNumInWarehouseLock(
                            inStockLockDTO.getNumInWarehouseLock() + stockLockDTO.getNumInWarehouseLock());
                    }
                });
            }
            if (!CollectionUtils.isEmpty(onWayLockDTOList)) {
                onWayLockDTOList.forEach(stockLockDTO -> {
                    WarehouseInStockLockDTO inStockLockDTO = inStockLockDTOMap
                        .get(String.valueOf(stockLockDTO.getWarehouseId()) + String.valueOf(stockLockDTO.getStockId()));
                    if (inStockLockDTO == null) {
                        inStockLockDTO = new WarehouseInStockLockDTO();
                        org.springframework.beans.BeanUtils.copyProperties(stockLockDTO, inStockLockDTO);
                        inStockLockDTOMap.put(
                            String.valueOf(stockLockDTO.getWarehouseId()) + String.valueOf(stockLockDTO.getStockId()),
                            inStockLockDTO);
                    } else {
                        if (inStockLockDTO.getNumOnWayLock() == null) {
                            inStockLockDTO.setNumOnWayLock(stockLockDTO.getNumOnWayLock());
                        } else {
                            inStockLockDTO.setNumInWarehouseLock(
                                inStockLockDTO.getNumOnWayLock() + stockLockDTO.getNumOnWayLock());
                        }
                    }
                });
            }
            itemStockLockDTO.setInStockLockDTOList(new ArrayList<>(inStockLockDTOMap.values()));
        });
    }

    /**
     * 锁定和解锁库存(TODO 解锁和锁定有问题，解锁报错或，锁定未回购)
     * 
     * @param itemCountList
     * @param areaIdList
     * @param distributorId
     * @param notSupportOnWayList
     * @param unLockItemList
     * @return
     */
    @Override
    public Response<List<ItemStockLockDTO>> summaryLockStockAndUnLockStock(List<GoodsItemCountDTO> itemCountList,
        List<Integer> areaIdList, Integer distributorId, List<Integer> notSupportOnWayList,
        List<ItemStockLockDTO> unLockItemList) {
        try {
            WarehouseStockDubboValidator.checkLockAndUnLockParamHybrid(itemCountList, areaIdList, distributorId,
                notSupportOnWayList, unLockItemList);
            List<ItemStockLockDTO> lockDTOList = null;
            if (!CollectionUtils.isEmpty(itemCountList)) {
                // 锁库操作
                Response<List<ItemStockLockDTO>> lockStockResp =
                    summaryLockStock(itemCountList, areaIdList, distributorId, notSupportOnWayList);
                if (!lockStockResp.isSuccess()) {
                    return lockStockResp;
                }
                lockDTOList = lockStockResp.getData();
            }
            if (!CollectionUtils.isEmpty(unLockItemList)) {
                // 解锁操作
                Response unLockStockResp = unLockStock(unLockItemList);
                if (!unLockStockResp.isSuccess()) {
                    return unLockStockResp;
                }
            }
            // 锁库
            return Response.of(lockDTOList);
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(e.getCode(), e.getMessage());
        }
    }

    /**
     * 定时redis缓存数据同步到数据库
     *
     * @return
     */
    @Override
    public Response stockRedisSyncMysqlJobHandler() {
        LOGGER.info("=======定时器执行库存同步====");
        warehouseStockSyncCmdExe.stockRedisSyncMysqlJobHandler(TenantContext.getTenantNo());
        return Response.buildSuccess();
    }

    /**
     * ERP同步库存到B2B
     *
     * @return
     */
    @Override
    public Response stockErpSyncB2BJobHandler() {
        LOGGER.info("---------ERP同步库存到B2B开始同步-----------");
        warehouseInStockServiceI.syncStock(TenantContext.getTenantNo());
        LOGGER.info("---------ERP同步库存到B2B执行完毕-----------");
        return Response.buildSuccess();
    }

    /**
     * 根据仓库id和货品id查询在库库存(包含在途库存)
     *
     * @param warehouseId
     * @param itemId
     * @return
     */
    @Override
    public Response<WarehouseInStockRpcDTO> getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId) {
        if (warehouseId == null) {
            return Response.buildFailure(WarehouseCommonErrorCode.ID_NULL,
                MessageUtils.get(WarehouseNameErrorCode.ERROR_NAME_WAREHOUSE)
                    + MessageUtils.get(WarehouseCommonErrorCode.ID_NULL));
        }
        if (itemId == null) {
            return Response.buildFailure(WarehouseCommonErrorCode.ITEM_ID_NULL,
                MessageUtils.get(WarehouseCommonErrorCode.ITEM_ID_NULL));
        }
        WarehouseInStockDO warehouseInStockDO = warehouseInStockServiceI.getByWarehouseIdAndItemId(warehouseId, itemId);
        if (warehouseInStockDO == null) {
            LOGGER.error("查询不到在库库存、仓库id{},货品id{}", warehouseId, itemId);
            return Response.buildFailure(WarehouseCommonErrorCode.ITEM_ID_ERROR,
                MessageUtils.get(WarehouseCommonErrorCode.ITEM_ID_ERROR));
        }
        return Response.of(BeanUtils.copy(warehouseInStockDO, WarehouseInStockRpcDTO.class));
    }

    /**
     * 根据货品id查询VMI库存
     *
     * @param itemId
     * @return
     */
    @Override
    public Response<GoodsVmiStockRpcDTO> getVmiByItemId(Integer itemId) {
        try {
            GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemId);
            return Response.of(BeanUtils.copy(goodsVmiStockDO, GoodsVmiStockRpcDTO.class));
        } catch (WarehouseException e) {
            e.printStackTrace();
            LOGGER.error(itemId + "查询VMI库存异常{}", e);
            return Response.buildFailure(e.getMessage(), e.getMessage());
        }

    }

    @Override
    public Response<List<GoodsVmiStockRpcDTO>> listVmiByItemIdList(Set<Integer> itemIdSet) {
        try {
            List<GoodsVmiStockDO> goodsVmiStockDOList = goodsVmiStockServiceI.listRedisByItemIdList(itemIdSet);
            return Response.of(BeanUtils.copyList(goodsVmiStockDOList, GoodsVmiStockRpcDTO.class));
        } catch (WarehouseException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), e.getMessage());
        }

    }

    // /**
    // * 根据货品id查询在途库存
    // *
    // * @param itemId
    // * @return
    // */
    // @Override
    // public Response<GoodsOnWayStockRpcDTO> getOnWayByItemId(Integer itemId) {
    // try {
    // GoodsOnWayStockDO goodsOnWayStockDO = goodsOnWayStockServiceI.getByItemId(itemId);
    // return Response.of(BeanUtils.copy(goodsOnWayStockDO, GoodsOnWayStockRpcDTO.class));
    // } catch (WarehouseException e) {
    // e.printStackTrace();
    // LOGGER.error(itemId + "查询VMI库存异常{}", e);
    // return Response.buildFailure(e.getMessage(), e.getMessage());
    // }
    // }
    //
    // @Override
    // public Response<List<GoodsOnWayStockRpcDTO>> listOnWayByItemIdList(Set<Integer> itemIdSet) {
    // try {
    // List<GoodsOnWayStockDO> goodsOnWayStockDOList = goodsOnWayStockServiceI.listRedisByItemIdList(itemIdSet);
    // return Response.of(BeanUtils.copyList(goodsOnWayStockDOList, GoodsOnWayStockRpcDTO.class));
    // } catch (WarehouseException e) {
    // e.printStackTrace();
    // return Response.buildFailure(e.getMessage(), e.getMessage());
    // }
    // }

    /**
     * 根据在库rediskey来查询在库库存列表
     *
     * @param instockKeySet
     *            warehouseId+"_"+itemId
     * @return
     */
    @Override
    public Response<List<WarehouseInStockRpcDTO>> listInstockByRedisKeySet(Set<String> instockKeySet) {
        try {
            List<WarehouseInStockDO> warehouseInStockDOList =
                warehouseInStockServiceI.listInstockByRedisKeySet(instockKeySet);
            return Response.of(BeanUtils.copyList(warehouseInStockDOList, WarehouseInStockRpcDTO.class));
        } catch (WarehouseException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), e.getMessage());
        }
    }

    @Override
    public Response<WarehouseStockInfoRpcDTO> getStockByItemIds(List<Integer> itemIds) {
        if (itemIds.size() == 0) {
            return Response.of(null);
        }

        WarehouseStockInfoRpcDTO warehouseStockInfoRpcDTO = new WarehouseStockInfoRpcDTO();

        List<WarehouseInStockDO> warehouseInStockDOS = warehouseInStockServiceI.listByItemIds(itemIds);
        List<WarehouseInStockRpcDTO> warehouseInStockRpcDTOS = new ArrayList<>();
        for (WarehouseInStockDO warehouseInStockDO : warehouseInStockDOS) {
            WarehouseInStockRpcDTO warehouseInStockRpcDTO = new WarehouseInStockRpcDTO();
            org.springframework.beans.BeanUtils.copyProperties(warehouseInStockDO, warehouseInStockRpcDTO);
            warehouseInStockRpcDTOS.add(warehouseInStockRpcDTO);
        }
        warehouseStockInfoRpcDTO.setWarehouseInStockRpcDTOS(warehouseInStockRpcDTOS);

        Set<Integer> itemIdsSet = new HashSet<>(itemIds);
        // List<GoodsOnWayStockDO> goodsOnWayStockDOS = goodsOnWayStockServiceI.incListRedisByItemIdList(itemIdsSet);
        // List<GoodsOnWayStockRpcDTO> goodsOnWayStockRpcDTOS = new ArrayList<>();
        // for (GoodsOnWayStockDO goodsOnWayStockDO : goodsOnWayStockDOS) {
        // GoodsOnWayStockRpcDTO goodsOnWayStockRpcDTO = new GoodsOnWayStockRpcDTO();
        // org.springframework.beans.BeanUtils.copyProperties(goodsOnWayStockDO, goodsOnWayStockRpcDTO);
        // goodsOnWayStockRpcDTOS.add(goodsOnWayStockRpcDTO);
        // }
        // warehouseStockInfoRpcDTO.setGoodsOnWayStockRpcDTOS(goodsOnWayStockRpcDTOS);

        List<GoodsVmiStockDO> goodsVmiStockDOS = goodsVmiStockServiceI.incListRedisByItemIdList(itemIdsSet);
        List<GoodsVmiStockRpcDTO> goodsVmiStockRpcDTOS = new ArrayList<>();
        for (GoodsVmiStockDO goodsVmiStockDO : goodsVmiStockDOS) {
            GoodsVmiStockRpcDTO goodsVmiStockRpcDTO = new GoodsVmiStockRpcDTO();
            org.springframework.beans.BeanUtils.copyProperties(goodsVmiStockDO, goodsVmiStockRpcDTO);
            goodsVmiStockRpcDTOS.add(goodsVmiStockRpcDTO);
        }
        warehouseStockInfoRpcDTO.setGoodsVmiStockRpcDTOS(goodsVmiStockRpcDTOS);
        return Response.of(warehouseStockInfoRpcDTO);
    }

    public static void main(String[] args) {
        try {
            System.out.println(MessageUtils.get(WarehouseDubboApiErrorCode.W_ITEM_NOT_SUPPORT_ON_WAY));
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_NOT_SUPPORT_ON_WAY);
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            LOGGER.info(e.getMsg());
        }
    }

}
