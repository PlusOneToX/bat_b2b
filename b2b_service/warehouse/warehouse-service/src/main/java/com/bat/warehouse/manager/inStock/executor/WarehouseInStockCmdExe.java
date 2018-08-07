package com.bat.warehouse.manager.inStock.executor;

import static com.bat.warehouse.manager.common.constant.OrderGoodsStockConstant.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.manager.common.config.WarehouseConfig;
import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.inStock.convertor.WarehouseInStockConvertor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.bat.dubboapi.goods.stock.api.GoodsStockFlagServiceRpc;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.dubboapi.order.stock.api.OrderGoodsStockServiceRpc;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemErpDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.inStock.dto.GoodsItemStockFlagCmd;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.WarehouseInStockDOMapper;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.config.ConfigQry;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WarehouseInStockCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseInStockCmdExe.class);

    @Resource
    private WarehouseInStockDOMapper warehouseInStockDOMapper;

    @Resource
    private WarehouseServiceI warehouseServiceI;

    @Resource
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    @Resource
    private WarehouseInStockQryExe warehouseInStockQryExe;

    @Resource
    private WarehouseConfig config;

    @DubboReference(check = false, timeout = 2000000, retries = 0)
    private GoodsStockFlagServiceRpc goodsStockFlagServiceRpc;

    @DubboReference(check = false, timeout = 1000000, retries = 0)
    private OrderGoodsStockServiceRpc orderGoodsStockServiceRpc;

    @Resource
    private ConfigQry vmiConfigExe;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE)
    private Cache<String, WarehouseInStockDO> itemInStockCache;

    public void update(WarehouseInStockDO warehouseInStockDO) {
        warehouseInStockDOMapper.updateByPrimaryKeySelective(warehouseInStockDO);
        itemInStockCache.put(TenantContext.getTenantNo() + ":" + warehouseInStockDO.getWarehouseId() + "_"
            + warehouseInStockDO.getItemId(), warehouseInStockDO);
    }

    public Integer addNumInWarehouseLockById(Integer numLock, Integer id) {
        return warehouseInStockDOMapper.addNumInWarehouseLockById(numLock, id);
    }

    public Map<Integer, List<WarehouseInStockDO>> initGoodsStock(Map<String, InquiryInfo> instockMap,
        GoodsMsgRpcDTO goodsMsgRpcDTO, List<WarehouseDO> warehouseList) {
        List<GoodsItemErpDTO> itemErpDTOList = goodsMsgRpcDTO.getItemErpDTOList();
        Map<Integer, List<WarehouseInStockDO>> inStockDOSMap = new HashMap<>();
        for (int x = 0; x < itemErpDTOList.size(); x++) {
            // 设置在库库存
            GoodsItemErpDTO goodsItemErpDTO = itemErpDTOList.get(x);
            for (int y = 0; y < warehouseList.size(); y++) {
                WarehouseDO warehouseDO = warehouseList.get(y);
                // 根据仓库id和货品id查询ERP返回数据
                InquiryInfo inquiryInfo = instockMap.get(warehouseDO.getId() + "_" + goodsItemErpDTO.getItemErpId());
                WarehouseInStockDO warehouseInStockDO = new WarehouseInStockDO();
                warehouseInStockDO.setWarehouseId(warehouseDO.getId());
                warehouseInStockDO.setGoodsId(goodsMsgRpcDTO.getGoodsId());
                warehouseInStockDO.setItemId(goodsItemErpDTO.getItemId());
                warehouseInStockDO.setItemErpId(goodsItemErpDTO.getItemErpId());
                warehouseInStockDO.setNumInWarehouseLock(0);
                warehouseInStockDO.setNumOnWayLock(0);
                warehouseInStockDO.setNumInWarehouse(0);
                warehouseInStockDO.setNumOnWay(0);
                warehouseInStockDO.setErpNumInWarehouse(0);
                warehouseInStockDO.setNumReserved(0);
                warehouseInStockDO.setErpNumLock(0);
                warehouseInStockDO.setCreateTime(new Date());
                warehouseInStockDO.setUpdateTime(new Date());
                if (inquiryInfo != null) {
                    warehouseInStockDO.setNumInWarehouse(inquiryInfo.getPREDICTQTY().intValue());
                    warehouseInStockDO.setErpNumInWarehouse(inquiryInfo.getFBASEQTY().intValue());
                    warehouseInStockDO.setNumOnWay(inquiryInfo.getQuantityInTransit().intValue());
                }
                warehouseInStockDO.setItemCode(goodsItemErpDTO.getItemCode());
                warehouseInStockDO.setItemName(goodsItemErpDTO.getItemName());
                try {
                    warehouseInStockDOMapper.insert(warehouseInStockDO);
                } catch (DuplicateKeyException e) {
                    continue;
                }
                List<WarehouseInStockDO> inStockDOS = inStockDOSMap.get(warehouseInStockDO.getItemId());
                if (inStockDOS == null) {
                    inStockDOS = new ArrayList<>();
                    inStockDOSMap.put(goodsItemErpDTO.getItemId(), inStockDOS);
                }
                inStockDOS.add(warehouseInStockDO);
                // 设置到缓存
                itemInStockCache.put(
                    TenantContext.getTenantNo() + ":" + warehouseDO.getId() + "_" + warehouseInStockDO.getItemId(),
                    warehouseInStockDO);
            }
        }
        return inStockDOSMap;
    }

    public void deleteById(Integer id, String redisKey) {
        warehouseInStockDOMapper.deleteByPrimaryKey(id);
        // 移除缓存
        WarehouseInStockDO warehouseInStockDO = itemInStockCache.get(TenantContext.getTenantNo() + ":" + redisKey);
        if (warehouseInStockDO != null) {
            itemInStockCache.remove(TenantContext.getTenantNo() + ":" + redisKey);
        }
    }

    /**
     * 批量删除库存数据（包括缓存数据）
     * 
     * @param inStockDOS
     */
    public void deleteList(List<WarehouseInStockDO> inStockDOS) {
        Set<String> redisKeys = new HashSet<>();
        List<Integer> itemIds = new ArrayList<>();
        inStockDOS.forEach(inStockDO -> {
            itemIds.add(inStockDO.getItemId());
            redisKeys.add(TenantContext.getTenantNo() + ":" + inStockDO.getWarehouseId() + "_" + inStockDO.getItemId());
        });
        itemInStockCache.removeAll(redisKeys);
        warehouseInStockDOMapper.deleteByIds(itemIds);
    }

    public void create(WarehouseInStockDO warehouseInStockDO) {
        warehouseInStockDOMapper.insert(warehouseInStockDO);
        itemInStockCache.put(TenantContext.getTenantNo() + ":" + warehouseInStockDO.getWarehouseId() + "_"
            + warehouseInStockDO.getItemId(), warehouseInStockDO);
    }

    /**
     * 移除库存缓存数据
     *
     * @param warehouseInStockDO
     */
    public void removeStockRedisCache(WarehouseInStockDO warehouseInStockDO) {
        String key = TenantContext.getTenantNo() + ":" + warehouseInStockDO.getWarehouseId() + "_"
            + warehouseInStockDO.getItemId();
        LOGGER.info("删除缓存货品库存：" + JSON.toJSONString(warehouseInStockDO));
        itemInStockCache.remove(key);
    }

    /**
     * 不保存mysql
     * 
     * @param warehouseInStockDO
     */
    public void updateRedisCacheOnly(WarehouseInStockDO warehouseInStockDO) {
        String key = TenantContext.getTenantNo() + ":" + warehouseInStockDO.getWarehouseId() + "_"
            + warehouseInStockDO.getItemId();
        // LOGGER.info("更新缓存货品库存：" + JSON.toJSONString(warehouseInStockDO));
        itemInStockCache.put(key, warehouseInStockDO);
    }

    /**
     * 批量修改mysql数据（不更新缓存）
     * 
     * @param updateList
     */
    public void updateList(List<WarehouseInStockDO> updateList) {
        warehouseInStockDOMapper.updateList(updateList);
    }

    /**
     * 刷新商品是否有库存
     */
    @Async
    public void freshItemUnderStockFlag(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        try {
            // 在库在途同步（所有仓库）
            List<WarehouseDO> warehouseList = warehouseServiceI.listUsableWarehouse(null);
            if (CollectionUtils.isEmpty(warehouseList)) {
                return;
            }
            Integer page = 1;
            List<Integer> itemIds = new ArrayList<>();
            while (page == 1 || (!CollectionUtils.isEmpty(itemIds) && itemIds.size() == config.getSyncStockNum())) {
                PageHelper.startPage(page, config.getSyncStockNum());
                itemIds = warehouseInStockDOMapper.allItemId();
                page++;
                freshItemUnderStockFlag(warehouseList, itemIds);
            }
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 刷新商品是否有库存
     */
    @Async
    public void freshItemUnderStockFlag(String tenantNo, List<Integer> itemIds) {
        TenantContext.setTenantNo(tenantNo);
        try {
            // 在库在途同步（所有仓库）
            List<WarehouseDO> warehouseList = warehouseServiceI.listUsableWarehouse(null);
            if (CollectionUtils.isEmpty(warehouseList)) {
                return;
            }
            freshItemUnderStockFlag(warehouseList, itemIds);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 根据货品和仓库刷新是否有库存
     * 
     * @param warehouseList
     * @param itemIds
     */
    public void freshItemUnderStockFlag(List<WarehouseDO> warehouseList, List<Integer> itemIds) {
        List<GoodsStockFlagDTO> goodsStockFlagDTOList = new ArrayList<>();
        itemIds.forEach(itemId -> {
            List<WarehouseInStockDO> warehouseInStockDOS = new ArrayList<>();
            warehouseList.forEach(warehouseDO -> {
                WarehouseInStockDO warehouseInStockDO =
                    warehouseInStockQryExe.getByWarehouseIdAndItemId(warehouseDO.getId(), itemId);
                if (warehouseInStockDO != null) {
                    warehouseInStockDOS.add(warehouseInStockDO);
                }
            });
            GoodsVmiStockDO vmiStockDO = goodsVmiStockServiceI.getByItemId(itemId);
            // 排列组合
            WarehouseInStockConvertor.getGoodsStockFlagDTOList(warehouseInStockDOS, vmiStockDO, goodsStockFlagDTOList);
        });
        goodsStockFlagServiceRpc.updateList(goodsStockFlagDTOList);
    }

    /**
     * 刷新货品库存是否缺货标识
     * 
     * @param tenantNo
     * @param goodsItemStockFlagCmd
     */
    @Async
    public void dealwithGoodsUnderStockFlag(String tenantNo, List<WarehouseDO> warehouseDOS,
        GoodsItemStockFlagCmd goodsItemStockFlagCmd) {
        TenantContext.setTenantNo(tenantNo);
        try {
            List<GoodsStockFlagDTO> goodsStockFlagDTOList = new ArrayList<>();
            goodsItemStockFlagCmd.getDetailCmdList().forEach(goodsItemStockFlagDetailCmd -> {
                // 在库锁库涉及的在库库存列表
                List<WarehouseInStockDO> warehouseInStockDOList =
                    goodsItemStockFlagDetailCmd.getWarehouseInStockDOList();
                warehouseDOS.forEach(warehouseDO -> {
                    // 判断在库锁库列表是否缺漏（可能三个仓库、但锁库只有2个、需要将剩余一个加上去）
                    Boolean flag = false;
                    for (int y = 0; y < warehouseInStockDOList.size(); y++) {
                        if (warehouseDO.getId().equals(warehouseInStockDOList.get(y).getWarehouseId())) {
                            // 存在
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        WarehouseInStockDO warehouseInStockDO =
                            getByWarehouseIdAndItemId(warehouseDO.getId(), goodsItemStockFlagDetailCmd.getItemId());
                        if (warehouseInStockDO != null) {
                            warehouseInStockDOList.add(warehouseInStockDO);
                        }
                    }
                });
                // 排列组合
                WarehouseInStockConvertor.getGoodsStockFlagDTOList(warehouseInStockDOList,
                    goodsItemStockFlagDetailCmd.getGoodsVmiStockDO(), goodsStockFlagDTOList);
            });
            goodsStockFlagServiceRpc.updateList(goodsStockFlagDTOList);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    public WarehouseInStockDO getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId) {
        WarehouseInStockDO warehouseInStockDO = warehouseInStockQryExe.getByWarehouseIdAndItemId(warehouseId, itemId);
        if (warehouseInStockDO == null) {
            return null;
        }
        boolean updateRedis = false;
        // 处理redis的数据缺少id的
        if (warehouseInStockDO.getId() == null) {
            // 删除异常redis缓存数据
            removeStockRedisCache(warehouseInStockDO);
            warehouseInStockDO = warehouseInStockQryExe.getByWarehouseIdAndItemId(warehouseId, itemId);
            updateRedis = true;
        }
        // 解决属性为null 的情况
        if (warehouseInStockDO.getNumInWarehouse() == null) {
            warehouseInStockDO.setNumInWarehouse(0);
            updateRedis = true;
        }
        if (warehouseInStockDO.getNumInWarehouseLock() == null) {
            warehouseInStockDO.setNumInWarehouseLock(0);
            updateRedis = true;
        }
        if (warehouseInStockDO.getNumOnWay() == null) {
            warehouseInStockDO.setNumOnWay(0);
            updateRedis = true;
        }
        if (warehouseInStockDO.getNumOnWayLock() == null) {
            warehouseInStockDO.setNumOnWayLock(0);
            updateRedis = true;
        }
        if (warehouseInStockDO.getNumReserved() == null) {
            warehouseInStockDO.setNumReserved(0);
            updateRedis = true;
        }
        if (updateRedis) {
            updateRedisCacheOnly(warehouseInStockDO);
        }
        return warehouseInStockDO;
    }

    /**
     * 根据货品id重置锁库数量
     * 
     * @param itemId
     */
    public void resetLockStock(Integer itemId) {
        List<Integer> itemIds = new ArrayList<>();
        itemIds.add(itemId);
        List<WarehouseDO> warehouseDOS = warehouseServiceI.listUsableWarehouse(null);
        if (!CollectionUtils.isEmpty(warehouseDOS)) {
            restWarehouseLockStock(warehouseDOS, itemIds);
        }
    }

    /**
     * 初始化库存锁库数据
     */
    public void initResetLockStock() {
        List<WarehouseDO> warehouseDOS = warehouseServiceI.listUsableWarehouse(null);
        if (!CollectionUtils.isEmpty(warehouseDOS)) {
            Integer page = 1;
            List<Integer> itemIds = new ArrayList<>();
            while (page == 1 || (!CollectionUtils.isEmpty(itemIds) && itemIds.size() == config.getSyncStockNum())) {
                PageHelper.startPage(page, config.getSyncStockNum());
                itemIds = warehouseInStockDOMapper.allItemId();
                page++;
                restWarehouseLockStock(warehouseDOS, itemIds);
            }
        }
    }

    /**
     * 根据仓库id和货品ids重置库存锁定
     *
     * @param itemIds
     */
    public void restWarehouseLockStock(List<WarehouseDO> warehouseDOS, List<Integer> itemIds) {
        com.bat.dubboapi.order.common.Response<List<OrderGoodsStockSimpleDTO>> response =
            orderGoodsStockServiceRpc.sumByItemIds(itemIds);
        Date date = new Date();
        List<OrderGoodsStockSimpleDTO> goodsStockSimpleDTOList = response.getData();
        Map<Integer, List<OrderGoodsStockSimpleDTO>> goodsStockSimpleDTOSMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goodsStockSimpleDTOList)) {
            // 根据货品id分组
            goodsStockSimpleDTOSMap.putAll(
                goodsStockSimpleDTOList.stream().collect(Collectors.groupingBy(OrderGoodsStockSimpleDTO::getItemId)));
        }
        itemIds.forEach(itemId -> {
            Map<Integer, WarehouseInStockDO> inStockDOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(warehouseDOS)) {
                // 初始化货品所有仓库在途在库的锁库记录为零
                warehouseDOS.forEach(warehouseDO -> {
                    WarehouseInStockDO warehouseInStockDO = getByWarehouseIdAndItemId(warehouseDO.getId(), itemId);
                    if (warehouseInStockDO != null) {
                        warehouseInStockDO.setUpdateTime(date);
                        warehouseInStockDO.setNumOnWayLock(0);
                        warehouseInStockDO.setNumInWarehouseLock(0);
                        if (warehouseInStockDO != null) {
                            inStockDOMap.put(warehouseDO.getId(), warehouseInStockDO);
                        }
                    }
                });
            }
            // 初始化VMI库存锁库记录为零
            GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(itemId);
            if (goodsVmiStockDO != null) {
                goodsVmiStockDO.setUpdateTime(date);
                goodsVmiStockDO.setNumLock(0);
            }
            List<OrderGoodsStockSimpleDTO> orderGoodsStockSimpleDTOS = goodsStockSimpleDTOSMap.get(itemId);
            if (!CollectionUtils.isEmpty(orderGoodsStockSimpleDTOS)) {
                // 根据仓库id分组
                Map<Integer, List<OrderGoodsStockSimpleDTO>> orderGoodsStocksWarehouseMap = orderGoodsStockSimpleDTOS
                    .stream().collect(Collectors.groupingBy(OrderGoodsStockSimpleDTO::getWarehouseId));
                orderGoodsStocksWarehouseMap.forEach((k1, v1) -> {
                    // 根据锁库类型分组
                    Map<Short, List<OrderGoodsStockSimpleDTO>> orderGoodsStocksLockTypeMap =
                        v1.stream().collect(Collectors.groupingBy(OrderGoodsStockSimpleDTO::getLockType));
                    orderGoodsStocksLockTypeMap.forEach((k2, v2) -> {
                        // 汇总在库锁库
                        AtomicReference<Integer> lockNum = new AtomicReference<>(0);
                        v2.forEach(orderGoodsStock -> {
                            lockNum.set(lockNum.get() + orderGoodsStock.getLockNum());
                        });
                        // 在途在库库存数据
                        WarehouseInStockDO warehouseInStockDO = inStockDOMap.get(k1);
                        if (k2.equals(LOCK_TYPE_INSTOCK)) {// 在库
                            if (warehouseInStockDO != null) {
                                warehouseInStockDO.setNumInWarehouseLock(lockNum.get());
                            }
                        } else if (k2.equals(LOCK_TYPE_ONWAY)) {// 在途
                            if (warehouseInStockDO != null) {
                                warehouseInStockDO.setNumOnWayLock(lockNum.get());
                            }
                        } else if (k2.equals(LOCK_TYPE_VMI)) {// VMI
                            if (goodsVmiStockDO != null) {
                                goodsVmiStockDO.setNumLock(lockNum.get());
                            }
                        }
                    });
                });
            }
            // 更新在途在库库存数据
            if (!CollectionUtils.isEmpty(inStockDOMap)) {
                inStockDOMap.forEach((k, v) -> {
                    updateRedisCacheOnly(v);
                });
            }
            if (goodsVmiStockDO != null) {
                goodsVmiStockServiceI.updateRedisCacheOnly(goodsVmiStockDO);
            }
        });
    }

}
