package com.bat.warehouse.manager.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockCmdExe;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockQryExe;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.data.DataImportServiceI;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;
import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import com.bat.warehouse.dao.stockReserved.dataobject.WarehouseStockReservedDO;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.dao.warehouseSetting.dataobject.WarehouseSettingDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import com.bat.warehouse.manager.inStock.executor.HttpUtil;
import com.bat.warehouse.manager.inStock.executor.WarehouseInStockCmdExe;
import com.bat.warehouse.manager.inStock.executor.WarehouseInStockQryExe;
import com.bat.warehouse.manager.log.executor.WarehouseStockChangeLogCmdExe;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockCmdExe;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockQryExe;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedCmdExe;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedDetailCmdExe;
import com.bat.warehouse.manager.warehouse.executor.WarehouseCmdExe;
import com.bat.warehouse.manager.warehouseSetting.executor.WarehouseSettingCmdExe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataImportServiceImpl implements DataImportServiceI {

    private static Logger LOGGER = LoggerFactory.getLogger(DataImportServiceImpl.class);

    @Autowired
    private GoodsOnWayStockCmdExe goodsOnWayStockCmdExe;

    @Autowired
    private GoodsVmiStockCmdExe goodsVmiStockCmdExe;

    @Autowired
    private WarehouseCmdExe warehouseCmdExe;

    @Autowired
    private WarehouseInStockCmdExe warehouseInStockCmdExe;

    @Autowired
    private WarehouseSettingCmdExe warehouseSettingCmdExe;

    @Autowired
    private WarehouseStockChangeLogCmdExe warehouseStockChangeLogCmdExe;

    @Autowired
    private WarehouseStockReservedCmdExe warehouseStockReservedCmdExe;

    @Autowired
    private WarehouseStockReservedDetailCmdExe warehouseStockReservedDetailCmdExe;

    @Autowired
    private GoodsVmiStockQryExe goodsVmiStockQryExe;

    @Autowired
    private GoodsOnWayStockQryExe goodsOnWayStockQryExe;

    @Autowired
    private WarehouseInStockQryExe warehouseInStockQryExe;

    private static final String hoseName="http://47.56.89.60:9999/open/sql/export/";

    @Override
    @Transactional
    public Response importData(Integer index) {
        LOGGER.info("当前租户:{}", TenantContext.getTenantNo());
        JSONArray jsonArray = null;
        if (index == 1001) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的材质列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer,GoodsItemRpcDTO> map = initGoods();
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    GoodsOnWayStockDO goodsOnWayStockDO = JSONObject.parseObject(jsonObject.toJSONString(), GoodsOnWayStockDO.class);
                    GoodsItemRpcDTO goodsItemRpcDTO = map.get(goodsOnWayStockDO.getItemId());
                    if(goodsItemRpcDTO !=null){
                        goodsOnWayStockDO.setItemName(goodsItemRpcDTO.getItemName());
                        goodsOnWayStockDO.setItemCode(goodsItemRpcDTO.getItemCode());
                        goodsOnWayStockDO.setCreateTime(new Date());
                        goodsOnWayStockDO.setUpdateTime(new Date());
                        goodsOnWayStockCmdExe.create(goodsOnWayStockDO);
                    }
                }
            }
        }
        if (index == 1002) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的VMI库存列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer,GoodsItemRpcDTO> map = initGoods();
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    GoodsVmiStockDO goodsVmiStockDO = JSONObject.parseObject(jsonObject.toJSONString(), GoodsVmiStockDO.class);
                    GoodsItemRpcDTO goodsItemRpcDTO = map.get(goodsVmiStockDO.getItemId());
                    if(goodsItemRpcDTO !=null){
                        goodsVmiStockDO.setItemName(goodsItemRpcDTO.getItemName());
                        goodsVmiStockDO.setItemCode(goodsItemRpcDTO.getItemCode());
                        goodsVmiStockDO.setCreateTime(new Date());
                        goodsVmiStockDO.setUpdateTime(new Date());
                        goodsVmiStockCmdExe.create(goodsVmiStockDO);
                    }
                }
            }
        }
        if (index == 1003) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的仓库列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseDO warehouseDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseDO.class);
                    warehouseDO.setWarehouseNo(jsonObject.getString("no"));
                    warehouseDO.setOpenFlag(WarehouseCommonConstant.COMMON_OPEN_FLAG_YES);
                    if(jsonObject.getShort("isDelete") ==1){
                        warehouseDO.setOpenFlag(WarehouseCommonConstant.COMMON_OPEN_FLAG_NO);
                    }
                    warehouseDO.setDelFlag(WarehouseCommonConstant.COMMON_DEL_FLAG_NO);
                    warehouseDO.setSortNo(jsonObject.getInteger("sort"));
                    warehouseDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    warehouseDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    warehouseDO.setCreateUserId(-1);
                    warehouseCmdExe.create(warehouseDO);
                }
            }
        }
        if (index == 1004) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的在库列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer,GoodsItemRpcDTO> map = initGoods();
                Map<Integer, WarehouseInStockDO> stockDOMap = new HashMap<>();
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseInStockDO warehouseInStockDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseInStockDO.class);
                    GoodsItemRpcDTO goodsItemRpcDTO = map.get(warehouseInStockDO.getItemId());
                    if(goodsItemRpcDTO !=null){
                        warehouseInStockDO.setItemName(goodsItemRpcDTO.getItemName());
                        warehouseInStockDO.setItemCode(goodsItemRpcDTO.getItemCode());
                        warehouseInStockDO.setCreateTime(new Date());
                        warehouseInStockDO.setUpdateTime(new Date());
                        warehouseInStockDO.setErpNumInWarehouse(jsonObject.getInteger("erpNumInWarehouose"));
                        warehouseInStockCmdExe.create(warehouseInStockDO);
                        stockDOMap.put(warehouseInStockDO.getItemId(),warehouseInStockDO);
                    }
                }
                List<GoodsVmiStockDO> vmiStockDOList =goodsVmiStockQryExe.listAll();
                Map<Integer, GoodsVmiStockDO> vmiMap =vmiStockDOList.stream().collect(Collectors.toMap(GoodsVmiStockDO::getItemId, goodsVmiStockDO -> goodsVmiStockDO));
                //处理VMI之前数据为空的
                Set<Map.Entry<Integer, GoodsItemRpcDTO>> entrySet = map.entrySet();
                Iterator<Map.Entry<Integer, GoodsItemRpcDTO>> iterator = entrySet.iterator();

                List<GoodsOnWayStockDO> goodsOnWayStockDOList = goodsOnWayStockQryExe.listAll();

                Map<Integer, GoodsOnWayStockDO> onWayStockDOMap = goodsOnWayStockDOList.stream().collect(Collectors.toMap(GoodsOnWayStockDO::getItemId, goodsOnWayStockDO -> goodsOnWayStockDO));

                while (iterator.hasNext()){
                    Map.Entry<Integer, GoodsItemRpcDTO> entry = iterator.next();
                    if(!vmiMap.containsKey(entry.getKey())){
                        //没用VMI
                        if( stockDOMap.containsKey(entry.getKey())){
                            WarehouseInStockDO warehouseInStockDO = stockDOMap.get(entry.getKey());
                            GoodsVmiStockDO goodsVmiStockDO = new GoodsVmiStockDO();
                            goodsVmiStockDO.setItemErpId(warehouseInStockDO.getItemErpId());
                            goodsVmiStockDO.setItemId(warehouseInStockDO.getItemId());
                            goodsVmiStockDO.setGoodsId(warehouseInStockDO.getGoodsId());
                            goodsVmiStockDO.setNumLock(0);
                            goodsVmiStockDO.setNumVmi(0);
                            goodsVmiStockDO.setUpdateTime(new Date());
                            goodsVmiStockDO.setCreateTime(new Date());
                            goodsVmiStockDO.setItemCode(warehouseInStockDO.getItemCode());
                            goodsVmiStockDO.setItemName(warehouseInStockDO.getItemName());
                            goodsVmiStockCmdExe.create(goodsVmiStockDO);
                        }

                    }
                    if(!onWayStockDOMap.containsKey(entry.getKey())){
                        //在途没有
                        if( stockDOMap.containsKey(entry.getKey())){
                            WarehouseInStockDO warehouseInStockDO = stockDOMap.get(entry.getKey());
                            GoodsOnWayStockDO goodsOnWayStockDO = new GoodsOnWayStockDO();
                            goodsOnWayStockDO.setItemErpId(warehouseInStockDO.getItemErpId());
                            goodsOnWayStockDO.setItemId(warehouseInStockDO.getItemId());
                            goodsOnWayStockDO.setGoodsId(warehouseInStockDO.getGoodsId());
                            goodsOnWayStockDO.setNumLock(0);
                            goodsOnWayStockDO.setNumOnWay(0);
                            goodsOnWayStockDO.setUpdateTime(new Date());
                            goodsOnWayStockDO.setCreateTime(new Date());
                            goodsOnWayStockDO.setItemCode(warehouseInStockDO.getItemCode());
                            goodsOnWayStockDO.setItemName(warehouseInStockDO.getItemName());
                            goodsOnWayStockCmdExe.create(goodsOnWayStockDO);
                        }
                    }

                }
            }


        }
        if (index == 1005) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的仓库设置列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseSettingDO warehouseSettingDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseSettingDO.class);
                    warehouseSettingDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    warehouseSettingDO.setCreateTime(new Date());
                    warehouseSettingDO.setCreateUserId(-1);
                    warehouseSettingCmdExe.create(warehouseSettingDO);
                }
            }
        }
        if (index == 1006) {
            String result =HttpUtil.doGet(hoseName+index);
            //jsonArray = HttpUtil.getJson("http://localhost:9997/open/sql/export/"+index, JSONArray.class);
            jsonArray = JSON.parseArray(result);
            LOGGER.info("查询回来的库存变更日志列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseStockChangeLogDO warehouseStockChangeLogDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseStockChangeLogDO.class);
                    warehouseStockChangeLogDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    warehouseStockChangeLogDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    warehouseStockChangeLogDO.setItemCode(jsonObject.getString("itemNo"));
                    warehouseStockChangeLogCmdExe.create(warehouseStockChangeLogDO);
                }
            }
        }
        if (index == 1007) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的库存预留列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseStockReservedDO warehouseStockReservedDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseStockReservedDO.class);
                    warehouseStockReservedDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    warehouseStockReservedDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    warehouseStockReservedDO.setStatus(jsonObject.getShort("processStatus"));
                    warehouseStockReservedDO.setCreateUserId(jsonObject.getInteger("operateUserId"));
                    warehouseStockReservedDO.setUpdateUserId(jsonObject.getInteger("operateUserId"));
                    warehouseStockReservedCmdExe.create(warehouseStockReservedDO);
                }
            }
        }
        if (index == 1008) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的库存预留明细列表：" + JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer,GoodsItemRpcDTO> map = initGoods();
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    WarehouseStockReservedDetailDO reservedDetailDO = JSONObject.parseObject(jsonObject.toJSONString(), WarehouseStockReservedDetailDO.class);
                    GoodsItemRpcDTO goodsItemRpcDTO = map.get(reservedDetailDO.getItemId());
                    if(goodsItemRpcDTO !=null){
                        reservedDetailDO.setItemName(goodsItemRpcDTO.getItemName());
                        reservedDetailDO.setCreateUserId(-1);
                        warehouseStockReservedDetailCmdExe.create(reservedDetailDO);
                    }

                }
            }
        }
        if (index == 1000) {
            jsonArray = HttpUtil.getJson(hoseName+index, JSONArray.class);
            LOGGER.info("查询回来的货品列表：" + JSON.toJSONString(jsonArray));

            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);

                }
            }
            //处理在库库存
            List<WarehouseInStockDO> f1List = warehouseInStockQryExe.listByCondition(51);
            Map<Integer, WarehouseInStockDO> chengpMap = f1List.stream().collect(Collectors.toMap(WarehouseInStockDO::getItemId, warehouseInStockDO -> warehouseInStockDO));
            //京东
            List<WarehouseInStockDO> q2List = warehouseInStockQryExe.listByCondition(72);
            Map<Integer, WarehouseInStockDO> jdMap = q2List.stream().collect(Collectors.toMap(WarehouseInStockDO::getItemId, warehouseInStockDO -> warehouseInStockDO));
            //印想
            List<WarehouseInStockDO> q3List = warehouseInStockQryExe.listByCondition(73);
            Map<Integer, WarehouseInStockDO> yxMap = q3List.stream().collect(Collectors.toMap(WarehouseInStockDO::getItemId, warehouseInStockDO -> warehouseInStockDO));
            //在途
            List<GoodsOnWayStockDO> onWayStockDOList = goodsOnWayStockQryExe.listAll();
            Map<Integer, GoodsOnWayStockDO> onWayStockDOMap = onWayStockDOList.stream().collect(Collectors.toMap(GoodsOnWayStockDO::getItemId, goodsOnWayStockDO -> goodsOnWayStockDO));
            //vmi
            List<GoodsVmiStockDO> vmiStockDOList = goodsVmiStockQryExe.listAll();
            Map<Integer, GoodsVmiStockDO> vmiStockDOMap = vmiStockDOList.stream().collect(Collectors.toMap(GoodsVmiStockDO::getItemId, goodsVmiStockDO -> goodsVmiStockDO));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x = 0; x < jsonArray.size(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    Integer itemId = jsonObject.getInteger("id");
                    String itemCode = jsonObject.getString("itemCode");
                    String itemName = jsonObject.getString("itemName");
                    Integer itemErpId = jsonObject.getInteger("itemErpId");
                    Integer goodsId = jsonObject.getInteger("goodsId");
                    if(!chengpMap.containsKey(itemId)){
                        WarehouseInStockDO inStockDO = new WarehouseInStockDO();
                        inStockDO.setWarehouseId(51);
                        inStockDO.setGoodsId(goodsId);
                        inStockDO.setItemId(itemId);
                        inStockDO.setItemErpId(itemErpId);
                        inStockDO.setNumInWarehouse(100);
                        inStockDO.setNumInWarehouseLock(0);
                        inStockDO.setNumOnWay(100);
                        inStockDO.setNumOnWayLock(0);
                        inStockDO.setNumReserved(0);
                        inStockDO.setErpNumInWarehouse(0);
                        inStockDO.setErpNumLock(0);
                        inStockDO.setCreateTime(new Date());
                        inStockDO.setUpdateTime(new Date());
                        inStockDO.setItemCode(itemCode);
                        inStockDO.setItemName(itemName);
                        warehouseInStockCmdExe.create(inStockDO);
                    }
                    if(!jdMap.containsKey(itemId)){
                        WarehouseInStockDO inStockDO = new WarehouseInStockDO();
                        inStockDO.setWarehouseId(72);
                        inStockDO.setGoodsId(goodsId);
                        inStockDO.setItemId(itemId);
                        inStockDO.setItemErpId(itemErpId);
                        inStockDO.setNumInWarehouse(100);
                        inStockDO.setNumInWarehouseLock(0);
                        inStockDO.setNumOnWay(100);
                        inStockDO.setNumOnWayLock(0);
                        inStockDO.setNumReserved(0);
                        inStockDO.setErpNumInWarehouse(0);
                        inStockDO.setErpNumLock(0);
                        inStockDO.setCreateTime(new Date());
                        inStockDO.setUpdateTime(new Date());
                        inStockDO.setItemCode(itemCode);
                        inStockDO.setItemName(itemName);
                        warehouseInStockCmdExe.create(inStockDO);
                    }
                    if(!yxMap.containsKey(itemId)){
                        WarehouseInStockDO inStockDO = new WarehouseInStockDO();
                        inStockDO.setWarehouseId(73);
                        inStockDO.setGoodsId(goodsId);
                        inStockDO.setItemId(itemId);
                        inStockDO.setItemErpId(itemErpId);
                        inStockDO.setNumInWarehouse(100);
                        inStockDO.setNumInWarehouseLock(0);
                        inStockDO.setNumOnWay(100);
                        inStockDO.setNumOnWayLock(0);
                        inStockDO.setNumReserved(0);
                        inStockDO.setErpNumInWarehouse(0);
                        inStockDO.setErpNumLock(0);
                        inStockDO.setCreateTime(new Date());
                        inStockDO.setUpdateTime(new Date());
                        inStockDO.setItemCode(itemCode);
                        inStockDO.setItemName(itemName);
                        warehouseInStockCmdExe.create(inStockDO);
                    }
//                    if(!onWayStockDOMap.containsKey(itemId)){
//                        GoodsOnWayStockDO goodsOnWayStockDO = new GoodsOnWayStockDO();
//                        goodsOnWayStockDO.setGoodsId(goodsId);
//                        goodsOnWayStockDO.setItemId(itemId);
//                        goodsOnWayStockDO.setItemErpId(itemErpId);
//                        goodsOnWayStockDO.setNumOnWay(10);
//                        goodsOnWayStockDO.setNumLock(0);
//                        goodsOnWayStockDO.setCreateTime(new Date());
//                        goodsOnWayStockDO.setUpdateTime(new Date());
//                        goodsOnWayStockDO.setItemCode(itemCode);
//                        goodsOnWayStockDO.setItemName(itemName);
//                        goodsOnWayStockCmdExe.create(goodsOnWayStockDO);
//                    }
                    if(!vmiStockDOMap.containsKey(itemId)){
                        GoodsVmiStockDO goodsVmiStockDO = new GoodsVmiStockDO();
                        goodsVmiStockDO.setGoodsId(goodsId);
                        goodsVmiStockDO.setItemId(itemId);
                        goodsVmiStockDO.setItemErpId(itemErpId);
                        goodsVmiStockDO.setNumVmi(500);
                        goodsVmiStockDO.setNumLock(0);
                        goodsVmiStockDO.setCreateTime(new Date());
                        goodsVmiStockDO.setUpdateTime(new Date());
                        goodsVmiStockDO.setItemCode(itemCode);
                        goodsVmiStockDO.setItemName(itemName);
                        goodsVmiStockCmdExe.create(goodsVmiStockDO);
                    }
                }
            }

        }
        return null;
    }

    public static Map<Integer,GoodsItemRpcDTO> initGoods(){
        JSONArray  jsonArray = HttpUtil.getJson(hoseName+1000, JSONArray.class);
        LOGGER.info("查询回来的货品列表：" + JSON.toJSONString(jsonArray));
        Map<Integer,GoodsItemRpcDTO> map = new HashMap<>();
        for (int x = 0; x < jsonArray.size(); x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            GoodsItemRpcDTO goodsItemRpcDTO = JSONObject.parseObject(jsonObject.toJSONString(), GoodsItemRpcDTO.class);
            map.put(goodsItemRpcDTO.getId(),goodsItemRpcDTO);
        }
        return map;
    }


}
