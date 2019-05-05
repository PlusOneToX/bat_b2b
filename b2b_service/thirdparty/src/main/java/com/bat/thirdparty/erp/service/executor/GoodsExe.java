package com.bat.thirdparty.erp.service.executor;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.thirdparty.erp.convertor.ErpGoodsConvertor;
import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import com.bat.thirdparty.erp.manager.ErpDataManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.thirdparty.erp.dto.goods.ErpPriceFieldListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemErpListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemPriceErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.ErpPriceFieldRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO;
import com.bat.thirdparty.erp.api.request.BillQueryRequest;
import com.bat.thirdparty.erp.api.request.CostPriceListRequest;
import com.bat.thirdparty.erp.api.request.MaterialAllRequest;
import com.bat.thirdparty.erp.api.request.SalPriceListRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/17 17:44
 */
@Slf4j
@Component
public class GoodsExe {

    @Resource
    private ErpDataManager erpDataManager;

    /**
     * 获取erp货品信息列表
     *
     * @param qry
     * @return
     */
    public List<GoodsItemErpRpcDTO> listGoodsItemErp(GoodsItemErpListRpcQry qry) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        /** 获取货品信息 */
        List<GoodsItemErpRpcDTO> goodsItemErpRpcDTOList = listGoodsItemErpInfo(qry, client);
        /** 促销标识 正常：5caebbaa6c7861 清仓: 5caebbb86c7863 B: 5e9fec57c26d75 C：5f0c2feda00210 D：5f0c3003a00212 */
        String content = qry.getContent();
        if (!CollectionUtils.isEmpty(goodsItemErpRpcDTOList)
            && (content == null || !content.equals("5caebbb86c7863") || !content.equals("5f0c3003a00212"))) {
            List<Integer> erpIds =
                goodsItemErpRpcDTOList.stream().map(GoodsItemErpRpcDTO::getItemErpId).collect(Collectors.toList());
            String erpIdFields = StringUtils.join(erpIds, ",");
            Map<Integer, GoodsItemErpRpcDTO> itemErpRpcDTOMap = goodsItemErpRpcDTOList.stream()
                .collect(Collectors.toMap(GoodsItemErpRpcDTO::getItemErpId, goodsItemErpApiDTO -> goodsItemErpApiDTO));
            /** 查找等级价格 */
            List<ErpPriceFieldListRpcQry> erpPriceFields = qry.getErpPriceFields();
            if (!CollectionUtils.isEmpty(erpPriceFields)) {
                listGoodsItemPrice(erpPriceFields, itemErpRpcDTOMap, erpIdFields, client);
            }
            /** 查找成本价 */
            listGoodsItemCostPrice(erpIdFields, itemErpRpcDTOMap, client);
            /** 查找箱规信息 */
//            listErpGoodsItemsBoxInfo(erpIdFields, goodsItemErpRpcDTOList, client);
        }
        return goodsItemErpRpcDTOList;
    }

    /**
     * 同步erp装箱信息
     *
     * @param erpIdFields
     * @param itemErpRpcDTOS
     * @param client
     * @throws Exception
     */
    private void listErpGoodsItemsBoxInfo(String erpIdFields, List<GoodsItemErpRpcDTO> itemErpRpcDTOS,
        K3CloudApiClient client) throws Exception {
        Map<String, GoodsItemErpRpcDTO> itemErpRpcDTOCodeMap = itemErpRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemErpRpcDTO::getItemCode, goodsItemErpApiDTO -> goodsItemErpApiDTO));
        List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS = listErpGoodsItemsBoxInfo(erpIdFields, client);
        if (!CollectionUtils.isEmpty(goodsItemBoxRpcDTOS)) {
            goodsItemBoxRpcDTOS.forEach(goodsItemBoxRpcDTO -> {
                GoodsItemErpRpcDTO goodsItemErpRpcDTO = itemErpRpcDTOCodeMap.get(goodsItemBoxRpcDTO.getItemCode());
                List<GoodsItemBoxRpcDTO> boxInfoList = goodsItemErpRpcDTO.getBoxInfoList();
                if (null == boxInfoList) {
                    boxInfoList = new ArrayList<>();
                    goodsItemErpRpcDTO.setBoxInfoList(boxInfoList);
                }
                boxInfoList.add(goodsItemBoxRpcDTO);
            });
        }
    }

    /**
     * 同步erp装箱信息
     * 
     * @param erpIdFields
     * @param client
     * @return
     * @throws Exception
     */
    private List<GoodsItemBoxRpcDTO> listErpGoodsItemsBoxInfo(String erpIdFields, K3CloudApiClient client)
        throws Exception {
        List<List<Object>> result = getErpGoodsItemsBoxResult(erpIdFields, client);
        List<GoodsItemBoxRpcDTO> boxInfoList = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            for (List<Object> obj : result) {
                // F_PAEZ_BaseProperty,FCHILDITEMNAME,FMATERIALID.FNumber,FMATERIALIDCHILD.FNumber,FDENOMINATOR,FMATERIALIDCHILD.FLENGTH,FMATERIALIDCHILD.FWIDTH,FMATERIALIDCHILD.FHEIGHT,FMATERIALIDCHILD.FNETWEIGHT1,FMATERIALID
                /** 箱规类型，物料名称，子项物料编码，装箱数量,长，宽，高，整箱重量 */
                GoodsItemBoxRpcDTO boxRpcDTO = new GoodsItemBoxRpcDTO();
                /** 箱规类型 */
                String value = obj.get(0).toString();
                if (!StringUtils.isEmpty(value)) {
                    boxRpcDTO.setBoxType(value);
                    /** 物料名称 */
                    String boxName = obj.get(1).toString();
                    boxRpcDTO.setBoxName(boxName);
                    /** 父项物料编码 */
                    String itemCode = obj.get(2).toString();
                    boxRpcDTO.setItemCode(itemCode);
                    /** 子项物料编码 */
                    String boxErpId = obj.get(3).toString();
                    boxRpcDTO.setBoxErpId(boxErpId);
                    /** 装箱数量 */
                    String boxNum = obj.get(4).toString();
                    boxRpcDTO.setBoxNum(new BigDecimal(boxNum).setScale(3));
                    /** 长 */
                    String boxLength = obj.get(5).toString();
                    boxRpcDTO.setBoxLength(new BigDecimal(boxLength).setScale(3));
                    /** 宽 */
                    String boxWidth = obj.get(6).toString();
                    boxRpcDTO.setBoxWidth(new BigDecimal(boxWidth).setScale(3));
                    /** 高 */
                    String boxHeight = obj.get(7).toString();
                    boxRpcDTO.setBoxHeight(new BigDecimal(boxHeight).setScale(3));
                    /** 整箱重量 */
                    String boxWeight = obj.get(8).toString();
                    boxRpcDTO.setBoxWeight(new BigDecimal(boxWeight).setScale(3));
                    boxInfoList.add(boxRpcDTO);
                }
            }
        }
        return boxInfoList;
    }

    /**
     * 获取erp装箱信息
     *
     * @param erpIdFields
     * @param client
     * @return
     * @throws Exception
     */
    private List<List<Object>> getErpGoodsItemsBoxResult(String erpIdFields, K3CloudApiClient client) throws Exception {
        BillQueryRequest request = new BillQueryRequest();
        request.setFormId("ENG_BOM");
        request.setFieldKeys(
            "F_PAEZ_BaseProperty,FCHILDITEMNAME,FMATERIALID.FNumber,FMATERIALIDCHILD.FNumber,FDENOMINATOR,FMATERIALIDCHILD.FLENGTH,FMATERIALIDCHILD.FWIDTH,FMATERIALIDCHILD.FHEIGHT,FMATERIALIDCHILD.FNETWEIGHT1,FMATERIALID");
        request.setFilterString("FMATERIALID in (" + erpIdFields
            + ") AND F_PAEZ_BaseProperty is not null And FDocumentStatus = 'C' And FForbidStatus = 'A' And FMATERIALIDCHILD.FDocumentStatus = 'C' And FMATERIALIDCHILD.FForbidStatus = 'A' ");
        String json = JSONObject.toJSONString(request);
        List<List<Object>> lists = client.executeBillQuery(json);
        return lists;
    }

    /**
     * 同步erp成本价
     *
     * @param erpIdFields
     * @param itemErpApiDTOMap
     * @param client
     * @throws Exception
     */
    private void listGoodsItemCostPrice(String erpIdFields, Map<Integer, GoodsItemErpRpcDTO> itemErpApiDTOMap,
        K3CloudApiClient client) throws Exception {
        List<List<Object>> result = getErpCostPriceResult(erpIdFields, client);
        if (!CollectionUtils.isEmpty(result)) {
            for (List<Object> obj : result) {
                /** FMaterialId */
                Object value = obj.get(0);
                Integer erpId = Integer.valueOf(value.toString());
                GoodsItemErpRpcDTO goodsItemErpRpcDTO = itemErpApiDTOMap.get(erpId);
                try {
                    goodsItemErpRpcDTO.setCostPrice(new BigDecimal(obj.get(1).toString()).setScale(3));
                }catch (Exception e){
                    goodsItemErpRpcDTO.setCostPrice(new BigDecimal(0).setScale(3));
                }
            }
        }
    }

    /**
     * 获取erp成本价
     *
     * @param erpIdFields
     * @param client
     * @return
     * @throws Exception
     */
    private List<List<Object>> getErpCostPriceResult(String erpIdFields, K3CloudApiClient client) throws Exception {
        CostPriceListRequest costPriceListRequest = new CostPriceListRequest();
        String fieldKeys = "FMaterialId,FTaxPrice,FEntryEffectiveDate";
        costPriceListRequest.setFieldKeys(fieldKeys);
        costPriceListRequest.setFilterString("FMaterialId in (" + erpIdFields + ")");
        String json = JSONObject.toJSONString(costPriceListRequest);
        List<List<Object>> lists = client.executeBillQuery(json);
        return lists;
    }

    /**
     * 同步erp商品价格
     *
     * @param erpPriceFields
     * @param itemErpApiDTOMap
     * @throws Exception
     */
    private void listGoodsItemPrice(List<ErpPriceFieldListRpcQry> erpPriceFields,
        Map<Integer, GoodsItemErpRpcDTO> itemErpApiDTOMap, String erpIdFields, K3CloudApiClient client)
        throws Exception {
        List<String> priceFieldKeys =
            erpPriceFields.stream().map(ErpPriceFieldListRpcQry::getErpField).collect(Collectors.toList());
        List<List<Object>> erpPriceResult = getErpPriceResult(priceFieldKeys, erpIdFields, client);
        if (!CollectionUtils.isEmpty(erpPriceResult)) {
            Map<Integer, String> XSJMMap = new HashMap<>();
            for (List<Object> objs : erpPriceResult) {
                // 只处理标准价目表
                if (objs.get(0).toString() == null || !"A".equals(objs.get(2).toString())) {
                    continue;
                }
                /** FMaterialId */
                Integer erpId = Integer.valueOf(objs.get(0).toString());
                // 优先使用XSJMB0001价目表(当商品在XSJMB0001存在时，优先使用XSJMB0001)
                String XSJM = XSJMMap.get(erpId);
                if (!org.springframework.util.StringUtils.isEmpty(XSJM) && XSJM.equals("XSJMB0001")) {
                    continue;
                }
                XSJM = objs.get(1).toString();
                XSJMMap.put(erpId, XSJM);
                GoodsItemErpRpcDTO goodsItemErpRpcDTO = itemErpApiDTOMap.get(erpId);
                List<ErpPriceFieldRpcDTO> priceList = goodsItemErpRpcDTO.getPriceList();
                if (priceList == null) {
                    priceList = new ArrayList<>();
                    goodsItemErpRpcDTO.setPriceList(priceList);
                }
                /** 价格 */
                for (int i = 3; i < objs.size(); i++) {
                    if (objs.get(i) != null) {
                        BigDecimal salePrice = new BigDecimal(objs.get(i).toString());
                        // 获取第一个价格为默认销售价
                        if (salePrice.compareTo(new BigDecimal(0)) != 0 && i == 3) {
                            goodsItemErpRpcDTO.setSalePrice(salePrice);
                        }
                        Integer goodsItemGradeId = erpPriceFields.get(i - 3).getId();
                        Optional<ErpPriceFieldRpcDTO> optional = priceList.stream()
                            .filter(price -> price.getGoodsItemGradeId().equals(goodsItemGradeId)).findFirst();
                        ErpPriceFieldRpcDTO dto = null;
                        if (optional == null || !optional.isPresent()) {
                            dto = new ErpPriceFieldRpcDTO();
                            dto.setPrice(salePrice);
                            dto.setGoodsItemGradeId(goodsItemGradeId);
                            priceList.add(dto);
                        } else {
                            dto = optional.get();
                            dto.setPrice(salePrice);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取erp等级价格
     *
     * @param priceFieldKeys
     * @param fMaterialIds
     * @return
     * @throws Exception
     */
    private List<List<Object>> getErpPriceResult(List<String> priceFieldKeys, String fMaterialIds,
        K3CloudApiClient client) throws Exception {
        SalPriceListRequest requestPrice = new SalPriceListRequest();
        String fieldKeys = "FMaterialId,FNumber,FRowAuditStatus,";
        fieldKeys = fieldKeys + StringUtils.join(priceFieldKeys, ",");
        requestPrice.setFieldKeys(fieldKeys);
        requestPrice.setFilterString("FMaterialId in (" + fMaterialIds + ")");
        String json = JSONObject.toJSONString(requestPrice);
        List<List<Object>> lists = client.executeBillQuery(json);
        log.info(lists.toString());
        return lists;
    }

    /**
     * 根据条件查找erp物料基本信息
     *
     * @param qry
     * @param client
     * @return
     * @throws Exception
     */
    private List<GoodsItemErpRpcDTO> listGoodsItemErpInfo(GoodsItemErpListRpcQry qry, K3CloudApiClient client)
        throws Exception {
        MaterialAllRequest erpRequest = new MaterialAllRequest();
        // 对应erp产品型号：F_PAEZ_Text2
        // 对应erp系列：F_PAEZ_XL
        // 对应erp应用设备：F_PAEZ_YYSB
        // 对应erp产品型号en：F_PAEZ_CPXH
        // 对应erp系列en：F_PAEZ_XL1
        // 对应erp应用设备en：F_PAEZ_YYSB1
        // 对应erp固定提前期:FFixLeadTime
        // 对应erp计划策略模式:F_PAEZ_MRP1
        erpRequest.setFieldKeys("FBARCODE,FGROSSWEIGHT,FName,FName1,FNumber,FMaterialId,FLENGTH,FWIDTH,FHEIGHT,"
            + "F_PAEZ_MOQ,FCategoryID.FNumber,F_PAEZ_Assistant2.FNumber,F_PAEZ_ASSISTANT3,"
            + "F_PAEZ_Text2,F_PAEZ_XL,F_PAEZ_YYSB,F_PAEZ_CPXH,F_PAEZ_XL1,F_PAEZ_YYSB1,FFixLeadTime,F_PAEZ_MRP1");
        erpRequest.setLimit(qry.getSize() + "");
        String filter = " F_PAEZ_COMBO='上架' and FUseOrgId.FNumber='" + qry.getOrgId() + "'";
        if (StringUtils.isNotBlank(qry.getContent())) {
            String search = "'%" + qry.getContent() + "%'";
            filter = filter + " and ( FName like  " + search + " or FBARCODE like " + search + " or FNumber like "
                + search + " or F_PAEZ_ASSISTANT3 like " + search + "  )";
        }
        erpRequest.setFilterString(filter);
        erpRequest.setStartRow((qry.getPage() - 1) * qry.getSize() + "");
        List<GoodsItemErpRpcDTO> itemErpRpcDTOS = listGoodsItemErp(JSONObject.toJSONString(erpRequest), client);
        return itemErpRpcDTOS;
    }

    /**
     * 根据条件获取erp货品信息
     * 
     * @param erpRequest
     * @param client
     * @return
     * @throws Exception
     */
    private List<GoodsItemErpRpcDTO> listGoodsItemErp(String erpRequest, K3CloudApiClient client) throws Exception {
        List<List<Object>> result = client.executeBillQuery(erpRequest);
        List<GoodsItemErpRpcDTO> itemErps = new ArrayList<>();
        log.info("货品同步的信息：{}"+JSONObject.toJSONString(result));
        if (result != null && !result.isEmpty()) {
            for (List<Object> obj : result) {
                GoodsItemErpRpcDTO itemErp = new GoodsItemErpRpcDTO();
                /** 条码:FBARCODE */
                Object value = obj.get(0);
                if (value != null) {
                    itemErp.setBarCode(value.toString());
                }
                /** 毛重:FGROSSWEIGHT */
                value = obj.get(1);
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    itemErp.setWeight(new BigDecimal(value.toString()).setScale(3));
                }
                /** 名称:FName */
                value = obj.get(2);
                if (value != null) {
                    itemErp.setItemName(value.toString().trim());
                }
                /** 货品英文名称 FName1 */
                // Object nameEnValue = obj.get(3);
                // if (nameEnValue != null) {
                // itemErp.setItemNameEn(nameEnValue.toString().trim());
                // }
                /** 编码:FNumber */
                value = obj.get(4);
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    itemErp.setItemCode(value.toString().trim());
                }
                /** erp物料id:FMaterialId */
                value = obj.get(5);
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    itemErp.setItemErpId(Integer.valueOf(value.toString()));
                }
                /** 长:FLENGTH */
                Object Lvalue = obj.get(6);
                if (Lvalue != null && StringUtils.isNotBlank(Lvalue.toString())) {
                    itemErp.setLength(new BigDecimal(Lvalue.toString()).setScale(3));
                }
                /** 宽:FWIDTH */
                Object Wvalue = obj.get(7);
                if (Wvalue != null && StringUtils.isNotBlank(Wvalue.toString())) {
                    itemErp.setWidth(new BigDecimal(Wvalue.toString()).setScale(3));
                }
                /** 高:FHEIGHT */
                Object Hvalue = obj.get(8);
                if (Hvalue != null && StringUtils.isNotBlank(Hvalue.toString())) {
                    itemErp.setHeight(new BigDecimal(Hvalue.toString()).setScale(3));
                }
                /** 预售最少购买数量:F_PAEZ_MOQ */
                Object moqValue = obj.get(9);
                if (moqValue != null) {
                    itemErp.setMoq(moqValue.toString());
                }
                /** 存货类别:FCategoryID.FNumber */
                Object categoryValue = obj.get(10);
                if (categoryValue != null) {
                    itemErp.setCategoryErpNo(categoryValue.toString());
                }
                /** 商品生命周期:F_PAEZ_Assistant2.FNumber */
                Object lifeCycle = obj.get(11);
                if (lifeCycle != null && StringUtils.isNotBlank(lifeCycle.toString())) {
                    itemErp.setLifeCycle(Short.valueOf(lifeCycle.toString()));
                }
                /** ERP促销状态:F_PAEZ_ASSISTANT3 */
                Object clearance = obj.get(12);
                if (clearance != null && StringUtils.isNotBlank(clearance.toString())) {
                    itemErp.setPromotionStatus(String.valueOf(clearance.toString()));
                }
                // 尺寸单位
                itemErp.setUnit("mm");
                itemErps.add(itemErp);
            }
        }
        return itemErps;
    }

    /**
     * 根据货品itemErpIds获取erp货品等级价格
     * 
     * @param qry
     * @return
     */
    public List<GoodsItemPriceRpcDTO> listGoodsItemPriceErp(GoodsItemPriceErpRpcQry qry) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        String erpIdFields = StringUtils.join(qry.getItemErpIds(), ",");
        List<String> priceFieldKeys =
            qry.getErpPriceFields().stream().map(ErpPriceFieldListRpcQry::getErpField).collect(Collectors.toList());
        // 获取erp等级价格
        List<List<Object>> erpPriceResult = getErpPriceResult(priceFieldKeys, erpIdFields, client);
        Map<Integer, GoodsItemPriceRpcDTO> rpcDTOMap =
            ErpGoodsConvertor.toGoodsItemPriceRpcDTOMap(qry.getItemErpIds(), qry.getErpPriceFields(), erpPriceResult);
        // 获取erp成本价
        List<List<Object>> erpCostPriceResult = getErpCostPriceResult(erpIdFields, client);
        ErpGoodsConvertor.toGoodsItemPriceRpcDTOMap(rpcDTOMap, erpCostPriceResult);
        return rpcDTOMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 根据itemErpIds获取更新货品信息
     * 
     * @param itemErpIds
     * @return
     */
    public List<GoodsItemErpRpcDTO> listGoodsItemErp(List<Integer> itemErpIds) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        String itemErpIdsStr = itemErpIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        /** 获取货品信息 */
        List<GoodsItemErpRpcDTO> itemErpRpcDTOS = listGoodsItemErpInfo(itemErpIdsStr, client);
        return itemErpRpcDTOS;
    }

    /**
     * 根据itemErpIds物料基本信息
     *
     * @param itemErpIds
     * @param client
     * @return
     * @throws Exception
     */
    private List<GoodsItemErpRpcDTO> listGoodsItemErpInfo(String itemErpIds, K3CloudApiClient client) throws Exception {
        MaterialAllRequest erpRequest = new MaterialAllRequest();
        // 对应erp产品型号：F_PAEZ_Text2
        // 对应erp系列：F_PAEZ_XL
        // 对应erp应用设备：F_PAEZ_YYSB
        // 对应erp产品型号en：F_PAEZ_CPXH
        // 对应erp系列en：F_PAEZ_XL1
        // 对应erp应用设备en：F_PAEZ_YYSB1
        // 对应erp固定提前期:FFixLeadTime
        // 对应erp计划策略模式:F_PAEZ_MRP1
        erpRequest.setFieldKeys("FBARCODE,FGROSSWEIGHT,FName,FName1,FNumber,FMaterialId,FLENGTH,FWIDTH,FHEIGHT,"
            + "F_PAEZ_MOQ,FCategoryID.FNumber,F_PAEZ_Assistant2.FNumber,F_PAEZ_ASSISTANT3,"
            + "F_PAEZ_Text2,F_PAEZ_XL,F_PAEZ_YYSB,F_PAEZ_CPXH,F_PAEZ_XL1,F_PAEZ_YYSB1,FFixLeadTime,F_PAEZ_MRP1");
        String filter = " FMaterialId in (" + itemErpIds + ")";
        erpRequest.setFilterString(filter);
        erpRequest.setLimit(2000 + "");
        erpRequest.setStartRow(0 + "");
        List<GoodsItemErpRpcDTO> itemErpRpcDTOS = listGoodsItemErp(JSONObject.toJSONString(erpRequest), client);
        return itemErpRpcDTOS;
    }

    /**
     * 根据itemErpIds获取erp装箱信息
     * 
     * @param itemErpIds
     * @return
     * @throws Exception
     */
    public List<GoodsItemBoxRpcDTO> listGoodsItemBoxErp(List<Integer> itemErpIds) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        String itemErpIdsStr = itemErpIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS = listErpGoodsItemsBoxInfo(itemErpIdsStr, client);
        return goodsItemBoxRpcDTOS;
    }

}
