package com.bat.order.service.third.factory.haixing.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderQueCmd;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.third.factory.validator.FactoryValidatorQryExe;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/30 12:08
 */
@Component
@Slf4j
public class HaixingConvertor {

    @Resource
    private FactoryValidatorQryExe factoryValidatorQryExe;

    @SneakyThrows
    public HaiXingOrderAddCmd toHaixingOrderAddCmd(Integer orderId, String factoryCode) {
        OrderInfoDO orderInfo = factoryValidatorQryExe.getOrderInfo(orderId);
        OrderDeliveryDO orderDeliveryDO = factoryValidatorQryExe.getOrderDelivery(orderId, factoryCode);
        OrderDistributorDataDO orderDistributorData = factoryValidatorQryExe.getOrderDistributorData(orderId);

        HaiXingOrderAddCmd addCmd = new HaiXingOrderAddCmd();
        addCmd.setSellerOrderNo(orderInfo.getOrderNo());
        addCmd.setMemo(orderDistributorData.getRemark());

        addCmd.setAddress(orderDeliveryDO.getProvinceName() + orderDeliveryDO.getCityName()
            + orderDeliveryDO.getDistrictName() + orderDeliveryDO.getAddress());
        addCmd.setProvince(orderDeliveryDO.getProvinceName());
        addCmd.setCity(orderDeliveryDO.getCityName());
        addCmd.setCounty(orderDeliveryDO.getDistrictName());
        addCmd.setContacts(orderDeliveryDO.getUserName());
        addCmd.setMobileNumber(orderDeliveryDO.getMobile());

        addCmd.setStoreCode("00683");
        // JSONObject orderCustomizationJson = new JSONObject();
        // // orderCustomizationJson.put("customer", obj.getDistributorId());
        // addCmd.setOrderCustomization(orderCustomizationJson.toString());

        Map<Integer, OrderGoodsDO> orderGoodsDOMap = factoryValidatorQryExe.getOrderGoodsMap(orderInfo.getId());
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = factoryValidatorQryExe.listOrderGoodsDiy(orderId, factoryCode);
        List<HaiXingOrderAddCmd.Goods> goodsList = new ArrayList<>();
        // 材质和型号关联关系集合 、key 为model_id+"_"+material_id
        Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap = new HashMap<>();
        for (OrderGoodsDiyDO orderGoodsDiyDO : orderGoodsDiyDOS) {
            HaiXingOrderAddCmd.Goods goods = new HaiXingOrderAddCmd.Goods();
            goods.setGoodsImg(orderGoodsDiyDO.getPreviewImage());
            goods.setDiyFileUrl(orderGoodsDiyDO.getGenerateImage());
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsDiyDO.getOrderGoodsId());
            goods.setGoodsNum(orderGoodsDO.getItemCount());
            ModelMaterialRelevanceDTORpcQry qry =
                factoryValidatorQryExe.getModelMaterialRelevanceByModelIdAndMaterialId(relevanceDTORpcQryMap,
                    orderGoodsDiyDO.getModelId(), orderGoodsDiyDO.getMaterialId());
            goods.setSkuCode(qry.getThirdSku());
            goods.setGoodsCustomization(goodsCustomization(orderGoodsDiyDO, orderGoodsDO));
            goodsList.add(goods);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(goodsList);
        addCmd.setGoods(json);
        log.info("组装完的参数 json:{}", JSON.toJSONString(addCmd));
        return addCmd;
    }

    /**
     * 获取商品定制数据
     * 
     * @return
     * @param orderGoods
     * @param orderGoodsDO
     */
    private String goodsCustomization(OrderGoodsDiyDO orderGoods, OrderGoodsDO orderGoodsDO) {
        JSONObject json = new JSONObject();
        String barCode = orderGoodsDO.getBarCode();
        if (barCode == null) {
            barCode = "";
        }
        String itemCode = orderGoodsDO.getItemCode();
        if (itemCode == null) {
            itemCode = "";
        }
        json.put("条形码", barCode + "/" + itemCode);
        json.put("货品中文名称", orderGoodsDO.getItemName());
        GoodsItemRpcDTO goodsItemRpcDTO = factoryValidatorQryExe.goodsItemByCode(itemCode);
        PictureDTORpcQry picture = factoryValidatorQryExe.getPictureById(orderGoods.getPictureId());
        json.put("货品英文名称", goodsItemRpcDTO.getItemNameEn());
        json.put("图片中文名称", picture.getName());
        json.put("图片英文名称", picture.getEnglishName());
        json.put("图片ID", orderGoods.getPictureId() + "/" + picture.getCode());
        json.put("重量", orderGoodsDO.getWeight());
        json.put("标签url", orderGoods.getLabelUrl());
        return json.toJSONString();
    }

    public HaiXingOrderQueCmd toHaixingOrderQueCmd(Integer orderId, String factoryCode) {
        HaiXingOrderQueCmd queCmd = new HaiXingOrderQueCmd();
        queCmd.setOrderId(orderId);
        return queCmd;
    }
}
