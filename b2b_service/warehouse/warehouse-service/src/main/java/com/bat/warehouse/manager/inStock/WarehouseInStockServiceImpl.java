package com.bat.warehouse.manager.inStock;

import static com.bat.warehouse.manager.common.constant.OrderGoodsStockConstant.*;
import static com.bat.warehouse.manager.common.constant.WarehouseCommonConstant.COMMON_SYNC_TYPE_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.manager.common.config.WarehouseConfig;
import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseInStockErrorCode;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockCmdExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorBusinessRpcDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemOnwaySaleFlagRpcDTO;
import com.bat.dubboapi.goods.stock.api.GoodsStockFlagServiceRpc;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.dubboapi.order.stock.api.OrderGoodsStockServiceRpc;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.MessageUtils;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.inStock.dto.GoodsItemStockFlagCmd;
import com.bat.warehouse.api.inStock.dto.WarehouseInStockPageQry;
import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventoryCO;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.config.ConfigQry;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import com.bat.warehouse.manager.common.constant.external.DistributorConstant;
import com.bat.warehouse.manager.inStock.convertor.WarehouseInStockConvertor;
import com.bat.warehouse.manager.inStock.executor.ErpStockQryExe;
import com.bat.warehouse.manager.inStock.executor.WarehouseInStockCmdExe;
import com.bat.warehouse.manager.inStock.executor.WarehouseInStockQryExe;
import com.bat.warehouse.manager.inStock.executor.WarehouseStockSyncCmdExe;
import com.bat.warehouse.manager.warehouse.executor.WarehouseQryExe;

@Service
public class WarehouseInStockServiceImpl implements WarehouseInStockServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseInStockServiceImpl.class);

    @Autowired
    private WarehouseInStockQryExe warehouseInStockQryExe;

    @Autowired
    private WarehouseInStockCmdExe warehouseInStockCmdExe;

    @Resource
    private GoodsVmiStockCmdExe getGoodsVmiStockCmdExe;

    @Autowired
    private GoodsVmiStockServiceI goodsVmiStockServiceI;

    @Autowired
    private WarehouseServiceI warehouseServiceI;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 6000)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private ErpStockQryExe erpStockQryExe;

    @Lazy
    @Resource
    private WarehouseStockSyncCmdExe warehouseStockSyncCmdExe;

    @Resource
    private ConfigQry vmiConfigExe;

    @Autowired
    private GoodsVmiStockCmdExe goodsVmiStockCmdExe;

    @Autowired
    private WarehouseQryExe warehouseQryExe;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_LOCK_KEY_PRE)
    private Cache<String, String> stockkeyCache;

    @Resource
    private WarehouseConfig config;

    @DubboReference(check = false, timeout = 1000000, retries = 0)
    private OrderGoodsStockServiceRpc orderGoodsStockServiceRpc;

    @DubboReference(check = false, timeout = 2000000, retries = 0)
    private GoodsStockFlagServiceRpc goodsStockFlagServiceRpc;

    private static ExecutorService executor = Executors.newFixedThreadPool(20);

    /**
     * 同步ERP库存
     */
    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_SYNC_ERP_STOCK_KEY_PRE)
    private Cache<String, Integer> syncErpStockCache;

    @CreateCache(name = WarehouseKeyConstant.WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE)
    private Cache<String, WarehouseInStockDO> itemInStockCache;

    @Override
    public PageInfo<GoodsItemInventoryCO> page(WarehouseInStockPageQry warehouseInStockPageQry) {
        // redis取数据
        PageHelper.startPage(warehouseInStockPageQry.getPage(), warehouseInStockPageQry.getSize());
        List<GoodsItemInventoryCO> list = listCOByWarehouseIdAndContent(warehouseInStockPageQry.getWarehouseId(),
            warehouseInStockPageQry.getContent());
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(inventoryCO -> {
                // 取缓存的数据库覆盖mysql查询回来
                WarehouseInStockDO warehouseInStockDO =
                    getByWarehouseIdAndItemId(warehouseInStockPageQry.getWarehouseId(), inventoryCO.getItemId());
                if (warehouseInStockDO != null) {
                    inventoryCO.setNumInWarehouse(warehouseInStockDO.getNumInWarehouse());
                    inventoryCO.setNumOnWay(warehouseInStockDO.getNumOnWay());
                    inventoryCO.setNumReserved(warehouseInStockDO.getNumReserved());
                    inventoryCO.setNumInWarehouseLock(warehouseInStockDO.getNumInWarehouseLock());
                    inventoryCO.setNumOnWayLock(warehouseInStockDO.getNumOnWayLock());
                }
                // VMI库存数据
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(inventoryCO.getItemId());
                inventoryCO.setNumVmi(goodsVmiStockDO.getNumVmi());
                // 在库可下单量 在库数量+VMI数量-在库锁定-在库预留-VMI锁定-在途锁定
                Integer inStockCount =
                    inventoryCO.getNumInWarehouse() + goodsVmiStockDO.getNumVmi() - inventoryCO.getNumInWarehouseLock()
                        - inventoryCO.getNumReserved() - goodsVmiStockDO.getNumLock() - inventoryCO.getNumOnWayLock();
                inventoryCO.setInStockUsableCount(inStockCount);
                // 在途可下单量 =在库可下单量+在途数量
                inventoryCO.setOnWayUsableCount(inStockCount + inventoryCO.getNumOnWay());
                // B2B锁库数量 为 在库锁定 + 在途锁定 + vmi锁定
                inventoryCO.setNumLock(
                    inventoryCO.getNumInWarehouseLock() + inventoryCO.getNumOnWayLock() + goodsVmiStockDO.getNumLock());
                if (inventoryCO.getInStockUsableCount() < 0) {
                    inventoryCO.setInStockUsableCount(0);
                }
                if (inventoryCO.getOnWayUsableCount() < 0) {
                    inventoryCO.setOnWayUsableCount(0);
                }
            });
        }
        PageInfo<GoodsItemInventoryCO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 把null 转成0
     * 
     * @param warehouseId
     * @param content
     * @return
     */
    private List<GoodsItemInventoryCO> listCOByWarehouseIdAndContent(Integer warehouseId, String content) {
        List<GoodsItemInventoryCO> goodsItemInventoryCOS =
            warehouseInStockQryExe.listCOByWarehouseIdAndContent(warehouseId, content);
        goodsItemInventoryCOS.forEach(goodsItemInventoryCO -> {
            if (goodsItemInventoryCO.getNumInWarehouse() == null) {
                goodsItemInventoryCO.setNumInWarehouse(0);
            }
            if (goodsItemInventoryCO.getNumInWarehouseLock() == null) {
                goodsItemInventoryCO.setNumInWarehouseLock(0);
            }
            if (goodsItemInventoryCO.getNumOnWay() == null) {
                goodsItemInventoryCO.setNumOnWay(0);
            }
            if (goodsItemInventoryCO.getNumOnWayLock() == null) {
                goodsItemInventoryCO.setNumOnWayLock(0);
            }
            if (goodsItemInventoryCO.getNumReserved() == null) {
                goodsItemInventoryCO.setNumReserved(0);
            }
        });
        return goodsItemInventoryCOS;
    }

    /**
     * 根据货品id和仓库id列表查询在库库存
     * 
     * @param itemId
     * @param warehouseIdList
     * @return
     */
    @Override
    public List<WarehouseInStockDO> listByItemIdAndWarehouseIdList(Integer itemId, List<Integer> warehouseIdList) {
        return warehouseInStockQryExe.listByItemIdAndWarehouseIdList(itemId, warehouseIdList);
    }

    @Override
    public List<GoodsItemInventorySummaryCO> summaryByItemIdListAndAreaIdListAndDistributorId(List<Integer> itemIdList,
        List<Integer> areaIdList, Integer distributorId, Boolean fromRpc, List<Integer> notSupportOnWayList) {
        // 直发客户不支持在途货品
        Map<Integer, Boolean> notSupportOnWayMap = new HashMap<>();
        if (areaIdList == null || areaIdList.size() == 0) {
            // 要去分销商去拿;
            com.bat.dubboapi.distributor.common.Response<DistributorBusinessRpcDTO> response =
                distributorServiceRpc.getDistributorBusiness(distributorId);
            DistributorBusinessRpcDTO distributorBusinessRpcDTO = response.getData();
            if (distributorBusinessRpcDTO == null) {
                throw new WarehouseException(WarehouseCommonErrorCode.COMMON_DISTRIBUTOR_ID_ERROR);
            }
            areaIdList = distributorBusinessRpcDTO.getSalesAreaIds();
            if (areaIdList == null || areaIdList.size() == 0) {
                throw new WarehouseException(WarehouseInStockErrorCode.W_DISTRIBUTOR_SALE_AREA_NULL);
            }
            if (DistributorConstant.DISTRIBUTOR_ON_WAY_FLAG_NO.equals(distributorBusinessRpcDTO.getOnWayFlag())) {
                // 客户不支持在途
                itemIdList.forEach(itemId -> {
                    notSupportOnWayMap.put(itemId, true);
                });
            } else {
                // 客户维度支持在途、需要判断是否直发和货品维度是否支持直发
                if (DistributorConstant.DISTRIBUTOR_AUTO_DELIVERY_YES
                    .equals(distributorBusinessRpcDTO.getAutoDelivery()) && !fromRpc) {
                    // 直发客户且支持在途 (RPC调用会带是不支持在途货品id列表、必须要查)
                    com.bat.dubboapi.goods.common.Response<List<GoodsItemOnwaySaleFlagRpcDTO>> onWayResponse =
                        goodsServiceRpc.listGoodsItemOnwaySaleFlag(itemIdList);
                    List<GoodsItemOnwaySaleFlagRpcDTO> saleFlagRpcDTOList = onWayResponse.getData();
                    if (saleFlagRpcDTOList == null || saleFlagRpcDTOList.size() == 0) {
                        LOGGER.error("查询货品列表是否支持在途异常{}", itemIdList);
                        throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
                    }
                    saleFlagRpcDTOList.forEach(goodsItemOnwaySaleFlagRpcDTO -> {
                        if (DistributorConstant.DISTRIBUTOR_ON_WAY_FLAG_NO
                            .equals(goodsItemOnwaySaleFlagRpcDTO.getOnwaySaleFlag())) {
                            // 不支持在途
                            notSupportOnWayMap.put(goodsItemOnwaySaleFlagRpcDTO.getId(), true);
                        }
                    });
                }
            }

        }
        if (notSupportOnWayList != null && notSupportOnWayList.size() > 0) {
            notSupportOnWayList.forEach(itemId -> {
                notSupportOnWayMap.put(itemId, true);
            });
        }
        return summaryByItemIdListAndAreaIdList(itemIdList, areaIdList, notSupportOnWayMap, null);
    }

    @Override
    public GoodsItemInventorySummaryCO summaryByItemIdAndAreaIdListAndWarehouseIdList(Integer itemId,
        List<Integer> areaIdList, List<Integer> warehouseIdList) {
        return warehouseInStockQryExe.summaryByItemIdAndAreaIdListAndWarehouseIdList(itemId, areaIdList,
            warehouseIdList);
    }

    @Override
    @Transactional
    public void update(WarehouseInStockDO warehouseInStockDO) {
        warehouseInStockCmdExe.update(warehouseInStockDO);
    }

    @Override
    public Integer addNumInWarehouseLockById(Integer numLock, Integer id) {
        return warehouseInStockCmdExe.addNumInWarehouseLockById(numLock, id);
    }

    @Override
    @Transactional
    public Response importSql(String url) {
        InputStream in;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(url)));
            String s = null;
            Random random = new Random();
            while ((s = bufferedReader.readLine()) != null) {
                String[] arr = s.split("VALUES \\(")[1].split(", ");
                WarehouseInStockDO warehouseInStockDO = new WarehouseInStockDO();
                String goodsId = arr[1].replaceAll(" ", "");
                String erpItemId = arr[5].replaceAll(" ", "");
                warehouseInStockDO.setWarehouseId(1);
                warehouseInStockDO.setGoodsId(Integer.parseInt(goodsId));
                warehouseInStockDO.setItemId(Integer.parseInt(arr[0]));
                warehouseInStockDO.setItemErpId(Integer.parseInt(erpItemId));
                warehouseInStockDO.setNumInWarehouse(random.nextInt(10000));
                warehouseInStockDO.setNumOnWay(random.nextInt(10000));
                warehouseInStockDO.setNumInWarehouseLock(0);
                warehouseInStockDO.setNumOnWayLock(0);
                warehouseInStockDO.setNumReserved(1);
                warehouseInStockDO.setErpNumInWarehouse(0);
                warehouseInStockDO.setErpNumLock(0);
                warehouseInStockDO.setCreateTime(new Date());
                warehouseInStockDO.setUpdateTime(new Date());
                warehouseInStockCmdExe.create(warehouseInStockDO);
                warehouseInStockDO.setWarehouseId(72);
                warehouseInStockDO.setId(null);
                warehouseInStockCmdExe.create(warehouseInStockDO);
                warehouseInStockDO.setId(null);
                warehouseInStockDO.setWarehouseId(73);
                warehouseInStockCmdExe.create(warehouseInStockDO);
                GoodsVmiStockDO goodsVmiStockDO = new GoodsVmiStockDO();
                goodsVmiStockDO.setCreateTime(new Date());
                goodsVmiStockDO.setGoodsId(warehouseInStockDO.getGoodsId());
                goodsVmiStockDO.setItemErpId(warehouseInStockDO.getItemErpId());
                goodsVmiStockDO.setItemId(warehouseInStockDO.getItemId());
                goodsVmiStockDO.setNumVmi(random.nextInt(100));
                goodsVmiStockDO.setNumLock(0);
                goodsVmiStockDO.setUpdateTime(new Date());
                goodsVmiStockServiceI.create(goodsVmiStockDO);
                // GoodsOnWayStockDO goodsOnWayStockDO = new GoodsOnWayStockDO();
                // goodsOnWayStockDO.setNumLock(0);
                // goodsOnWayStockDO.setCreateTime(new Date());
                // goodsOnWayStockDO.setItemId(warehouseInStockDO.getItemId());
                // goodsOnWayStockDO.setUpdateTime(new Date());
                // goodsOnWayStockDO.setGoodsId(warehouseInStockDO.getGoodsId());
                // goodsOnWayStockDO.setItemErpId(warehouseInStockDO.getItemErpId());
                // goodsOnWayStockDO.setNumOnWay(random.nextInt(1000));
                // goodsOnWayStockServiceI.create(goodsOnWayStockDO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.buildSuccess();
    }

    /**
     * 根据货品erpID列表同步ERP库存数量
     * 
     * @param itemErpIdList
     *            需要同步的erpId 列表
     * @return
     */
    @Override
    public Response syncStock(List<Long> itemErpIdList) {
        if (itemErpIdList.size() > config.getSyncStockNum()) {
            throw WarehouseException.buildException(WarehouseErrorCode.W_WAREHOUSE_SYNC_STOCK_NUM_ERROR,
                MessageUtils.get(WarehouseErrorCode.W_WAREHOUSE_SYNC_STOCK_NUM_ERROR) + config.getSyncStockNum());
        }
        String vmiWarehouse = vmiConfigExe.getTenantErpConfig().getVmiWarehouse();
        List<WarehouseDO> warehouseList = warehouseServiceI.listUsableWarehouse(COMMON_SYNC_TYPE_1);
        if (CollectionUtils.isEmpty(warehouseList)) {
            throw new WarehouseException(WarehouseErrorCode.W_WAREHOUSE_NONE_ENABLE_ERROR);
        }
        List<WarehouseInStockDO> inStockDOList = warehouseInStockQryExe.listByItemErpIdList(itemErpIdList);
        if (CollectionUtils.isEmpty(inStockDOList)) {
            throw new WarehouseException("无符合条件的货品需要同步库存");
        }
        syncStock(vmiWarehouse, warehouseList, inStockDOList);
        return Response.buildSuccess();
    }

    /**
     * 全量同步ERP库存数量(同步redis缓存库存数据)
     * 
     * @return
     */
    @Override
    public Response syncStock(String tenantNo) {
        // 正在全量同步时，需锁定
        AutoReleaseLock autoReleaseLock =
            syncErpStockCache.tryLock(TenantContext.getTenantNo() + ":" + "syncAllStock", 2, TimeUnit.HOURS);
        if (autoReleaseLock == null) {
            return Response.buildFailure(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_SYNC_ERP_REPEAT,
                MessageUtils.get(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_SYNC_ERP_REPEAT));
        }
        warehouseStockSyncCmdExe.syncStock(tenantNo, autoReleaseLock);
        return Response.buildSuccess();
    }

    /**
     * 根据vmi、仓库同步ERP库存（注意：库存inStockDOList列表不超过1000）
     *
     * @param vmiWarehouse
     * @param warehouseList
     * @param inStockDOList
     */
    public void syncStock(String vmiWarehouse, List<WarehouseDO> warehouseList,
        List<WarehouseInStockDO> inStockDOList) {
        List<Integer> warehouseIdList = warehouseList.stream().map(WarehouseDO::getId).collect(Collectors.toList());
        // 同步在库在途库存 、key为itemErpId 、value为货品的仓库库存列表
        Map<Integer, List<InquiryInfo>> inStockMap = new HashMap<>();
        List<Long> groupItemErpIdList = new ArrayList<>();
        inStockDOList.forEach(warehouseInStockDO -> {
            groupItemErpIdList.add(warehouseInStockDO.getItemErpId().longValue());
        });
        warehouseList.forEach(warehouse -> {
            List<InquiryInfo> resultList =
                erpStockQryExe.queryErpStock(warehouse.getWarehouseNo(), groupItemErpIdList, config.getSyncStockNum());
            if (!CollectionUtils.isEmpty(resultList)) {
                resultList.forEach(inquiryInfo -> {
                    inquiryInfo.setWarehouseId(warehouse.getId());
                    Integer itemErpId = Integer.parseInt(inquiryInfo.getFMATERIALID());
                    List<InquiryInfo> infoList = inStockMap.get(itemErpId);
                    if (infoList == null) {
                        infoList = new ArrayList<>();
                        inStockMap.put(itemErpId, infoList);
                    }
                    infoList.add(inquiryInfo);
                });
            }
        });
        // 同步VMI库存
        List<InquiryInfo> vmiStockList =
            erpStockQryExe.queryErpStock(vmiWarehouse, groupItemErpIdList, config.getSyncStockNum());
        // 更新库存数据
        warehouseStockSyncCmdExe.syncStock(inStockMap, vmiStockList, warehouseIdList, inStockDOList);
        // 刷新货品是否有库存
        List<Integer> itemIds = inStockDOList.stream().map(WarehouseInStockDO::getItemId).collect(Collectors.toList());
        warehouseInStockCmdExe.freshItemUnderStockFlag(warehouseList, itemIds);
    }

    @Override
    public List<GoodsItemInventorySummaryCO> listByAdmin(List<Integer> itemIdList, Integer warehouseId) {
        return summaryByItemIdListAndAreaIdList(itemIdList, null, null, warehouseId);
    }

    @Autowired
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @Override
    public void testLock(Integer index) {
        String param =
            "[{\"inStockCount\":5,\"itemId\":12416,\"onWayCount\":0,\"supportOversoldFlag\":false},{\"inStockCount\":10,\"itemId\":12417,\"onWayCount\":0,\"supportOversoldFlag\":false},{\"inStockCount\":5,\"itemId\":12415,\"onWayCount\":0,\"supportOversoldFlag\":false}]";
        List<GoodsItemCountDTO> itemCountList = JSONArray.parseArray(param, GoodsItemCountDTO.class);
        List<Integer> areaIdList = new ArrayList<>();
        areaIdList.add(9);

        com.bat.dubboapi.warehouse.common.Response<List<ItemStockLockDTO>> response =
            warehouseStockServiceRpc.summaryLockStock(itemCountList, areaIdList, 4401, null);
        System.out.println(response);
        // warehouseInStockQryExe.testLock(index);
    }

    @Override
    public WarehouseInStockDO findByItemIdAndWarehouseNo(Integer itemId, String warehouseNo) {
        return warehouseInStockQryExe.findByItemIdAndWarehouseNo(itemId, warehouseNo);
    }

    @Transactional
    @Override
    public void deleteByItemId(Integer itemId) {
        // 处理在库缓存
        List<WarehouseInStockDO> list = warehouseInStockQryExe.listByItemIdAndWarehouseIdList(itemId, null);
        if (list == null || list.size() == 0) {
            return;
        }
        list.stream().forEach(warehouseInStockDO -> {
            warehouseInStockCmdExe.deleteById(warehouseInStockDO.getId(),
                warehouseInStockDO.getWarehouseId() + "_" + itemId);
        });
    }

    /**
     * <h2>根据仓库id和商品货号ID查询在库库存信息</h2>
     *
     * @param warehouseId
     * @param itemId
     * @return
     */
    @Override
    public WarehouseInStockDO getByWarehouseIdAndItemId(Integer warehouseId, Integer itemId) {
        // 根据仓库id和商品货号ID查询信息
        WarehouseInStockDO warehouseInStockDO = warehouseInStockQryExe.getByWarehouseIdAndItemId(warehouseId, itemId);
        if (warehouseInStockDO == null) {
            return null;
        }
        boolean updateRedis = false;
        // 处理redis的数据缺少id的
        if (warehouseInStockDO.getId() == null) {
            // 删除异常redis缓存数据
            warehouseInStockCmdExe.removeStockRedisCache(warehouseInStockDO);
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
            warehouseInStockCmdExe.updateRedisCacheOnly(warehouseInStockDO);
        }
        return warehouseInStockDO;
    }

    @Transactional
    @Override
    public void updateRedisCacheOnly(WarehouseInStockDO warehouseInStockDO) {
        warehouseInStockCmdExe.updateRedisCacheOnly(warehouseInStockDO);
    }

    @Override
    @Transactional
    public Response initStock(String tenantNo) {
        // 正在全量同步时，需锁定
        AutoReleaseLock autoReleaseLock =
            syncErpStockCache.tryLock(TenantContext.getTenantNo() + ":" + "initStock", 2, TimeUnit.HOURS);
        if (autoReleaseLock == null) {
            return Response.buildFailure(WarehouseInStockErrorCode.W_WAREHOUSE_INIT_STOCK_SYNC,
                MessageUtils.get(WarehouseInStockErrorCode.W_WAREHOUSE_INIT_STOCK_SYNC));
        }
        // 获取所有仓库数据
        List<WarehouseDO> warehouseDOS = warehouseServiceI.listWarehouse(null, null);
        if (CollectionUtils.isEmpty(warehouseDOS)) {
            return Response.buildSuccess();
        }
        // 获取所有货品ids
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpc>> response = goodsServiceRpc.getAllGoodsItem();
        if (!response.isSuccess()) {
            return Response.buildFailure(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_INIT_EXCEPTION,
                MessageUtils.get(WarehouseInStockErrorCode.W_WAREHOUSE_STOCK_INIT_EXCEPTION));
        }
        warehouseStockSyncCmdExe.initStock(tenantNo, autoReleaseLock, warehouseDOS, response.getData());
        return Response.buildSuccess();
    }

    @Override
    public List<WarehouseInStockDO> listByWarehouseIdListGroupByItemId(List<Integer> warehouseIdList) {
        return warehouseInStockQryExe.listByWarehouseIdListGroupByItemId(warehouseIdList);
    }

    @Override
    public List<WarehouseInStockDO> listByWarehouseIdAndItemCodeList(List<Integer> warehouseIdList,
        List<String> itemCodeList) {
        return warehouseInStockQryExe.listByWarehouseIdAndItemCodeList(warehouseIdList, itemCodeList);
    }

    @Override
    public List<WarehouseInStockDO> listByCondition(Integer warehouseId) {
        return warehouseInStockQryExe.listByCondition(warehouseId);
    }

    // 批量修改mysql数据（不更新缓存）
    @Override
    public void updateList(List<WarehouseInStockDO> updateList) {
        warehouseInStockCmdExe.updateList(updateList);
    }

    /**
     * 根据redis key查询在库库存
     *
     * @param instockKeySet
     * @return
     */
    @Override
    public List<WarehouseInStockDO> listInstockByRedisKeySet(Set<String> instockKeySet) {
        AutoReleaseLock autoReleaseLock =
            stockkeyCache.tryLock(TenantContext.getTenantNo() + ":" + "---1", 10, TimeUnit.HOURS);
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        autoReleaseLockList.add(autoReleaseLock);
        for (int x = 0; x < autoReleaseLockList.size(); x++) {
            AutoReleaseLock autoReleaseLock1 = autoReleaseLockList.get(x);
            autoReleaseLock1.close();
            autoReleaseLockList.remove(x);
            x--;
        }
        LOGGER.info(JSON.toJSONString(autoReleaseLockList));

        return warehouseInStockQryExe.listInstockByRedisKeySet(instockKeySet);
    }

    @Override
    public List<WarehouseInStockDO> incListInstockByRedisKeySet(Set<String> instockKeySet) {
        AutoReleaseLock autoReleaseLock =
            stockkeyCache.tryLock(TenantContext.getTenantNo() + ":" + "---1", 10, TimeUnit.HOURS);
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        autoReleaseLockList.add(autoReleaseLock);
        for (int x = 0; x < autoReleaseLockList.size(); x++) {
            AutoReleaseLock autoReleaseLock1 = autoReleaseLockList.get(x);
            autoReleaseLock1.close();
            autoReleaseLockList.remove(x);
            x--;
        }
        LOGGER.info(JSON.toJSONString(autoReleaseLockList));

        return warehouseInStockQryExe.incListInstockByRedisKeySet(instockKeySet);
    }

    /**
     * 根据货品id重置锁库数量
     */
    @Override
    public void resetLockStock(Integer itemId) {
        warehouseInStockCmdExe.resetLockStock(itemId);
    }

    /**
     * 初始化库存锁库数据
     */
    @Override
    public void initResetLockStock() {
        warehouseInStockCmdExe.initResetLockStock();
    }

    /**
     * 初始化货品是否缺货
     */
    @Override
    public void initItemUnderStockFlag() {
        // 查询所有可用仓库
        List<WarehouseDO> warehouseDOList = warehouseServiceI.listUsableWarehouse(null);
        // 根据仓库查询所有的货品
        // Integer page = 1;
        // List<WarehouseInStockDO> warehouseInStockDOList = new ArrayList<>();

        // while (page == 1 || (!ObjectUtils.isEmpty(warehouseInStockDOList)
        // && warehouseInStockDOList.size() == config.getSyncStockNum())) {
        // PageHelper.startPage(page, config.getSyncStockNum());
        // 根据仓库id查询所有在库货品
        List<WarehouseInStockDO> warehouseInStockDOList =
            warehouseInStockQryExe.listByCondition(warehouseDOList.get(0).getId());
        // 循环每个货品库存 1
        warehouseInStockDOList.forEach(warehouseInStockDO -> {
            // 存储集合
            List<GoodsStockFlagDTO> goodsStockFlagDTOList = new ArrayList<>();
            // 当前货品的所有仓库信息stockDOList
            List<WarehouseInStockDO> stockDOList = new ArrayList<>();
            // 循环每个仓库里的货品库存 51
            warehouseDOList.forEach(warehouseDO -> {
                WarehouseInStockDO stockDO =
                    getByWarehouseIdAndItemId(warehouseDO.getId(), warehouseInStockDO.getItemId());
                if (stockDO != null) {
                    stockDOList.add(stockDO);
                }
            });
            // 根据货品id查询该货品的vmi数据信息
            GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(warehouseInStockDO.getItemId());
            WarehouseInStockConvertor.getGoodsStockFlagDTOList(stockDOList, goodsVmiStockDO, goodsStockFlagDTOList);
            goodsStockFlagServiceRpc.createList(goodsStockFlagDTOList);
        });
        // // 分页
        // Integer count = goodsStockFlagDTOList.size();
        // Integer size = 1000;
        // Integer pageCount = count % size > 0 ? (count / size) + 1 : count / size;
        // // 普通运行
        // // for (int i = 1; i < pageCount; i++) {
        // // List<GoodsStockFlagDTO> goodsStockFlagDTOListPagination =
        // // goodsStockFlagDTOList.stream().skip((i - 1) * size).limit(size).collect(Collectors.toList());
        // // goodsStockFlagServiceRpc.createList(goodsStockFlagDTOListPagination);
        // // }
        // // 开线程运行
        // for (int i = 1; i < pageCount; i++) {
        // List<GoodsStockFlagDTO> goodsStockFlagDTOListPagination =
        // goodsStockFlagDTOList.stream().skip((i - 1) * size).limit(size).collect(Collectors.toList());
        // executor.execute(new Runnable() {
        // @Override
        // public void run() {
        // goodsStockFlagServiceRpc.createList(goodsStockFlagDTOListPagination);
        // }
        // });
        // }
    }

    // page++;
    // }

    /**
     * 修改货品是否缺货
     * 
     * @param goodsItemStockFlagCmd
     */
    @Override
    public void dealwithGoodsUnderStockFlag(String tenantNo, GoodsItemStockFlagCmd goodsItemStockFlagCmd) {
        if (CollectionUtils.isEmpty(goodsItemStockFlagCmd.getDetailCmdList())) {
            throw new WarehouseException(WarehouseCommonErrorCode.COMMON_LIST_NULL);
        }

        List<WarehouseDO> warehouseDOS = goodsItemStockFlagCmd.getWarehouseDOList();
        if (CollectionUtils.isEmpty(warehouseDOS)) {
            // 如果仓库列表空时，查询仓库
            warehouseDOS = warehouseServiceI.listUsableWarehouse(null);
        }
        warehouseInStockCmdExe.dealwithGoodsUnderStockFlag(tenantNo, warehouseDOS, goodsItemStockFlagCmd);
    }

    @Override
    public List<WarehouseInStockDO> listByItemIds(List<Integer> itemIds) {
        if (itemIds.size() == 0) {
            return new ArrayList<>();
        }
        // 找出所有仓库
        List<WarehouseDO> warehouseDOS =
            warehouseQryExe.listByCondition(null, WarehouseCommonConstant.COMMON_OPEN_FLAG_YES, null);

        List<Integer> warehouseIds = warehouseDOS.stream().map(WarehouseDO::getId).collect(Collectors.toList());
        if (warehouseIds.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> instockKeySet = new HashSet<>();
        for (Integer itemId : itemIds) {
            for (Integer warehouseId : warehouseIds) {
                instockKeySet.add(warehouseId + "_" + itemId);
            }
        }
        return incListInstockByRedisKeySet(instockKeySet);
    }

    /**
     * 根据商品Ids移除库存（包括缓存数据）
     * 
     * @param goodsIds
     * @return
     */
    @Override
    public void deleteByGoodsIds(List<Integer> goodsIds) {
        List<WarehouseInStockDO> inStockDOS = warehouseInStockQryExe.listByGoodsIdsAndWarehouseIdList(goodsIds, null);
        if (!CollectionUtils.isEmpty(inStockDOS)) {
            List<Integer> itemIds = inStockDOS.stream().map(WarehouseInStockDO::getItemId).collect(Collectors.toList());
            warehouseInStockCmdExe.deleteList(inStockDOS);
            getGoodsVmiStockCmdExe.deleteList(itemIds);
        }
    }

    private void restWarehouseLockStock(List<WarehouseInStockDO> warehouseInStockDOList, Integer warehouseId,
        Short lockType, Integer itemId) {
        com.bat.dubboapi.order.common.Response<List<OrderGoodsStockSimpleDTO>> response =
            orderGoodsStockServiceRpc.sumByWarehouseIdAndLockTypeGroupByItemId(warehouseId, lockType, itemId);
        List<OrderGoodsStockSimpleDTO> goodsStockSimpleDTOList = response.getData();
        Map<Integer, OrderGoodsStockSimpleDTO> map = new HashMap<>();
        if (goodsStockSimpleDTOList != null && goodsStockSimpleDTOList.size() > 0) {
            map = goodsStockSimpleDTOList.stream().collect(Collectors.toMap(OrderGoodsStockSimpleDTO::getItemId,
                orderGoodsStockSimpleDTO -> orderGoodsStockSimpleDTO));
        }

        LOGGER.info("开始处理在库锁定数量" + warehouseId);
        for (int x = 0; x < warehouseInStockDOList.size(); x++) {
            WarehouseInStockDO stockDO = warehouseInStockDOList.get(x);
            if (LOCK_TYPE_INSTOCK.equals(lockType)) {
                WarehouseInStockDO warehouseInStockDO = getByWarehouseIdAndItemId(warehouseId, stockDO.getItemId());
                if (warehouseInStockDO != null) {
                    OrderGoodsStockSimpleDTO orderGoodsStockSimpleDTO = map.get(stockDO.getItemId());
                    if (orderGoodsStockSimpleDTO != null) {
                        warehouseInStockDO.setNumInWarehouseLock(orderGoodsStockSimpleDTO.getLockNum());
                    } else {
                        warehouseInStockDO.setNumInWarehouseLock(0);
                    }
                    warehouseInStockDO.setUpdateTime(new Date());
                    warehouseInStockCmdExe.updateRedisCacheOnly(warehouseInStockDO);
                } else {
                    LOGGER.info("找不到该货品{}", JSON.toJSONString(stockDO));
                }
            }
            if (LOCK_TYPE_ONWAY.equals(lockType)) {
                WarehouseInStockDO warehouseInStockDO = getByWarehouseIdAndItemId(warehouseId, stockDO.getItemId());
                if (warehouseInStockDO != null) {
                    OrderGoodsStockSimpleDTO orderGoodsStockSimpleDTO = map.get(stockDO.getItemId());
                    if (orderGoodsStockSimpleDTO != null) {
                        warehouseInStockDO.setNumOnWayLock(orderGoodsStockSimpleDTO.getLockNum());
                    } else {
                        warehouseInStockDO.setNumOnWayLock(0);
                    }
                    warehouseInStockDO.setUpdateTime(new Date());
                    warehouseInStockCmdExe.updateRedisCacheOnly(warehouseInStockDO);
                } else {
                    LOGGER.info("找不到该货品{}", JSON.toJSONString(stockDO));
                }
            }
            if (LOCK_TYPE_VMI.equals(lockType)) {
                GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockServiceI.getByItemId(stockDO.getItemId());
                if (goodsVmiStockDO != null) {
                    OrderGoodsStockSimpleDTO orderGoodsStockSimpleDTO = map.get(stockDO.getItemId());
                    if (orderGoodsStockSimpleDTO != null) {
                        goodsVmiStockDO.setNumLock(orderGoodsStockSimpleDTO.getLockNum());
                    } else {
                        goodsVmiStockDO.setNumLock(0);
                    }
                    goodsVmiStockDO.setUpdateTime(new Date());
                    goodsVmiStockCmdExe.updateRedisCacheOnly(goodsVmiStockDO);
                } else {
                    LOGGER.info("找不到该货品{}", JSON.toJSONString(stockDO));
                }
            }
        }
    }

    /**
     *
     * @param itemIdList
     *            货品id列表
     * @param areaIdList
     *            销售区域id列表
     * @param noSupportOnWayMap
     *            不支持在途货品map
     * @param assignWarehouseId
     *            指定查询库存
     * @return
     */
    public List<GoodsItemInventorySummaryCO> summaryByItemIdListAndAreaIdList(List<Integer> itemIdList,
        List<Integer> areaIdList, Map<Integer, Boolean> noSupportOnWayMap, Integer assignWarehouseId) {
        List<WarehouseDO> warehouseDOList = new ArrayList<>();
        if (assignWarehouseId != null) {
            // 指定仓库
            WarehouseDO warehouseDO = warehouseServiceI.getById(assignWarehouseId);
            warehouseDOList.add(warehouseDO);
        } else {
            warehouseDOList =
                warehouseServiceI.listByAreaIdListAndOpenFlag(areaIdList, WarehouseCommonConstant.COMMON_OPEN_FLAG_YES);
        }
        List<GoodsItemInventorySummaryCO> summaryCOList = new ArrayList<>();
        for (int x = 0; x < itemIdList.size(); x++) {
            Integer itemId = itemIdList.get(x);
            GoodsItemInventorySummaryCO summaryCO = new GoodsItemInventorySummaryCO();
            summaryCO.setItemId(itemId);
            summaryCO.setNumInWarehouseSum(0);
            summaryCO.setNumInWarehouseLockSum(0);
            summaryCO.setNumOnWaySum(0);
            summaryCO.setNumOnWayLockSum(0);
            summaryCO.setNumReservedSum(0);
            summaryCO.setErpNumInWarehouse(0);
            for (int y = 0; y < warehouseDOList.size(); y++) {
                WarehouseInStockDO warehouseInStockDO =
                    getByWarehouseIdAndItemId(warehouseDOList.get(y).getId(), itemId);
                if (warehouseInStockDO != null) {
                    // 仓库有可能无这个货品库存记录、需要判空
                    summaryCO.setNumInWarehouseSum(
                        summaryCO.getNumInWarehouseSum() + warehouseInStockDO.getNumInWarehouse());
                    summaryCO.setNumInWarehouseLockSum(
                        summaryCO.getNumInWarehouseLockSum() + warehouseInStockDO.getNumInWarehouseLock());
                    summaryCO.setNumOnWaySum(summaryCO.getNumOnWaySum() + warehouseInStockDO.getNumOnWay());
                    summaryCO.setNumOnWayLockSum(summaryCO.getNumOnWayLockSum() + warehouseInStockDO.getNumOnWayLock());
                    summaryCO.setNumReservedSum(summaryCO.getNumReservedSum() + warehouseInStockDO.getNumReserved());
                    summaryCO.setErpNumInWarehouse(
                        summaryCO.getErpNumInWarehouse() + warehouseInStockDO.getErpNumInWarehouse());
                }
            }
            summaryCOList.add(summaryCO);
        }

        // 改为从redis里面取数据
        // List<GoodsItemInventorySummaryCO> summaryCOList =
        // warehouseInStockDOMapper.summaryByItemIdListAndAreaIdList(itemIdList,areaIdList);
        if (summaryCOList.size() == 0) {
            LOGGER.error(WarehouseCommonErrorCode.ITEM_ID_ERROR + "{}", itemIdList);
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
        }
        // 判断不支持在途的货品列表

        for (Integer itemId : itemIdList) {
            boolean flag = true;
            for (GoodsItemInventorySummaryCO goodsItemInventorySummaryCO : summaryCOList) {
                if (itemId - goodsItemInventorySummaryCO.getItemId() == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                LOGGER.info("货品id【" + itemId + "】找不到在库库存");
                throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
            }
        }
        summaryCOList.forEach(goodsItemInventorySummaryCO -> warehouseInStockQryExe
            .setStockInventoryParam(goodsItemInventorySummaryCO, noSupportOnWayMap));
        return summaryCOList;
    }
}
