package com.bat.warehouse.manager.dubbo.executor;

import static com.bat.warehouse.manager.common.constant.WarehouseCommonConstant.COMMON_SYNC_TYPE_1;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.constant.WarehouseStockChangeLogConstant;
import com.bat.warehouse.manager.common.error.WarehouseInStockErrorCode;
import com.bat.warehouse.manager.dubbo.validtor.WarehouseStockDubboValidator;
import com.bat.warehouse.manager.inStock.executor.WarehouseStockSyncCmdExe;
import com.bat.warehouse.manager.message.MessageSendService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.order.stock.api.OrderGoodsStockServiceRpc;
import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.stock.dto.ErpItemStockChangeCmd;
import com.bat.dubboapi.warehouse.stock.dto.ErpStockChangeCmd;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.WarehouseInStockLockDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseDubboApiException;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.log.WarehouseStockChangeLogServiceI;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.config.ConfigQry;
import com.bat.warehouse.mq.dto.OrderErpNoLineDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WarehouseStockDubboCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockDubboCmdExe.class);

    @Autowired
    private WarehouseServiceI warehouseServiceI;

    @Autowired
    private WarehouseInStockServiceI warehouseInStockServiceI;

    @Autowired
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    @Autowired
    private WarehouseStockChangeLogServiceI warehouseStockChangeLogServiceI;

    @Resource
    private ConfigQry vmiConfigExe;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private OrderGoodsStockServiceRpc orderGoodsStockServiceRpc;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE, cacheType = CacheType.BOTH)
    private Cache<String, String> stockkeyCache;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private WarehouseStockSyncCmdExe warehouseStockSyncCmdExe;

    /**
     * ERP 库存变更（包含订单库存解锁）
     * 
     * @param erpStockChangeCmd
     * @return
     */
    public Response updateWarehouseStock(ErpStockChangeCmd erpStockChangeCmd) {
        // ====================================校验数据====================================
        String vmiWarehouse = vmiConfigExe.getTenantErpConfig().getVmiWarehouse();
        // 校验ERP传过来的参数、返回所有的ERP订单明细 变成List<erpOrderNo,List<明细内码>>结构
        List<ErpItemStockChangeCmd> stockBillDetailList = erpStockChangeCmd.getStockBillDetails();
        // 获取ERP订单发货原因同步的库存减少
        List<OrderErpNoLineDTO> orderErpNoLineDTOS =
            WarehouseStockDubboValidator.validWarehouseStockChangeFromErp(stockBillDetailList);
        // 查询B2B可用的仓库列表
        List<WarehouseDO> warehouseDOList = warehouseServiceI.listUsableWarehouse(COMMON_SYNC_TYPE_1);
        if (CollectionUtils.isEmpty(warehouseDOList)) {
            throw WarehouseDubboApiException
                .buildException(WarehouseInStockErrorCode.W_DISTRIBUTOR_RELEVANCE_WAREHOUSE_NULL);
        }
        // 验证B2B仓库是否包含此仓库
        WarehouseStockDubboValidator.removeNotBelongB2BWarehouse(stockBillDetailList, warehouseDOList, vmiWarehouse);
        if (CollectionUtils.isEmpty(stockBillDetailList) && CollectionUtils.isEmpty(orderErpNoLineDTOS)) {
            return Response.buildSuccess();
        }
        List<String> itemCodeList =
            stockBillDetailList.stream().map(ErpItemStockChangeCmd::getItemNo).collect(Collectors.toList());
        List<Integer> warehouseIdList = warehouseDOList.stream().map(WarehouseDO::getId).collect(Collectors.toList());
        List<WarehouseInStockDO> allWarehouseInStockDOList =
            warehouseInStockServiceI.listByWarehouseIdAndItemCodeList(warehouseIdList, itemCodeList);
        // 移除非B2B没有的货品
        WarehouseStockDubboValidator.removeNotBelongB2BItem(stockBillDetailList, allWarehouseInStockDOList);
        if (CollectionUtils.isEmpty(stockBillDetailList) && CollectionUtils.isEmpty(orderErpNoLineDTOS)) {
            return Response.buildSuccess();
        }
        LOGGER.info("订单解锁参数为：{}", JSON.toJSONString(orderErpNoLineDTOS));
        // ====================================校验结束====================================
        // 库存变更消息（只变更需要变更的库存）
        if (!CollectionUtils.isEmpty(stockBillDetailList)) {
            messageSendService.changeGoodsStockByErp(stockBillDetailList, erpStockChangeCmd.getBillNo(),
                erpStockChangeCmd.getStockBillType());
        }
        // 订单明细解锁
        if (!CollectionUtils.isEmpty(orderErpNoLineDTOS)) {
            messageSendService.unLockByOrderErpNoLine(orderErpNoLineDTOS, erpStockChangeCmd.getBillNo());
        }
        return Response.buildSuccess();
    }

    /**
     * erp根据库存和物料更新库存(没有调用)
     * 
     * @param stockBillDetailList
     * @param billNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void erpChangeStock(List<ErpItemStockChangeCmd> stockBillDetailList, String billNo) {
        String vmiWarehouse = vmiConfigExe.getTenantErpConfig().getVmiWarehouse();
        List<WarehouseStockChangeLogDO> warehouseStockChangeLogDOList =
            warehouseStockChangeLogServiceI.listByBillNo(billNo);
        Map<String, WarehouseStockChangeLogDO> stockChangeLogMap = new HashMap<>();
        List<String> warehouseNos = new ArrayList<>();
        List<WarehouseStockChangeLogDO> stockChangeLogList = new ArrayList<>();
        List<GoodsVmiStockDO> goodsVmiStockDOList = new ArrayList<>();
        // List<GoodsOnWayStockDO> goodsOnWayStockDOList = new ArrayList<>();
        List<WarehouseDO> warehouseDOList = new ArrayList<>();
        List<WarehouseInStockDO> warehouseInStockDOList = new ArrayList<>();
        if (warehouseStockChangeLogDOList == null) {
            warehouseStockChangeLogDOList = new ArrayList<>();
        } else {
            for (WarehouseStockChangeLogDO stockChangeLog : warehouseStockChangeLogDOList) {
                stockChangeLogMap.put(stockChangeLog.getItemCode() + stockChangeLog.getWarehouseNo(), stockChangeLog);
                if (!warehouseNos.contains(stockChangeLog.getWarehouseNo())) {
                    warehouseNos.add(stockChangeLog.getWarehouseNo());
                }
            }
        }
        Map<String, WarehouseDO> warehouseMap = new HashMap<>();
        for (ErpItemStockChangeCmd erpStockChangeReq : stockBillDetailList) {
            if (!warehouseNos.contains(erpStockChangeReq.getWarehouseNo())) {
                warehouseNos.add(erpStockChangeReq.getWarehouseNo());
            }
            // redis进行编码去货品
            Integer itemId = 0;
            GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemId);
            goodsVmiStockDOList.add(goodsVmiStockDO);
            // GoodsOnWayStockDO goodsOnWayStockDO = goodsOnWayStockServiceI.getByItemId(itemId);
            // goodsOnWayStockDOList.add(goodsOnWayStockDO);
            List<WarehouseInStockDO> stockDOList =
                warehouseInStockServiceI.listByItemIdAndWarehouseIdList(itemId, null);
            warehouseInStockDOList.addAll(stockDOList);
            WarehouseDO warehouseDO = warehouseMap.get(erpStockChangeReq.getWarehouseNo());
            if (warehouseDO == null) {
                warehouseDO = warehouseServiceI.getByWarehouseNo(erpStockChangeReq.getWarehouseNo());
                warehouseMap.put(warehouseDO.getWarehouseNo(), warehouseDO);
            }
        }
        /* List<GoodsItemIdCode> goodsItemIdCodes = goodsDataManager.findGoodsItemIdCodes(itemNos);
        List<Long> itemIds = new ArrayList<>();
        Map<String,GoodsItemIdCode> goodsItemMap = new HashMap<>();
        for (GoodsItemIdCode goodsItemIdCode :goodsItemIdCodes){
            goodsItemMap.put(goodsItemIdCode.getItemCode(),goodsItemIdCode);
            itemIds.add(goodsItemIdCode.getId());
        }
        List<WarehouseStock> warehouseStocks = stockDataManager.findStocksByItemIds(itemIds);
        List<GoodsVmiStock> goodsVmiStocks = stockDataManager.findVmiStockByItemIds(itemIds);// vmi仓
        List<Warehouse> warehouses = warehouseDataManager.findByErpWarehouseNo(warehouseNos);*/
        for (int i = 0; i < stockBillDetailList.size(); i++) {
            ErpItemStockChangeCmd erpStockChangeReq = stockBillDetailList.get(i);
            WarehouseDO warehouse = warehouseMap.get(erpStockChangeReq.getWarehouseNo());
            // GoodsItemIdCode goodsItemIdCode = goodsItemMap.get(erpStockChangeReq.getItemNo());
            if (warehouse == null && !vmiWarehouse.equals(erpStockChangeReq.getWarehouseNo())) {// 当仓库或货品不存在，过滤更新
                stockBillDetailList.remove(i);
                i--;
                continue;
            }
            WarehouseStockChangeLogDO stockChangeLog =
                stockChangeLogMap.get(erpStockChangeReq.getItemNo() + erpStockChangeReq.getWarehouseNo());// 查找是否以往已同步过的单据
            // 以往已同步过的单据
            if (stockChangeLog != null) {
                warehouseStockChangeLogDOList.remove(stockChangeLog);// 日志也无需更新
                if (stockChangeLog.getChangeType().equals(erpStockChangeReq.getChangeType())) {
                    // 数量一样，无需更新
                    if (stockChangeLog.getNum().equals(erpStockChangeReq.getNum())) {
                        stockBillDetailList.remove(i);
                        i--;
                        continue;
                    } else {// 数量不一样，有更新
                        Integer num = erpStockChangeReq.getNum() - stockChangeLog.getNum();
                        stockChangeLog.setNum(erpStockChangeReq.getNum());
                        if (num < 0) {// 变化数量小于原来的数量，变更类型相反
                            erpStockChangeReq.setChangeType((short)(erpStockChangeReq.getChangeType() == 1 ? 2 : 1));
                        }
                        erpStockChangeReq.setNum(Math.abs(num));
                        stockChangeLog.setUpdateTime(new Date());
                        // saveStockChangeLogs.add(stockChangeLog);
                        warehouseStockChangeLogServiceI.update(stockChangeLog);
                    }
                } else {
                    // deleteStockChangeLogs.add(stockChangeLog);
                    // 删除记录
                    warehouseStockChangeLogServiceI.deleteById(stockChangeLog.getId());
                }
            } else {

                // saveStockChangeLogs.add(stockChangeLog);
                warehouseStockChangeLogServiceI.create(billNo, erpStockChangeReq.getChangeType(),
                    erpStockChangeReq.getWarehouseNo(), WarehouseStockChangeLogConstant.CHANGE_SOURCE_ERP,
                    erpStockChangeReq.getItemNo(), erpStockChangeReq.getNum());
            }
        }
        // 如果同一个单号以往的单据有数据，但最新的单据没找到数据时需恢复库存数量
        if (warehouseStockChangeLogDOList != null && warehouseStockChangeLogDOList.size() > 0) {
            for (WarehouseStockChangeLogDO stockChangeLog : warehouseStockChangeLogDOList) {
                ErpItemStockChangeCmd erpStockChangeReq = new ErpItemStockChangeCmd();
                erpStockChangeReq.setItemNo(stockChangeLog.getItemCode());
                erpStockChangeReq.setNum(stockChangeLog.getNum());
                erpStockChangeReq.setWarehouseNo(stockChangeLog.getWarehouseNo());
                erpStockChangeReq.setChangeType((short)(stockChangeLog.getChangeType() == 1 ? 2 : 1));
                stockBillDetailList.add(erpStockChangeReq);
                // deleteStockChangeLogs.add(stockChangeLog);
                // 删除记录
                warehouseStockChangeLogServiceI.deleteById(stockChangeLog.getId());
            }
        }
        for (int i = 0; i < stockBillDetailList.size(); i++) {
            ErpItemStockChangeCmd erpItemStockChangeCmd = stockBillDetailList.get(i);
            WarehouseDO warehouseDO = warehouseMap.get(erpItemStockChangeCmd.getWarehouseNo());
            // GoodsItemIdCode goodsItemIdCode = goodsItemMap.get(erpItemStockChangeCmd.getItemNo());
            // redis转换、拿到货品id
            Integer itemId = 0;
            if (warehouseDO == null && !vmiWarehouse.equals(erpItemStockChangeCmd.getWarehouseNo())) {// 当仓库或货品不存在，过滤更新
                stockBillDetailList.remove(i);
                i--;
                continue;
            }
            // VMI仓
            if (erpItemStockChangeCmd.getWarehouseNo().equals(vmiWarehouse)) {

                GoodsVmiStockDO goodsVmiStock = goodsVmiStockServiceI.getByItemId(itemId);
                if (goodsVmiStock != null) {// 只有一个VIM仓库，所以只要货品ID一致
                    LOGGER.info("库存变更前：" + goodsVmiStock.getItemId() + ":" + erpItemStockChangeCmd.getWarehouseNo()
                        + ":" + goodsVmiStock.getNumVmi());
                    Integer num = goodsVmiStock.getNumVmi();
                    if (erpItemStockChangeCmd.getChangeType() == 1) {// 增加
                        num = num + erpItemStockChangeCmd.getNum();
                    } else {// 减少
                        num = num - erpItemStockChangeCmd.getNum();
                    }
                    if (num < 0) {
                        num = 0;
                    }
                    goodsVmiStock.setNumVmi(num);
                    LOGGER.info("库存变更后：" + goodsVmiStock.getItemId() + ":" + erpItemStockChangeCmd.getWarehouseNo()
                        + ":" + goodsVmiStock.getNumVmi());
                    goodsVmiStockServiceI.create(goodsVmiStock);
                }
            }
            if (!warehouseDO.getWarehouseNo().equals(vmiWarehouse)) {
                // 在库数量更新
                WarehouseInStockDO warehouseInStockDO =
                    warehouseInStockServiceI.findByItemIdAndWarehouseNo(itemId, erpItemStockChangeCmd.getWarehouseNo());
                if (warehouseInStockDO != null) {// 货品ID和仓库ID要一致
                    LOGGER.info("库存变更前：" + warehouseInStockDO.getItemId() + ":" + erpItemStockChangeCmd.getWarehouseNo()
                        + ":" + warehouseInStockDO.getNumInWarehouse());
                    Integer num = warehouseInStockDO.getNumInWarehouse();
                    if (WarehouseStockChangeLogConstant.CHANGE_TYPE_ADD.equals(erpItemStockChangeCmd.getChangeType())) {// 增加
                        num = num + erpItemStockChangeCmd.getNum();
                    } else {// 减少
                        num = num - erpItemStockChangeCmd.getNum();
                    }
                    if (num < 0) {
                        num = 0;
                    }
                    warehouseInStockDO.setNumInWarehouse(num);
                    LOGGER.info("库存变更后：" + erpItemStockChangeCmd.getItemNo() + ":"
                        + erpItemStockChangeCmd.getWarehouseNo() + ":" + warehouseInStockDO.getNumInWarehouse());
                    /* if(!changeWarehouseStocks.contains(warehouseStock)){
                        changeWarehouseStocks.add(warehouseStock);
                    }*/
                    warehouseInStockServiceI.update(warehouseInStockDO);
                }
            }
        }
    }

    /**
     * 锁库（货品id粒度）、返回在库可下单量和
     * 
     * 分销商归属仓库列表 下单锁这个列表
     * 
     * @param itemId
     *            货品id
     * @param autoReleaseLockList
     *            锁列表
     * @param warehouseIdList
     *            仓库id列表（解锁库存锁这个列表）
     * @return
     */
    public List<WarehouseInStockDO> LockAndReturnInStockList(Integer itemId, List<AutoReleaseLock> autoReleaseLockList,
        List<Integer> warehouseIdList) {
        List<WarehouseInStockDO> warehouseInStockDOList = new ArrayList<>();
        // 锁定在途在库
        if (!CollectionUtils.isEmpty(warehouseIdList)) {
            warehouseIdList.forEach(warehouseId -> {
                while (true) {
                    AutoReleaseLock autoReleaseLock = stockkeyCache.tryLock(TenantContext.getTenantNo() + ":"
                        + WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE + warehouseId + "_" + itemId, 10,
                        TimeUnit.SECONDS);
                    if (autoReleaseLock != null) {
                        autoReleaseLockList.add(autoReleaseLock);
                        WarehouseInStockDO warehouseInStockDO =
                            warehouseInStockServiceI.getByWarehouseIdAndItemId(warehouseId, itemId);
                        if (warehouseInStockDO != null) {
                            warehouseInStockDOList.add(warehouseInStockDO);
                        }
                        break;
                    }
                }
            });
        }
        // 锁定 VMI
        while (true) {
            AutoReleaseLock vmiAutoReleaseLock = stockkeyCache.tryLock(
                TenantContext.getTenantNo() + ":" + WarehouseKeyConstant.WAREHOUSE_VMI_STOCK_LOCK_KEY_PRE + itemId, 10,
                TimeUnit.SECONDS);
            if (vmiAutoReleaseLock != null) {
                autoReleaseLockList.add(vmiAutoReleaseLock);
                break;
            }
        }
        return warehouseInStockDOList;
    }

    /**
     * 释放redis分布式锁
     * 
     * @param autoReleaseLockList
     */
    public void releaseLock(List<AutoReleaseLock> autoReleaseLockList) {
        if (!CollectionUtils.isEmpty(autoReleaseLockList)) {
            for (int x = 0; x < autoReleaseLockList.size(); x++) {
                AutoReleaseLock autoReleaseLock = autoReleaseLockList.get(x);
                autoReleaseLock.close();
                autoReleaseLockList.remove(x);
                x--;
            }
        }
    }

    /**
     * 当在库id为空的时候设置在库库存id
     * 
     * @param itemStockLockDTOList
     */
    public void setInStockId(List<ItemStockLockDTO> itemStockLockDTOList) {
        itemStockLockDTOList.stream().forEach(itemStockLockDTO -> {
            List<WarehouseInStockLockDTO> inStockLockDTOList = itemStockLockDTO.getInStockLockDTOList();
            if (!CollectionUtils.isEmpty(inStockLockDTOList)) {
                inStockLockDTOList.forEach(inStockLockDTO -> {
                    if (inStockLockDTO.getStockId() == null) {
                        WarehouseInStockDO warehouseInStockDO = warehouseInStockServiceI
                            .getByWarehouseIdAndItemId(inStockLockDTO.getWarehouseId(), itemStockLockDTO.getItemId());
                        inStockLockDTO.setStockId(warehouseInStockDO.getId());
                    }
                });
            }
        });
    }

    /**
     * 根据商品Ids移除库存（包括缓存数据）
     * 
     * @param goodsIds
     */
    public void deleteGoodsStockByGoodsIds(List<Integer> goodsIds) {
        warehouseInStockServiceI.deleteByGoodsIds(goodsIds);
    }
}
