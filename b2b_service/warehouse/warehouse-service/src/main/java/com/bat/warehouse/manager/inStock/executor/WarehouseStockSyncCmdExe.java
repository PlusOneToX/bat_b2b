package com.bat.warehouse.manager.inStock.executor;

import static com.bat.warehouse.manager.common.constant.WarehouseCommonConstant.COMMON_SYNC_TYPE_1;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.manager.common.config.WarehouseConfig;
import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.WarehouseErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseInStockErrorCode;
import com.bat.warehouse.manager.inStock.convertor.WarehouseInStockConvertor;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockCmdExe;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockQryExe;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockCmdExe;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.stock.api.GoodsStockFlagServiceRpc;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseDubboApiException;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.WarehouseInStockDOMapper;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;
import com.bat.warehouse.dao.vmi.GoodsVmiStockDOMapper;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.config.ConfigQry;
import com.bat.warehouse.manager.dubbo.executor.WarehouseStockDubboCmdExe;
import com.bat.warehouse.manager.dubbo.validtor.WarehouseStockDubboValidator;
import com.bat.warehouse.manager.message.MessageSendService;
import com.bat.warehouse.mq.dto.ErpItemStockChangeDTO;

@Component
public class WarehouseStockSyncCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockSyncCmdExe.class);

    @Lazy
    @Resource
    private WarehouseInStockServiceI warehouseInStockServiceI;

    @Lazy
    @Resource
    private WarehouseServiceI warehouseServiceI;

    @Resource
    private WarehouseStockDubboCmdExe warehouseStockDubboCmdExe;

    @Lazy
    @Resource
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE)
    private Cache<String, WarehouseInStockDO> itemInStockCache;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE, cacheType = CacheType.BOTH)
    private Cache<String, String> stockkeyCache;

    @CreateCache(name = WarehouseKeyConstant.GOODS_VMI_STOCK_DO_PRE)
    private Cache<String, GoodsVmiStockDO> goodsVmiStockDOCache;

    @Resource
    private ErpStockQryExe erpStockQryExe;

    @Resource
    private ConfigQry configQry;

    @Resource
    private GoodsOnWayStockQryExe goodsOnWayStockQryExe;

    @Resource
    private GoodsOnWayStockCmdExe goodsOnWayStockCmdExe;

    @Resource
    private WarehouseInStockCmdExe warehouseInStockCmdExe;

    @Resource
    private WarehouseInStockQryExe warehouseInStockQryExe;

    @Resource
    private GoodsVmiStockCmdExe goodsVmiStockCmdExe;

    @Resource
    private GoodsVmiStockQryExe goodsVmiStockQryExe;

    @DubboReference(check = false, timeout = 60000, retries = 0)
    private GoodsStockFlagServiceRpc goodsStockFlagServiceRpc;

    @DubboReference(check = false, timeout = 60000, retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @Resource
    private WarehouseConfig config;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_SYNC_ERP_STOCK_KEY_PRE)
    private Cache<String, Integer> syncErpStockCache;
    @Resource
    WarehouseInStockDOMapper warehouseInStockDOMapper;
    @Resource
    private GoodsVmiStockDOMapper goodsVmiStockDOMapper;

    @Resource
    private MessageSendService messageSendService;

    /**
     * ERP库存同步（只同步了缓存）
     *
     * @param tenantNo
     * @param autoReleaseLock
     */
    @Async
    public void syncStock(String tenantNo, AutoReleaseLock autoReleaseLock) {
        TenantContext.setTenantNo(tenantNo);
        try {
            // 在库在途同步（所有仓库）
            List<WarehouseDO> warehouseList = warehouseServiceI.listUsableWarehouse(COMMON_SYNC_TYPE_1);
            if (CollectionUtils.isEmpty(warehouseList)) {
                throw new WarehouseException(WarehouseErrorCode.W_WAREHOUSE_NONE_ENABLE_ERROR);
            }
            for (WarehouseDO warehouse : warehouseList) {
                // 分页同步库存（当获取的列表数量小于每页数量时，说明获取数据完成，结束循环）
                Integer page = 1;
                List<WarehouseInStockDO> inStockDOList = new ArrayList<>();
                while (page == 1
                    || (!CollectionUtils.isEmpty(inStockDOList) && inStockDOList.size() == config.getSyncStockNum())) {
                    PageHelper.startPage(page, config.getSyncStockNum());
                    inStockDOList = warehouseInStockDOMapper.listByCondition(warehouse.getId());
                    page++;
                    List<Long> groupItemErpIdList = inStockDOList.stream().map(WarehouseInStockDO::getItemErpId)
                        .mapToLong(itemErpId -> itemErpId.longValue()).boxed().collect(Collectors.toList());
                    // 在库在途库存同步
                    List<InquiryInfo> inStockList = erpStockQryExe.queryErpStock(warehouse.getWarehouseNo(),
                        groupItemErpIdList, config.getSyncStockNum());
                    Map<Integer, InquiryInfo> inStockMap = new HashMap<>();
                    if (!CollectionUtils.isEmpty(inStockList)) {
                        inStockList.forEach(inquiryInfo -> {
                            inquiryInfo.setWarehouseId(warehouse.getId());
                            Integer itemErpId = Integer.parseInt(inquiryInfo.getFMATERIALID());
                            inStockMap.put(itemErpId, inquiryInfo);
                        });
                    }
                    syncInStock(inStockMap, warehouse, inStockDOList);
                }
            }
            // VMI库存同步
            String vmiWarehouse = configQry.getTenantErpConfig().getVmiWarehouse();
            // 分页同步库存（当获取的列表数量小于每页数量时，说明获取数据完成，结束循环）
            Integer vmiPage = 1;
            List<GoodsVmiStockDO> vmiStockDOList = new ArrayList<>();
            while (vmiPage == 1
                || (!CollectionUtils.isEmpty(vmiStockDOList) && vmiStockDOList.size() == config.getSyncStockNum())) {
                PageHelper.startPage(vmiPage, config.getSyncStockNum());
                vmiStockDOList = goodsVmiStockDOMapper.listAll();
                vmiPage++;
                List<Long> groupItemErpIdList = vmiStockDOList.stream().map(GoodsVmiStockDO::getItemErpId)
                    .mapToLong(itemErpId -> itemErpId.longValue()).boxed().collect(Collectors.toList());
                // VMI库存同步
                List<InquiryInfo> vmiStockList =
                    erpStockQryExe.queryErpStock(vmiWarehouse, groupItemErpIdList, config.getSyncStockNum());
                Map<Integer, InquiryInfo> vmiStockMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(vmiStockList)) {
                    vmiStockList.forEach(inquiryInfo -> {
                        Integer itemErpId = Integer.parseInt(inquiryInfo.getFMATERIALID());
                        vmiStockMap.put(itemErpId, inquiryInfo);
                    });
                }
                syncVmiStock(vmiStockMap, vmiStockDOList);
            }
            // 刷新货品库存是否有货
            warehouseInStockCmdExe.freshItemUnderStockFlag(tenantNo);
        } catch (WarehouseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (autoReleaseLock != null) {
                autoReleaseLock.close();
            }
            TenantContext.removeTenantNo();
        }
    }

    /**
     * ERP库存同步（只同步了缓存）
     *
     * @param inStockMap
     *            在库和在途库存 (在途也在里面)
     * @param vmiStockList
     *            VMI库存
     * @param warehouseIdList
     *            仓库id列表
     */
    public void syncStock(Map<Integer, List<InquiryInfo>> inStockMap, List<InquiryInfo> vmiStockList,
        List<Integer> warehouseIdList, List<WarehouseInStockDO> inStockDOList) {
        // VMI库存集合
        Map<String, InquiryInfo> vmiStockMap = WarehouseInStockConvertor.toInquiryInfoMap(vmiStockList);
        // 分布式锁
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        try {
            for (int x = 0; x < inStockDOList.size(); x++) {
                WarehouseInStockDO itemInStockDO = inStockDOList.get(x);
                // 在库在途库存列表
                List<InquiryInfo> inStockList = inStockMap.get(itemInStockDO.getItemErpId());
                Map<Integer, InquiryInfo> inquiryInfoMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(inStockList)) {
                    inquiryInfoMap = inStockList.stream()
                        .collect(Collectors.toMap(InquiryInfo::getWarehouseId, inquiryInfo -> inquiryInfo));
                }
                // VMI库存
                InquiryInfo vmiInquiryInfo = vmiStockMap.get(String.valueOf(itemInStockDO.getItemErpId()));

                List<WarehouseInStockDO> warehouseInStockDOList = warehouseStockDubboCmdExe
                    .LockAndReturnInStockList(itemInStockDO.getItemId(), autoReleaseLockList, warehouseIdList);
                // 是否设置了在途库存
                // Boolean itemSetOnWayFlag = false;
                // 货品在库库存查询有值
                for (int y = 0; y < warehouseInStockDOList.size(); y++) {
                    WarehouseInStockDO warehouseInStockDO = warehouseInStockDOList.get(y);
                    InquiryInfo inquiryInfo = inquiryInfoMap.get(warehouseInStockDO.getWarehouseId());
                    if (inquiryInfo != null) {
                        Long numInWarehouse = inquiryInfo.getPREDICTQTY();
                        warehouseInStockDO.setNumInWarehouse(numInWarehouse.intValue());
                        warehouseInStockDO.setErpNumLock(inquiryInfo.getLockedQuantity().intValue());
                        warehouseInStockDO.setErpNumInWarehouse(inquiryInfo.getFBASEQTY().intValue());
                        warehouseInStockDO.setNumOnWay(inquiryInfo.getQuantityInTransit().intValue());
                    } else {
                        warehouseInStockDO.setNumInWarehouse(0);
                        warehouseInStockDO.setNumOnWay(0);
                    }
                    warehouseInStockDO.setUpdateTime(new Date());
                    warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
                }
                // 处理VMI
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemInStockDO.getItemId());
                if (vmiInquiryInfo != null) {
                    goodsVmiStockDO.setNumVmi(vmiInquiryInfo.getPREDICTQTY().intValue());
                } else {
                    goodsVmiStockDO.setNumVmi(0);
                }
                goodsVmiStockDO.setUpdateTime(new Date());
                goodsVmiStockServiceI.updateRedisCacheOnly(goodsVmiStockDO);
                // 处理完一个货品、释放锁
                warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("更新库存数据异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            // 释放锁
            warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
        }
    }

    /**
     * ERP在库在途库存同步（只同步了缓存）
     *
     * @param inStockMap
     * @param warehouse
     * @param inStockDOList
     */
    public void syncInStock(Map<Integer, InquiryInfo> inStockMap, WarehouseDO warehouse,
        List<WarehouseInStockDO> inStockDOList) {
        // 分布式锁
        inStockDOList.forEach(itemInStockDO -> {
            AutoReleaseLock inAutoReleaseLock = null;
            try {
                // 在库库存更新
                InquiryInfo inStock = inStockMap.get(itemInStockDO.getItemErpId());
                // 更新在库库存时需锁定对应的在库缓存数据
                inAutoReleaseLock = stockkeyCache
                    .tryLock(TenantContext.getTenantNo() + ":" + WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE
                        + warehouse.getId() + "_" + itemInStockDO.getItemId(), 10, TimeUnit.SECONDS);
                // 如果在库库存正被锁定情况，等到1秒继续获取
                while (inAutoReleaseLock == null) {
                    Thread.sleep(1000);
                    inAutoReleaseLock = stockkeyCache.tryLock(
                        TenantContext.getTenantNo() + ":" + WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE
                            + warehouse.getId() + "_" + itemInStockDO.getItemId(),
                        10, TimeUnit.SECONDS);
                }
                // 重新获取库存(优先从redis重新获取)
                WarehouseInStockDO warehouseInStockDO =
                    warehouseInStockServiceI.getByWarehouseIdAndItemId(warehouse.getId(), itemInStockDO.getItemId());
                // 更新库存有数据返回时更新库存，无数据返回时默认库存为零
                if (inStock != null) {
                    Long numInWarehouse = inStock.getPREDICTQTY();
                    warehouseInStockDO.setNumInWarehouse(numInWarehouse.intValue());
                    warehouseInStockDO.setErpNumInWarehouse(inStock.getFBASEQTY().intValue());
                    warehouseInStockDO.setNumOnWay(inStock.getQuantityInTransit().intValue());
                    warehouseInStockDO.setErpNumLock(inStock.getLockedQuantity().intValue());
                } else {
                    warehouseInStockDO.setNumInWarehouse(0);
                    warehouseInStockDO.setErpNumInWarehouse(0);
                    warehouseInStockDO.setNumOnWay(0);
                    warehouseInStockDO.setErpNumLock(0);
                }
                warehouseInStockDO.setUpdateTime(new Date());
                // 更新redis缓存数据
                warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                if (inAutoReleaseLock != null) {
                    inAutoReleaseLock.close();
                }
            }
        });
    }

    /**
     * ERP VMI库存同步（只同步了缓存）
     *
     * @param vmiStockMap
     * @param vmiStockDOList
     */
    public void syncVmiStock(Map<Integer, InquiryInfo> vmiStockMap, List<GoodsVmiStockDO> vmiStockDOList) {
        // 分布式锁
        vmiStockDOList.forEach(itemInStockDO -> {
            AutoReleaseLock vmiAutoReleaseLock = null;
            try {
                // VMI库存更新
                InquiryInfo vmiStock = vmiStockMap.get(itemInStockDO.getItemErpId());
                // 更新VIM库存时需锁定对应的VIM缓存数据
                vmiAutoReleaseLock = stockkeyCache.tryLock(TenantContext.getTenantNo() + ":"
                    + WarehouseKeyConstant.WAREHOUSE_VMI_STOCK_LOCK_KEY_PRE + itemInStockDO.getItemId(), 10,
                    TimeUnit.SECONDS);
                // 如果VIM库存正被锁定情况，等到1秒继续获取
                while (vmiAutoReleaseLock == null) {
                    Thread.sleep(1000);
                    vmiAutoReleaseLock = stockkeyCache.tryLock(TenantContext.getTenantNo() + ":"
                        + WarehouseKeyConstant.WAREHOUSE_VMI_STOCK_LOCK_KEY_PRE + itemInStockDO.getItemId(), 10,
                        TimeUnit.SECONDS);
                }
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemInStockDO.getItemId());
                if (vmiStock != null) {
                    goodsVmiStockDO.setNumVmi(vmiStock.getPREDICTQTY().intValue());
                } else {
                    goodsVmiStockDO.setNumVmi(0);
                }
                goodsVmiStockDO.setUpdateTime(new Date());
                goodsVmiStockServiceI.updateRedisCacheOnly(goodsVmiStockDO);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (vmiAutoReleaseLock != null) {
                    vmiAutoReleaseLock.close();
                }
            }
        });
    }

    /**
     * 预留
     *
     * @param itemStockMap
     *            key：货品id value:数量
     * @param warehouseId
     */
    public void operateReservedStock(Map<Integer, Integer> itemStockMap, Integer warehouseId) {
        if (warehouseId == null || itemStockMap == null || itemStockMap.size() == 0) {
            return;
        }
        List<Integer> warehouseIdList = new ArrayList<>();
        warehouseIdList.add(warehouseId);
        Iterator<Map.Entry<Integer, Integer>> iterator = itemStockMap.entrySet().iterator();
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        try {
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = iterator.next();
                Integer itemId = entry.getKey();
                Integer count = entry.getValue();
                List<WarehouseInStockDO> warehouseInStockDOList =
                    warehouseStockDubboCmdExe.LockAndReturnInStockList(itemId, autoReleaseLockList, warehouseIdList);
                for (int x = 0; x < warehouseInStockDOList.size(); x++) {
                    WarehouseInStockDO warehouseInStockDO = warehouseInStockDOList.get(x);
                    warehouseInStockDO.setNumReserved(warehouseInStockDO.getNumReserved() - count);
                    warehouseInStockDO.setUpdateTime(new Date());
                    warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
                }
                // VMI和在途不需要处理、释放锁
                warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WarehouseException(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_RESERVED_SET_ERROR);
        } finally {
            // 一定要释放锁
            warehouseStockDubboCmdExe.releaseLock(autoReleaseLockList);
        }

    }

    /**
     * 从redis同步数据到数据库()
     *
     * @param tenantNo
     */
    @Async
    public void stockRedisSyncMysqlJobHandler(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        try {
            List<WarehouseDO> warehouseDOList = warehouseServiceI.listWarehouse(null, null);
            // 分仓库同步
            warehouseDOList.forEach(warehouseDO -> {
                // 在库分页同步
                Integer page = 1;
                List<WarehouseInStockDO> inStockDOList = new ArrayList<>();
                while (page == 1
                    || (!CollectionUtils.isEmpty(inStockDOList) && inStockDOList.size() == config.getSyncStockNum())) {
                    PageHelper.startPage(page, config.getSyncStockNum());
                    inStockDOList = warehouseInStockDOMapper.listByCondition(warehouseDO.getId());
                    page++;
                    // 设置容量
                    Set<String> keySet = new HashSet<>(inStockDOList.size());
                    inStockDOList.forEach(inStockDO -> {
                        // key加入set里面
                        keySet
                            .add(TenantContext.getTenantNo() + ":" + warehouseDO.getId() + "_" + inStockDO.getItemId());
                    });
                    Map<String, WarehouseInStockDO> warehouseInStockDOMap = itemInStockCache.getAll(keySet);
                    if (!CollectionUtils.isEmpty(warehouseInStockDOMap)) {
                        // 转换为list
                        List<WarehouseInStockDO> list = new ArrayList<>(warehouseInStockDOMap.values());
                        warehouseInStockServiceI.updateList(list);
                    }
                }
            });
            // VMI分页同步
            Integer vmiPage = 1;
            List<GoodsVmiStockDO> vimStockDOList = new ArrayList<>();
            while (vmiPage == 1
                || (!CollectionUtils.isEmpty(vimStockDOList) && vimStockDOList.size() == config.getSyncStockNum())) {
                PageHelper.startPage(vmiPage, config.getSyncStockNum());
                vimStockDOList = goodsVmiStockDOMapper.listAll();
                vmiPage++;
                // 设置容量
                Set<String> keySet = new HashSet<>(vimStockDOList.size());
                vimStockDOList.forEach(vimStock -> {
                    // key加入set里面
                    keySet.add(TenantContext.getTenantNo() + ":" + vimStock.getItemId());
                    Map<String, GoodsVmiStockDO> goodsVmiStockDOMap = goodsVmiStockDOCache.getAll(keySet);
                    if (!CollectionUtils.isEmpty(goodsVmiStockDOMap)) {
                        // 转换为list
                        List<GoodsVmiStockDO> list = new ArrayList<>(goodsVmiStockDOMap.values());
                        goodsVmiStockServiceI.updateList(list);
                    }
                });
            }
            List<WarehouseInStockDO> warehouseInStockDOList =
                warehouseInStockServiceI.listByCondition(warehouseDOList.get(0).getId());
            LOGGER.info("查询回来的库存列表长度:{}", warehouseInStockDOList.size());
            List<WarehouseInStockDO> updateList = new ArrayList<>();
            Set<String> itemSet = new HashSet<>(warehouseInStockDOList.size());
            for (int x = 0; x < warehouseDOList.size(); x++) {
                WarehouseDO warehouseDO = warehouseDOList.get(x);
                // 按分割参数分割开
                List<List<WarehouseInStockDO>> partition =
                    Lists.partition(warehouseInStockDOList, config.getSyncStockNum());
                for (List<WarehouseInStockDO> warehouseInStockDOS : partition) {
                    // 设置容量
                    Set<String> keySet = new HashSet<>(warehouseInStockDOS.size());
                    // 遍历仓库
                    for (WarehouseInStockDO warehouseInStockDO : warehouseInStockDOS) {
                        // key加入set里面
                        keySet.add(TenantContext.getTenantNo() + ":" + warehouseDO.getId() + "_"
                            + warehouseInStockDO.getItemId());
                        if (x == 0) {
                            itemSet.add(TenantContext.getTenantNo() + ":" + warehouseInStockDO.getItemId());
                        }
                    }
                    Map<String, WarehouseInStockDO> warehouseInStockDOMap = itemInStockCache.getAll(keySet);
                    if (!CollectionUtils.isEmpty(warehouseInStockDOMap)) {
                        LOGGER.info("缓存的redis库存长度为0：keySet{}", JSON.toJSONString(keySet));
                    } else {
                        LOGGER.info("map:{}", warehouseInStockDOMap.size());
                    }
                    // 塞到数据库
                    List<WarehouseInStockDO> list = new ArrayList<>(warehouseInStockDOMap.values());
                    if (list.size() > 0) {
                        updateList.addAll(list);
                    }
                    Map<String, GoodsVmiStockDO> goodsVmiStockDOMap = goodsVmiStockDOCache.getAll(itemSet);
                    if (goodsVmiStockDOMap != null && goodsVmiStockDOMap.size() > 0) {
                        List<GoodsVmiStockDO> goodsVmiStockDOList = new ArrayList<>(goodsVmiStockDOMap.values());
                        goodsVmiStockServiceI.updateList(goodsVmiStockDOList);
                    }
                    // 批量修改数据量
                    if (updateList.size() == 0) {
                        return;
                    }
                    LOGGER.info("开始批量修改mysql库存");
                    // 分批保存 数据量太大
                    try {
                        warehouseInStockServiceI.updateList(updateList);
                    } catch (Exception e) {
                        LOGGER.info("updateList:{}", JSON.toJSONString(updateList));
                        throw e;
                    }
                }
            }
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    @Async
    public void initGoodsStock(String tenantNo, GoodsMsgRpcDTO goodsMsgRpcDTO) {
        TenantContext.setTenantNo(tenantNo);
        try {
            // 参数校验、得到货品的ERPid列表
            List<Long> itemErpIdList = WarehouseStockDubboValidator.checkItemErp(goodsMsgRpcDTO);
            List<WarehouseDO> warehouseList = warehouseServiceI.listUsableWarehouse(null);
            if (warehouseList == null || warehouseList.size() == 0) {
                throw WarehouseDubboApiException.buildException(WarehouseErrorCode.W_WAREHOUSE_NONE_ENABLE_ERROR);
            }
            // 先查询在库库存
            Map<String, InquiryInfo> instockMap = new HashMap<>();
            warehouseList.stream().forEach(warehouse -> {
                List<InquiryInfo> resultList =
                    erpStockQryExe.queryErpStock(warehouse.getWarehouseNo(), itemErpIdList, itemErpIdList.size());
                if (resultList != null && resultList.size() > 0) {
                    resultList.stream().forEach(inquiryInfo -> {
                        // 仓库id+erpid
                        instockMap.put(warehouse.getId() + "_" + inquiryInfo.getFBASEQTY(), inquiryInfo);
                    });
                }
            });
            // 设置在库库存
            Map<Integer, List<WarehouseInStockDO>> inStockDOSMap =
                warehouseInStockCmdExe.initGoodsStock(instockMap, goodsMsgRpcDTO, warehouseList);
            // 设置VMI库存
            List<InquiryInfo> vmiList = erpStockQryExe.queryErpStock(configQry.getTenantErpConfig().getVmiWarehouse(),
                itemErpIdList, itemErpIdList.size());
            goodsVmiStockCmdExe.initGoodsStock(vmiList, goodsMsgRpcDTO);
            // 设置缺货标记
            List<GoodsStockFlagDTO> goodsStockFlagDTOList = new ArrayList<>();
            goodsMsgRpcDTO.getItemErpDTOList().stream().forEach(goodsItemErpDTO -> {
                List<WarehouseInStockDO> warehouseInStockDOList = inStockDOSMap.get(goodsItemErpDTO.getItemId());
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(goodsItemErpDTO.getItemId());
                WarehouseInStockConvertor.getGoodsStockFlagDTOList(warehouseInStockDOList, goodsVmiStockDO,
                    goodsStockFlagDTOList);
            });
            goodsStockFlagServiceRpc.createList(goodsStockFlagDTOList);
        } catch (WarehouseDubboApiException e) {
            e.printStackTrace();
            LOGGER.error("货品初始化库存异常", e.getMsg());
            throw new WarehouseException(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("货品库存初始化系统异常", e.getMessage());
            throw new WarehouseException(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_INIT_EXCEPTION);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 根据ERP库存变更，同步变更B2B库存（只更新Redis缓存）
     *
     * @param stockChangeDTOS
     */
    @Async
    public void changeGoodsStockByErp(String tenantNo, List<ErpItemStockChangeDTO> stockChangeDTOS) {
        TenantContext.setTenantNo(tenantNo);
        try {
            PlatformTenantErpRpcDTO erpConfig = configQry.getTenantErpConfig();
            List<String> poInboundTypes = new ArrayList<>();
            if (!StringUtils.isEmpty(erpConfig.getPoInboundType())) {
                poInboundTypes.addAll(Arrays.asList(erpConfig.getPoInboundType().split(",")));
            }
            stockChangeDTOS.forEach(stockChangeDTO -> {
                // 分布式锁
                AutoReleaseLock inAutoReleaseLock = null;
                try {
                    // 更新在库库存时需锁定对应的在库缓存数据
                    inAutoReleaseLock =
                        stockkeyCache.tryLock(
                            TenantContext.getTenantNo() + ":" + WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE
                                + stockChangeDTO.getWarehouseId() + "_" + stockChangeDTO.getItemId(),
                            10, TimeUnit.SECONDS);
                    // 如果在库库存正被锁定情况，等到1秒继续获取
                    while (inAutoReleaseLock == null) {
                        Thread.sleep(1000);
                        inAutoReleaseLock = stockkeyCache.tryLock(
                            TenantContext.getTenantNo() + ":" + WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE
                                + stockChangeDTO.getWarehouseId() + "_" + stockChangeDTO.getItemId(),
                            10, TimeUnit.SECONDS);
                    }
                    // 重新获取库存(优先从redis重新获取)
                    WarehouseInStockDO warehouseInStockDO = warehouseInStockServiceI
                        .getByWarehouseIdAndItemId(stockChangeDTO.getWarehouseId(), stockChangeDTO.getItemId());
                    // 库存更新
                    // 在库库存
                    warehouseInStockDO
                        .setNumInWarehouse(warehouseInStockDO.getNumInWarehouse() + stockChangeDTO.getNum());
                    if (warehouseInStockDO.getNumInWarehouse() < 0) {
                        warehouseInStockDO.setNumInWarehouse(0);
                    }
                    // 如果库存单据类型为采购入库单，还需扣减在途库存
                    if (!StringUtils.isEmpty(stockChangeDTO.getStockBillType())
                        && poInboundTypes.contains(stockChangeDTO.getStockBillType())) {
                        warehouseInStockDO.setNumOnWay(warehouseInStockDO.getNumOnWay() - stockChangeDTO.getNum());
                        if (warehouseInStockDO.getNumOnWay() < 0) {
                            warehouseInStockDO.setNumOnWay(0);
                        }
                    }
                    warehouseInStockServiceI.updateRedisCacheOnly(warehouseInStockDO);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 最终都需要释放锁
                    if (inAutoReleaseLock != null) {
                        inAutoReleaseLock.close();
                    }
                }
            });
            // 发送刷新货品是否有库存消息
            List<Integer> itemIds =
                stockChangeDTOS.stream().map(ErpItemStockChangeDTO::getItemId).collect(Collectors.toList());
            messageSendService.freshItemUnderStockFlag(itemIds);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 初始化库存（停用的库存删除缓存数据，删除的货品删除库存和缓存数据）
     *
     * @param tenantNo
     */
    @Async
    public void initStock(String tenantNo, AutoReleaseLock autoReleaseLock, List<WarehouseDO> warehouseDOS,
        List<GoodsItemRpc> goodsItems) {
        TenantContext.setTenantNo(tenantNo);
        try {
            List<Integer> itemIds = goodsItems.stream().map(GoodsItemRpc::getItemId).collect(Collectors.toList());
            // 获取不存在货品（被删除的货品）的在途在库库存数据
            List<WarehouseInStockDO> noItemInStocks = warehouseInStockQryExe.getNoItemInStocks(itemIds);
            if (!CollectionUtils.isEmpty(noItemInStocks)) {
                // 删除不存在货品在途在库库存数据(包括缓存数据)
                noItemInStocks.forEach(noItemInStock -> {
                    warehouseInStockCmdExe.deleteById(noItemInStock.getId(),
                        noItemInStock.getWarehouseId() + "_" + noItemInStock.getItemId());
                });
            }
            // 获取不存在货品（被删除的货品）的VMI库存数据
            List<GoodsVmiStockDO> noItemVmiStock = goodsVmiStockQryExe.getNoItemVmiStock(itemIds);
            if (!CollectionUtils.isEmpty(noItemVmiStock)) {
                // 删除不存在货品VMI库存数据(包括缓存数据)
                noItemVmiStock.forEach(noItemInStock -> {
                    goodsVmiStockCmdExe.deleteByItemId(noItemInStock.getItemId());
                });
            }
            // 删历史所有在途数据
            List<GoodsOnWayStockDO> goodsOnWayStockDOS = goodsOnWayStockQryExe.listAll();
            if (!CollectionUtils.isEmpty(goodsOnWayStockDOS)) {
                goodsOnWayStockDOS.forEach(goodsOnWayStockDO -> {
                    goodsOnWayStockCmdExe.deleteByItemId(goodsOnWayStockDO.getItemId());
                });
            }
            if (!CollectionUtils.isEmpty(goodsItems)) {
                // 根据库存初始化未初始化的库存数据
                warehouseDOS.forEach(warehouseDO -> {
                    List<WarehouseInStockDO> warehouseInStockDOS =
                        warehouseInStockDOMapper.listByCondition(warehouseDO.getId());
                    List<Integer> exitItemIds = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(warehouseInStockDOS)) {
                        exitItemIds.addAll(warehouseInStockDOS.stream().map(WarehouseInStockDO::getItemId)
                            .collect(Collectors.toList()));
                    }
                    List<GoodsItemRpc> initGoodsItems = goodsItems.stream()
                        .filter(goodsItem -> !exitItemIds.contains(goodsItem.getItemId())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(initGoodsItems)) {
                        initNumZero(initGoodsItems, warehouseDO.getId());
                    }
                });
                // 根据VMi库存初始化未初始化的库存数据
                List<GoodsVmiStockDO> vmiStockDOS = goodsVmiStockQryExe.listAll();
                List<Integer> exitItemIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(vmiStockDOS)) {
                    exitItemIds
                        .addAll(vmiStockDOS.stream().map(GoodsVmiStockDO::getItemId).collect(Collectors.toList()));
                }
                List<GoodsItemRpc> initGoodsItems = goodsItems.stream()
                    .filter(goodsItem -> !exitItemIds.contains(goodsItem.getItemId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(initGoodsItems)) {
                    initVmiZero(initGoodsItems);
                }
            }
        } catch (Exception e) {
            LOGGER.error("初始化库存失败!");
            e.printStackTrace();
        } finally {
            if (autoReleaseLock != null) {
                autoReleaseLock.close();
            }
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 初始化在途在库库存
     *
     * @param initGoodsItems
     * @param warehouseId
     */
    private void initNumZero(List<GoodsItemRpc> initGoodsItems, Integer warehouseId) {
        List<WarehouseInStockDO> inStockDOS = new ArrayList<>();
        initGoodsItems.forEach(goodsItem -> {
            WarehouseInStockDO inStockDO = new WarehouseInStockDO();
            inStockDO.setWarehouseId(warehouseId);
            inStockDO.setGoodsId(goodsItem.getGoodsId());
            inStockDO.setItemId(goodsItem.getItemId());
            inStockDO.setItemErpId(goodsItem.getItemErpId());
            inStockDO.setNumInWarehouse(0);
            inStockDO.setNumOnWay(0);
            inStockDO.setNumInWarehouseLock(0);
            inStockDO.setNumOnWayLock(0);
            inStockDO.setNumReserved(0);
            inStockDO.setErpNumInWarehouse(0);
            inStockDO.setErpNumLock(0);
            inStockDO.setCreateTime(new Date());
            inStockDO.setUpdateTime(new Date());
            inStockDO.setItemName(goodsItem.getItemName());
            inStockDO.setItemCode(goodsItem.getItemCode());
            inStockDOS.add(inStockDO);
        });
        warehouseInStockDOMapper.insertList(inStockDOS);
    }

    /**
     * 初始化VMI库存
     */
    private void initVmiZero(List<GoodsItemRpc> initGoodsItems) {
        List<GoodsVmiStockDO> vmiStockDOS = new ArrayList<>();
        initGoodsItems.forEach(goodsItem -> {
            GoodsVmiStockDO goodsVmiStockDO = new GoodsVmiStockDO();
            goodsVmiStockDO.setGoodsId(goodsItem.getGoodsId());
            goodsVmiStockDO.setItemId(goodsItem.getItemId());
            goodsVmiStockDO.setItemErpId(goodsItem.getItemErpId());
            goodsVmiStockDO.setNumVmi(0);
            goodsVmiStockDO.setNumLock(0);
            goodsVmiStockDO.setCreateTime(new Date());
            goodsVmiStockDO.setUpdateTime(new Date());
            goodsVmiStockDO.setItemCode(goodsItem.getItemCode());
            goodsVmiStockDO.setItemName(goodsItem.getItemName());
            vmiStockDOS.add(goodsVmiStockDO);
        });
        goodsVmiStockDOMapper.insertList(vmiStockDOS);
    }
}
