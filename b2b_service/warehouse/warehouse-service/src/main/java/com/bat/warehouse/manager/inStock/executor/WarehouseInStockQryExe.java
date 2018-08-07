package com.bat.warehouse.manager.inStock.executor;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseInStockErrorCode;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.util.concurrent.RateLimiter;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.WarehouseInStockDOMapper;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventoryCO;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

@Component
public class WarehouseInStockQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseInStockQryExe.class);

    @Autowired
    private WarehouseInStockDOMapper warehouseInStockDOMapper;

    // @Autowired
    // private GoodsOnWayStockQryExe goodsOnWayStockQryExe;

    @Autowired
    private GoodsVmiStockQryExe goodsVmiStockQryExe;

    // @Autowired
    // private GoodsOnWayStockServiceI goodsOnWayStockServiceI;

    @Autowired
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    // @Autowired
    // private WarehouseServiceI warehouseServiceI;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE)
    private Cache<String, WarehouseInStockDO> itemInStockCache;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    /*  public GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdList(Integer itemId, List<Integer> areaIdList) {
        GoodsItemInventorySummaryCO summaryCO = warehouseInStockDOMapper.summaryByItemIdAndAreaIdList(itemId,areaIdList);
        if(summaryCO ==null || summaryCO.getItemId()==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
        }
         return  summaryCO;
    }*/

    public List<WarehouseInStockDO> listByItemIdAndWarehouseIdList(Integer itemId, List<Integer> warehouseIdList) {
        return warehouseInStockDOMapper.listByItemIdAndWarehouseIdList(itemId, warehouseIdList);
    }

    @Cached(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE, key = "#warehouseId+'_'+#itemId")
    public WarehouseInStockDO getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId) {
        WarehouseInStockDO warehouseInStockDO = warehouseInStockDOMapper.getByWarehouseIdAndItemId(warehouseId, itemId);
        return warehouseInStockDO;
    }

    public List<WarehouseInStockDO> listByGoodsIdsAndWarehouseIdList(List<Integer> goodsIds,
        List<Integer> warehouseIdList) {
        return warehouseInStockDOMapper.listByGoodsIdsAndWarehouseIdList(goodsIds, warehouseIdList);
    }

    /**
     * 根据货品id和在库仓库id列表查询货品可下单等各种数量
     * 
     * @param itemId
     * @param warehouseIdList
     * @return
     */
    public GoodsItemInventorySummaryCO summaryCoByItemId(Integer itemId, List<Integer> warehouseIdList) {
        GoodsItemInventorySummaryCO inventoryCO =
            warehouseInStockDOMapper.summaryByItemIdAndWarehouseIdList(itemId, warehouseIdList);
        // GoodsOnWayStockDO goodsOnWayStockDO = goodsOnWayStockQryExe.findByItemId(itemId);
        inventoryCO.setNumOnWay(inventoryCO.getNumOnWay());
        GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockQryExe.getByItemId(itemId);
        inventoryCO.setNumVmi(goodsVmiStockDO.getNumVmi());
        // 设置预留
        inventoryCO.setNumReservedSum(inventoryCO.getNumReservedSum());
        // 在库可下单量 = 在库数量+VMI数量-在库锁定-在库预留-VMI锁定-在途锁定
        Integer inStockCount =
            inventoryCO.getNumInWarehouseSum() + goodsVmiStockDO.getNumVmi() - inventoryCO.getNumInWarehouseLockSum()
                - inventoryCO.getNumReservedSum() - goodsVmiStockDO.getNumLock() - inventoryCO.getNumOnWayLockSum();
        inventoryCO.setInStockUsableCount(inStockCount);
        // 在途可下单量 =在库可下单量+在途数量
        inventoryCO.setOnWayUsableCount(inStockCount + inventoryCO.getNumOnWay());
        inventoryCO.setNumLockSum(
            inventoryCO.getNumInWarehouseLockSum() + inventoryCO.getNumOnWayLockSum() + goodsVmiStockDO.getNumLock());
        return inventoryCO;
    }

    /**
     * 根据货品id和在库仓库id列表查询货品可下单等各种数量
     * 
     * @param itemId
     *            货品id
     * @param areaIdList
     *            分销商所属区域id列表
     * @param warehouseIdList
     *            分销商所属在库仓库id列表
     * @return
     */
    public GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdListAndWarehouseIdList(Integer itemId,
        List<Integer> areaIdList, List<Integer> warehouseIdList) {
        if ((areaIdList == null || areaIdList.size() == 0)
            && (warehouseIdList == null || warehouseIdList.size() == 0)) {
            throw new WarehouseException(WarehouseInStockErrorCode.W_AREAIDLIST_AND_WAREHOUSEIDLIST_NULL);
        }
        if (itemId == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        GoodsItemInventorySummaryCO inventoryCO = null;
        if (warehouseIdList == null || warehouseIdList.size() == 0) {
            inventoryCO = warehouseInStockDOMapper.summaryByItemIdAndAreaIdList(itemId, areaIdList);
        } else {
            // 根据货品id和在库仓库id列表汇总查询
            inventoryCO = warehouseInStockDOMapper.summaryByItemIdAndWarehouseIdList(itemId, warehouseIdList);
        }
        setStockInventoryParam(inventoryCO, null);
        return inventoryCO;
    }

    /**
     * 组装库存参数
     * 
     * @param summaryCO
     * @param noSupportOnWayMap
     *            不支持在途库存集合
     * @return
     */
    public void setStockInventoryParam(GoodsItemInventorySummaryCO summaryCO, Map<Integer, Boolean> noSupportOnWayMap) {
        GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(summaryCO.getItemId());
        summaryCO.setNumVmi(goodsVmiStockDO.getNumVmi());
        summaryCO.setNumLockSum(
            summaryCO.getNumInWarehouseLockSum() + goodsVmiStockDO.getNumLock() + summaryCO.getNumOnWayLockSum());
        // 设置预留和
        // 在库可下单量 = 在库库存和+VMI数量 - 在库锁定-在库预留-VMI锁定-在途锁定
        Integer inStockCount =
            summaryCO.getNumInWarehouseSum() + goodsVmiStockDO.getNumVmi() - summaryCO.getNumInWarehouseLockSum()
                - summaryCO.getNumReservedSum() - goodsVmiStockDO.getNumLock() - summaryCO.getNumOnWayLockSum();
        summaryCO.setInStockUsableCount(inStockCount);
        summaryCO.setOnWayUsableCount(inStockCount + summaryCO.getNumOnWaySum());
        // 在途可下单量 = 在库可下单量+在途数量
        if (noSupportOnWayMap != null && noSupportOnWayMap.containsKey(summaryCO.getItemId())) {
            // 不支持在途、在途可下单量=0(重构跟之前不一样了)
            summaryCO.setOnWayUsableCount(0);
        }

        // 负数改为置0
        if (summaryCO.getInStockUsableCount() < 0) {
            summaryCO.setInStockUsableCount(0);
        }
        if (summaryCO.getOnWayUsableCount() < 0) {
            summaryCO.setOnWayUsableCount(0);
        }
    }

    public WarehouseInStockDO findByItemIdAndWarehouseNo(Integer itemId, String warehouseNo) {
        return warehouseInStockDOMapper.findByItemIdAndWarehouseNo(itemId, warehouseNo);
    }

    private static Long time = 1l;

    public void testLock(Integer index) {
        time = time + 1L;
        testRateLimiter(index.longValue());
        /*Integer times =0;
        while (times<5){
            Boolean flag =cache.tryLockAndRun("HHAH1",5,TimeUnit.SECONDS,()->{
                System.out.println("获得线程、进行处理"+new Date());
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("休眠:"+new Date());
            });
            System.out.println("加锁："+flag+new Date());
            if(!flag){
                try {
                    times++;
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag =cache.tryLockAndRun("HHAH1",100,TimeUnit.SECONDS,()->{
                    System.out.println("进来了"+new Date());
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("休眠1秒:"+new Date());
                });
            }else{
                break;
            }
        }
        System.out.println("哈哈哈"+new Date());*/
    }

    /**
     * 令牌桶池
     */
    private static Map<String, RateLimiter> rateLimiterMap = new HashMap<>();

    private static synchronized RateLimiter getByKey(String key, Long index) {
        RateLimiter rateLimiter = rateLimiterMap.get(key);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(3);
            rateLimiterMap.put(key, rateLimiter);
        }
        LOGGER.info("令牌对象：" + index + "、" + System.identityHashCode(rateLimiter));
        return rateLimiter;
    }

    public void testRateLimiter(Long index) {
        String key = 51 + "_" + 10001;
        LOGGER.info("处理令牌测试流程" + index);
        RateLimiter rateLimiter = getByKey(key, index);
        /* if(rateLimiter ==null){
            rateLimiter = RateLimiter.create(1);
            rateLimiterMap.put(key, rateLimiter);
        }*/

        // LOGGER.info("令牌对象："+rateLimiter);
        if (!rateLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            // 拿不到令牌、
            LOGGER.error("无法获取令牌、限流:" + index);
            return;
        }
        LOGGER.info("开始执行令牌、扣减库存" + index);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("锁释放、执行完毕：" + index);
    }

    public static void main(String[] args) {
        String result = HttpUtil.getJson("localhost:8060/warehouse/v1/web/user/u/warehouse/stock/test");
        System.out.println(result);
        for (long x = 0; x < 100; x++) {
            long finalX = x;
            Thread thread = new Thread(() -> {
                try {
                    HttpUtil.getJson("localhost:8060/warehouse/v1/web/user/u/warehouse/stock/test");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            System.out.println(finalX);
            thread.start();
        }
    }

    public List<WarehouseInStockDO> listByCondition(Integer warehouseId) {
        return warehouseInStockDOMapper.listByCondition(warehouseId);
    }

    public List<GoodsItemInventoryCO> listCOByWarehouseIdAndContent(Integer warehouseId, String content) {
        return warehouseInStockDOMapper.listCOByWarehouseIdAndContent(warehouseId, content);
    }

    public List<WarehouseInStockDO> listByWarehouseIdListGroupByItemId(List<Integer> warehouseIdList) {
        return warehouseInStockDOMapper.listByWarehouseIdListGroupByItemId(warehouseIdList);
    }

    public List<WarehouseInStockDO> listByWarehouseIdAndItemCodeList(List<Integer> warehouseIdList,
        List<String> itemCodeList) {
        return warehouseInStockDOMapper.listByWarehouseIdAndItemCodeList(warehouseIdList, itemCodeList);
    }

    public List<WarehouseInStockDO> listInstockByRedisKeySet(Set<String> instockKeySet) {
        Set<String> instockSet = new HashSet<>();
        instockKeySet.stream().forEach(instockKey -> {
            instockSet.add(TenantContext.getTenantNo() + ":" + instockKey);
        });
        Map<String, WarehouseInStockDO> allStockMap = itemInStockCache.getAll(instockSet);
        if (allStockMap == null || allStockMap.size() == 0) {
            LOGGER.info("查询不到在库库存信息、{}", JSON.toJSONString(instockKeySet));
            throw new WarehouseException(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_ITEM_RELEVANCE_ERROR);
        }
        instockSet.stream().forEach(key -> {
            // 遍历查询是否都存在
            if (!allStockMap.containsKey(key)) {
                LOGGER.info("查询不到该货品何仓库的在库库存信息、key为{}", key);
                throw new WarehouseException(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_ITEM_RELEVANCE_ERROR);
            }
        });

        return new ArrayList<>(allStockMap.values());
    }

    public List<WarehouseInStockDO> incListInstockByRedisKeySet(Set<String> instockKeySet) {
        Set<String> instockSet = new HashSet<>();
        instockKeySet.stream().forEach(instockKey -> {
            instockSet.add(TenantContext.getTenantNo() + ":" + instockKey);
        });
        Map<String, WarehouseInStockDO> allStockMap = itemInStockCache.getAll(instockSet);
        if (allStockMap == null) {
            allStockMap = new HashMap<>();
        }
        return new ArrayList<>(allStockMap.values());
    }

    public List<WarehouseInStockDO> listByItemErpIdList(List<Long> itemErpIdList) {
        return warehouseInStockDOMapper.listByItemErpIdList(itemErpIdList);
    }

    public List<WarehouseInStockDO> listByItemIdsAndWarehouseIds(List<Integer> itemIds, List<Integer> warehouseIds) {
        return warehouseInStockDOMapper.listByItemIdsAndWarehouseIds(itemIds, warehouseIds);
    }

    /**
     * 根据已存在的货品id获取不存在的货品库存数据
     * 
     * @param itemIds
     * @return
     */
    public List<WarehouseInStockDO> getNoItemInStocks(List<Integer> itemIds) {
        return warehouseInStockDOMapper.getNoItemInStocks(itemIds);
    }
}
