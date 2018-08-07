package com.bat.warehouse.manager.inStock.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.warehouse.api.base.util.BeanUtils;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import com.bat.warehouse.manager.common.constant.external.GoodsConstant;
import com.bat.warehouse.manager.dubbo.convertor.WarehouseStockDubboConvertor;

public class WarehouseInStockConvertor {

    /**
     * 过滤指定货品的库存、
     * 
     * @param itemErpIdList
     *            指定货品erpId
     * @param allItemInStockDOList
     */
    public static List<WarehouseInStockDO> filterAssignItem(List<Long> itemErpIdList,
        List<WarehouseInStockDO> allItemInStockDOList) {
        // 过滤指定库存
        if (itemErpIdList == null || itemErpIdList.size() == 0) {
            return allItemInStockDOList;
        }
        List<WarehouseInStockDO> assignList = new ArrayList<>();
        for (int z = 0; z < allItemInStockDOList.size(); z++) {
            if (itemErpIdList.contains(allItemInStockDOList.get(z).getItemErpId().longValue())) {
                // 指定
                assignList.add(allItemInStockDOList.get(z));
            }
        }
        // 指定货品更新库存
        return assignList;
    }

    public static Map<String, InquiryInfo> toInquiryInfoMap(List<InquiryInfo> list) {
        if (list == null || list.size() == 0) {
            // 不要返回空
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(InquiryInfo::getFMATERIALID, inquiryInfo -> inquiryInfo));
    }

    /**
     * 分组、按照10个一组
     * 
     * @param inStockDOList
     * @return
     */
    public static List<List<WarehouseInStockDO>> toWarehouseInStockListList(List<WarehouseInStockDO> inStockDOList,
        Integer num) {
        if (inStockDOList == null || inStockDOList.size() == 0) {
            return null;
        }
        List<List<WarehouseInStockDO>> allSyncList = new ArrayList<>();
        List<WarehouseInStockDO> syncList = new ArrayList<>();
        for (int x = 0; x < inStockDOList.size(); x++) {
            if (x > 0 && x % num == 0) {
                // 按照10个分组查询
                allSyncList.add(syncList);
                syncList = new ArrayList<>();
            }
            syncList.add(inStockDOList.get(x));
        }
        allSyncList.add(syncList);
        return allSyncList;
    }

    /**
     * 根据库存列表、得到全部的排列组合
     * 
     * @param warehouseInStockDOList
     * @return
     */
    public static List<List<WarehouseInStockDO>>
        getPermutationAndCombination(List<WarehouseInStockDO> warehouseInStockDOList) {
        List<List<WarehouseInStockDO>> combinationList = new ArrayList<>();
        if (CollectionUtils.isEmpty(warehouseInStockDOList)) {
            return null;
        }
        int startNum = 0;
        int endNum = warehouseInStockDOList.size();
        for (int i = startNum; i < endNum; i++) {
            int temp = i;
            List<WarehouseInStockDO> list = new ArrayList<>();
            list.add(warehouseInStockDOList.get(i));
            combinationList.add(list);
            permutationAndCombination(++temp, endNum, list, warehouseInStockDOList, combinationList);
        }
        return combinationList;
    }

    private static void permutationAndCombination(Integer start, Integer end, List<WarehouseInStockDO> list,
        List<WarehouseInStockDO> warehouseInStockDOList, List<List<WarehouseInStockDO>> combinationList) {
        for (int x = start; x < end; x++) {
            List<WarehouseInStockDO> groupList = BeanUtils.copyList(list, WarehouseInStockDO.class);
            groupList.add(warehouseInStockDOList.get(x));
            combinationList.add(groupList);
            int temp = x;
            permutationAndCombination(++temp, end, groupList, warehouseInStockDOList, combinationList);
        }
    }

    public static void main(String[] args) {
        List<WarehouseInStockDO> warehouseInStockDOList = new ArrayList<>();
        WarehouseInStockDO warehouseInStockDO = new WarehouseInStockDO();
        warehouseInStockDO.setId(1);
        warehouseInStockDOList.add(warehouseInStockDO);
        WarehouseInStockDO warehouseInStockDO2 = new WarehouseInStockDO();
        warehouseInStockDO2.setId(2);
        warehouseInStockDOList.add(warehouseInStockDO2);
        WarehouseInStockDO warehouseInStockDO3 = new WarehouseInStockDO();
        warehouseInStockDO3.setId(3);
        warehouseInStockDOList.add(warehouseInStockDO3);
        WarehouseInStockDO warehouseInStockDO4 = new WarehouseInStockDO();
        warehouseInStockDO4.setId(4);
        warehouseInStockDOList.add(warehouseInStockDO4);
        List<List<WarehouseInStockDO>> permutationAndCombination = getPermutationAndCombination(warehouseInStockDOList);
        permutationAndCombination.forEach(warehouseInStockDOList1 -> {
            System.out.println(JSON.toJSONString(warehouseInStockDOList1));
        });
    }

    /**
     * 转为仓库id字符串数组、_隔开
     * 
     * @param stockDOList
     * @return
     */
    public static String toWarehouseIdArr(List<WarehouseInStockDO> stockDOList) {
        if (CollectionUtils.isEmpty(stockDOList)) {
            return "";
        }
        String str = "";
        for (int x = 0; x < stockDOList.size(); x++) {
            str += stockDOList.get(x).getWarehouseId();
            if (x < stockDOList.size() - 1) {
                str += "_";
            }
        }
        return str;
    }

    /**
     * 根据单个货品的在库库存列表、VMI库存、在途库存获取货品是否缺货标记（组装数据）
     * 
     * @param stockDOList
     *            单个货品的所有仓库信息
     * @param goodsVmiStockDO
     *            单个货品的vim数据信息
     * @param goodsStockFlagDTOList
     *            收纳集合
     */
    public static void getGoodsStockFlagDTOList(List<WarehouseInStockDO> stockDOList, GoodsVmiStockDO goodsVmiStockDO,
        List<GoodsStockFlagDTO> goodsStockFlagDTOList) {
        // 需要将VMI转为仓库id是0的在库库存
        WarehouseInStockDO vmi = new WarehouseInStockDO();
        vmi.setId(null);
        vmi.setWarehouseId(0);
        vmi.setNumOnWay(0);
        vmi.setNumOnWayLock(0);
        vmi.setNumReserved(0);
        if (goodsVmiStockDO == null) {
            vmi.setNumInWarehouse(0);
            vmi.setNumInWarehouseLock(0);
        } else {
            vmi.setNumInWarehouse(goodsVmiStockDO.getNumVmi());
            vmi.setNumInWarehouseLock(goodsVmiStockDO.getNumLock());
            vmi.setItemId(goodsVmiStockDO.getItemId());
            vmi.setGoodsId(goodsVmiStockDO.getGoodsId());
        }
        if (!CollectionUtils.isEmpty(stockDOList)) {
            vmi.setItemId(stockDOList.get(0).getItemId());
            vmi.setGoodsId(stockDOList.get(0).getGoodsId());
        }
        // VMI放在第一位
        stockDOList.add(0, vmi);
        // 递归计算货品仓库组合是否有库存
        if (!CollectionUtils.isEmpty(stockDOList)) {
            for (int i = 0; i < stockDOList.size(); i++) {
                WarehouseInStockDO pStockDO = stockDOList.get(0);
                rWarehouseGoodsStock(i, pStockDO, stockDOList, goodsStockFlagDTOList);
            }
        }
    }

    /**
     * 递归处理货品仓库组合库存计算
     * 
     * @param num
     *            0
     * @param pStockDO
     *            循环当前的货品在库信息
     * @param stockDOList
     *            所有货品在库信息
     * @param goodsStockFlagDTOList
     *            收纳集合
     */
    private static void rWarehouseGoodsStock(Integer num, WarehouseInStockDO pStockDO,
        List<WarehouseInStockDO> stockDOList, List<GoodsStockFlagDTO> goodsStockFlagDTOList) {
        if (pStockDO.getItemId() == null) {
            return;
        }
        Integer stockUsableQuantity = 0;
        List<WarehouseInStockDO> stockDOS = new ArrayList<>();
        stockDOS.add(pStockDO);
        // VMI仓库不能独立存在，因为客户不会只有VMI仓库，VMI仓库是公共仓库
        if (pStockDO.getNumInWarehouse() != 0) {
            stockUsableQuantity = getGoodsStockFlagDTO(pStockDO.getGoodsId(), pStockDO.getItemId(), stockDOS,
                stockUsableQuantity, goodsStockFlagDTOList);
        }
        for (int i = num + 1; i < stockDOList.size(); i++) {
            WarehouseInStockDO sStockDO = stockDOList.get(i);
            stockDOS.add(sStockDO);
            stockUsableQuantity = getGoodsStockFlagDTO(pStockDO.getGoodsId(), pStockDO.getItemId(), stockDOS,
                stockUsableQuantity, goodsStockFlagDTOList);
            rWarehouseGoodsStock(num + 1, sStockDO, stockDOList, goodsStockFlagDTOList);
        }
    }

    /**
     * 生成货品是否有库标识
     * 
     * @param goodsId
     * @param itemId
     * @param stockDOS
     * @param stockUsableQuantity
     * @return
     */
    private static Integer getGoodsStockFlagDTO(Integer goodsId, Integer itemId, List<WarehouseInStockDO> stockDOS,
        Integer stockUsableQuantity, List<GoodsStockFlagDTO> goodsStockFlagDTOList) {
        List<Integer> stockQuantity = WarehouseStockDubboConvertor.calculateInStockUsable(stockDOS, null);
        stockUsableQuantity = stockUsableQuantity + stockQuantity.get(0) + stockQuantity.get(1);
        GoodsStockFlagDTO goodsStockFlagDTO = new GoodsStockFlagDTO();
        goodsStockFlagDTO.setGoodsId(goodsId);
        goodsStockFlagDTO.setItemId(itemId);
        // goodsStockFlagDTO.setUnderStockFlag(stockUsableQuantity > 0 ? GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_NO
        // : GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_YES);
        // 在库是否有货
        goodsStockFlagDTO.setUnderStockFlag(stockQuantity.get(0) > 0 ? GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_NO
            : GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_YES);
        // 在途是否有货
        goodsStockFlagDTO.setWhetherOutOfStockInTransit(stockQuantity.get(1) > 0
            ? GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_NO : GoodsConstant.GOODS_ITEM_UNDER_STOCK_FLAG_YES);

        goodsStockFlagDTO.setWarehouseIdArr(toWarehouseIdArr(stockDOS));
        goodsStockFlagDTOList.add(goodsStockFlagDTO);
        return stockUsableQuantity;
    }

    private static Boolean existVmiFlag(List<WarehouseInStockDO> list) {
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getWarehouseId() == 0) {
                return true;
            }
        }
        return false;
    }

}
