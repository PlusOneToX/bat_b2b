package com.bat.thirdparty.erp.convertor;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bat.dubboapi.thirdparty.erp.dto.goods.ErpPriceFieldListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.ErpPriceFieldRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO;

/**
 * 订单ERP
 */
@Component
public class ErpGoodsConvertor {

    public static Map<Integer, GoodsItemPriceRpcDTO> toGoodsItemPriceRpcDTOMap(List<Integer> itemErpIds,
        List<ErpPriceFieldListRpcQry> erpPriceFields, List<List<Object>> erpPriceResult) {
        Map<Integer, GoodsItemPriceRpcDTO> rpcDTOMap = new HashMap<>();

        itemErpIds.forEach(itemErpId -> {
            GoodsItemPriceRpcDTO rpcDTO = new GoodsItemPriceRpcDTO();
            rpcDTO.setItemErpId(itemErpId);
            rpcDTOMap.put(itemErpId, rpcDTO);
        });
        if (!CollectionUtils.isEmpty(erpPriceResult)) {
            Map<Integer, String> XSJMMap = new HashMap<>();
            // XSJMB0001价目表，状态：A 已审核，U 未审核，I 审核中，R 重新审核
            for (List<Object> objs : erpPriceResult) {
                if (objs.get(0).toString() == null || !"A".equals(objs.get(2).toString())) {
                    continue;
                }
                /** FMaterialId */
                Integer erpId = Integer.valueOf(objs.get(0).toString());
                // 优先使用XSJMB0001价目表(当商品在XSJMB0001存在时，优先使用XSJMB0001)
                String XSJM = XSJMMap.get(erpId);
                if (!StringUtils.isEmpty(XSJM) && XSJM.equals("XSJMB0001")) {
                    continue;
                }
                XSJM = objs.get(1).toString();
                XSJMMap.put(erpId, XSJM);
                GoodsItemPriceRpcDTO rpcDTO = rpcDTOMap.get(erpId);
                List<ErpPriceFieldRpcDTO> priceList = rpcDTO.getPriceList();
                if (priceList == null) {
                    priceList = new ArrayList<>();
                    rpcDTO.setPriceList(priceList);
                }
                /** 价格 */
                for (int i = 3; i < objs.size(); i++) {
                    if (objs.get(i) != null) {
                        BigDecimal salePrice = new BigDecimal(objs.get(i).toString());
                        // 获取第一个价格为默认销售价
                        if (salePrice.compareTo(new BigDecimal(0)) != 0 && i == 3) {
                            rpcDTO.setSalePrice(salePrice);
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
        return rpcDTOMap;
    }

    public static void toGoodsItemPriceRpcDTOMap(Map<Integer, GoodsItemPriceRpcDTO> rpcDTOMap,
        List<List<Object>> erpCostPriceResult) {
        if (!CollectionUtils.isEmpty(erpCostPriceResult)) {
            for (List<Object> obj : erpCostPriceResult) {
                /** FMaterialId */
                Object value = obj.get(0);
                Integer erpId = Integer.valueOf(value.toString());
                GoodsItemPriceRpcDTO rpcDTO = rpcDTOMap.get(erpId);
                rpcDTO.setCostPrice(new BigDecimal(obj.get(1).toString()));
            }
        }
    }

}
