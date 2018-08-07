package com.bat.order.service.third.factory.feikuai.convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.factory.feikuai.FeiKuaiOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderQueCmd;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.third.factory.validator.FactoryValidatorQryExe;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/30 12:08
 */
@Component
@Slf4j
public class FeiKuaiConvertor {

    @Resource
    private FactoryValidatorQryExe factoryValidatorQryExe;

    @SneakyThrows
    public FeiKuaiOrderAddCmd toFeiKuaiOrderAddCmd(Integer orderId, String factoryCode) {
        OrderInfoDO orderInfo = factoryValidatorQryExe.getOrderInfo(orderId);
        OrderDeliveryDO orderDeliveryDO = factoryValidatorQryExe.getOrderDelivery(orderId, factoryCode);
        OrderDistributorDataDO orderDistributorData = factoryValidatorQryExe.getOrderDistributorData(orderId);
        LogisticsRpcDTO logistics = factoryValidatorQryExe.getLogisticsByDistributionId(orderDeliveryDO.getDistributionId());

        FeiKuaiOrderAddCmd addCmd = new FeiKuaiOrderAddCmd();
        // 主订单数据组装
        FeiKuaiOrderAddCmd.Trade trade = new FeiKuaiOrderAddCmd.Trade();
        addCmd.setTrade(trade);

        trade.setTid(orderInfo.getOrderNo());
        trade.setReceiver_address(orderDeliveryDO.getProvinceName() + orderDeliveryDO.getCityName()
            + orderDeliveryDO.getDistrictName() + orderDeliveryDO.getAddress());
        trade.setReceiver_state(orderDeliveryDO.getProvinceName());
        trade.setReceiver_city(orderDeliveryDO.getCityName());
        trade.setReceiver_district(orderDeliveryDO.getDistrictName());
        trade.setReceiver_name(orderDeliveryDO.getUserName());
        trade.setReceiver_mobile(orderDeliveryDO.getMobile());
        trade.setReceiver_zip(orderDeliveryDO.getZipCode());
        trade.setCreated(orderInfo.getCreateTime().getTime()/1000);
        if(orderDistributorData.getPayTime() != null){
            trade.setPay_time(orderDistributorData.getPayTime().getTime()/1000);
        }
        // 获取配送方式代码
        if(logistics != null) {
            trade.setCp_code(logistics.getLogisticsFactoryId());
        }
        trade.setRemark(orderDistributorData.getRemark());
        trade.setBuyer_remark(orderDistributorData.getRemark());
        trade.setBuyer_nick(orderDistributorData.getDistributorName());
        // 子订单数据组装
        Map<Integer, OrderGoodsDO> orderGoodsDOMap = factoryValidatorQryExe.getOrderGoodsMap(orderInfo.getId());
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = factoryValidatorQryExe.listOrderGoodsDiy(orderId, factoryCode);
        List<FeiKuaiOrderAddCmd.Order> orders = new ArrayList<>();
        List<FeiKuaiOrderAddCmd.Barcode> barcodes = new ArrayList<>();
        addCmd.setOrder(orders);
        addCmd.setBarcode(barcodes);
        // 材质和型号关联关系集合 、key 为model_id+"_"+material_id
        Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap = new HashMap<>();
        orderGoodsDiyDOS.forEach(orderGoodsDiyDO ->{
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsDiyDO.getOrderGoodsId());
            // 子订单信息
            FeiKuaiOrderAddCmd.Order order = new FeiKuaiOrderAddCmd.Order();
            orders.add(order);
            order.setPic_path(orderGoodsDiyDO.getPreviewImage());
            String oid = String.valueOf(orderGoodsDiyDO.getOrderGoodsId());
            order.setOid(oid);
            order.setNum(orderGoodsDO.getItemCount());
            order.setPrice(new BigDecimal(0.00));
            if(orderGoodsDiyDO.getPictureId() != null && orderGoodsDiyDO.getPictureId() != 0){
                PictureDTORpcQry picture = factoryValidatorQryExe.getPictureById(orderGoodsDiyDO.getPictureId());
                order.setTitle(orderGoodsDiyDO.getModelName()+" "+picture.getName());
            }else {
                order.setTitle(orderGoodsDiyDO.getModelName());
            }
            order.setSku_properties_name(orderGoodsDiyDO.getMaterialName()+ " "+orderGoodsDiyDO.getModelName());
            ModelMaterialRelevanceDTORpcQry modelMaterial =
                    factoryValidatorQryExe.getModelMaterialRelevanceByModelIdAndMaterialId(relevanceDTORpcQryMap,
                            orderGoodsDiyDO.getModelId(), orderGoodsDiyDO.getMaterialId());
            order.setSku_id(String.valueOf(modelMaterial.getId()));
            // 配货信息
            FeiKuaiOrderAddCmd.Barcode barcode = new FeiKuaiOrderAddCmd.Barcode();
            barcodes.add(barcode);
            barcode.setOid(oid);
            barcode.setPic_path(orderGoodsDiyDO.getGenerateImage());
            // 生产手机型号和材质关键字段
            barcode.setWms_goods_id(Integer.valueOf(modelMaterial.getThirdSku()));
            barcode.setOuter_iid(modelMaterial.getMaterialSku()+" "+modelMaterial.getModelSku());
            barcode.setOrder_sjbm(modelMaterial.getModelSku()+"+"+orderGoodsDiyDO.getPictureId());
        });
        log.info("组装完的参数 json:{}", JSON.toJSONString(addCmd));
        return addCmd;
    }
}
