package com.bat.order.service.third.factory.maike.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.constant.MaikeConstant;
import com.bat.order.service.third.factory.validator.FactoryValidatorQryExe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.maike.*;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.local.api.OrderDetailPictureLocalServiceI;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@Component
public class MaikeConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaikeConvertor.class);

    @Resource
    private FactoryValidatorQryExe factoryValidatorQryExe;

    @Autowired
    private OrderDetailPictureLocalServiceI orderDetailPictureLocalServiceI;

    public MaikeOrderAddCmd toMaikeOrderAddCmd(Integer orderId) {
        String factoryNo = FactoryEnum.MK.name().toUpperCase();
        LOGGER.info("组装工厂,{}", factoryNo);
        LOGGER.info("组装工厂参数,{}", orderId);
        MaikeOrderAddCmd maikeOrderAddCmd = new MaikeOrderAddCmd();
        OrderInfoDO orderInfoDO = factoryValidatorQryExe.getOrderInfo(orderId);
        OrderDistributorDataDO orderDistributorDataDO = factoryValidatorQryExe.getOrderDistributorData(orderId);
        OrderExtendDataDO orderExtendDataDO = factoryValidatorQryExe.getOrderExtendData(orderInfoDO.getId());
        List<OrderGoodsDO> orderGoodsDOList = factoryValidatorQryExe.listOrderGoods(orderInfoDO.getId());
        List<OrderGoodsDiyDO> orderGoodsDiyDOList =
            factoryValidatorQryExe.listOrderGoodsDiy(orderInfoDO.getId(), factoryNo);

        // 生成未生成的标签(取第一个节点的分销商id、不要取同步erp的)
        OrderDistributorDataDO firstOrder = factoryValidatorQryExe.getFirstTreeNode(orderId);
        Integer finalOrderId = orderId;
        orderGoodsDiyDOList.stream().forEach(orderGoodsDiyDO -> {
            if (StringUtils.isBlank(orderGoodsDiyDO.getLabelUrl())) {
                // 先生成标签
                throw OrderException.buildException("订单:" + finalOrderId + "尚未生成标签、请先生成标签再同步工厂");
            }
        });

        OrderDeliveryDO orderDeliveryDO = factoryValidatorQryExe.getOrderDelivery(orderId, factoryNo);

        maikeOrderAddCmd.setOrder_number(String.valueOf(orderId));
        maikeOrderAddCmd.setCreator(orderDeliveryDO.getUserName());
        maikeOrderAddCmd.setReal_username(orderDeliveryDO.getUserName());
        // 1、自动审核、0、手动审核
        maikeOrderAddCmd.setAuto_pass(MaikeConstant.AutoPassAuto);
        maikeOrderAddCmd.setSender(MaikeConstant.Senderbat);
        // 判断是否海外订单、海外订单省份是 海外
        Boolean isOverseas = false;
        if (!orderDeliveryDO.getCountryName().contains("中国")) {
            isOverseas = true;
            maikeOrderAddCmd.setOverSeasFlag(true);
        }
        /**
         * 地址信息
         */
        MaikeShipperAddressCmd maikeShipperAddressCmd = getShippingAddressInfo(orderDeliveryDO, isOverseas);
        maikeOrderAddCmd.setShipping_address_info(maikeShipperAddressCmd);

        maikeOrderAddCmd.setLogisticsId(orderDeliveryDO.getDistributionId());

        MaikeSettlementInfo maikeSettlementInfo = setOrderSettlementInfo();
        maikeOrderAddCmd.setSettlement_info(maikeSettlementInfo);

        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList =
            factoryValidatorQryExe.listByOrderIdAndDistributorId(orderId, orderDistributorDataDO.getDistributorId());

        MaikeOrderInfo maikeOrderInfo = getOrderInfo(orderDistributorDataDO.getRemark(), orderGoodsDOList,
            orderGoodsDiyDOList, orderGoodsDistributorCostDOList);
        maikeOrderAddCmd.setOrder_info(maikeOrderInfo);
        // 设置工厂编码
        maikeOrderAddCmd.setManufactors(orderGoodsDiyDOList.get(0).getManufactors());
        LOGGER.info("订单推送麦客、参数：" + JSON.toJSONString(maikeOrderAddCmd));
        return maikeOrderAddCmd;
    }

    /**
     * 获取快递地址信息
     *
     * @param orderDeliveryDO
     * @param isOverseas
     *            是否海外订单(省市区 已经校验过了)
     * @return
     */
    private MaikeShipperAddressCmd getShippingAddressInfo(OrderDeliveryDO orderDeliveryDO, Boolean isOverseas) {
        MaikeShipperAddressCmd maikeShipperAddressCmd = new MaikeShipperAddressCmd();
        maikeShipperAddressCmd.setProvince(orderDeliveryDO.getProvinceName());
        maikeShipperAddressCmd.setCity(orderDeliveryDO.getCityName());
        maikeShipperAddressCmd.setDistrict(orderDeliveryDO.getDistrictName());
        maikeShipperAddressCmd.setAddress(orderDeliveryDO.getAddress());
        maikeShipperAddressCmd.setMobile(orderDeliveryDO.getMobile());
        maikeShipperAddressCmd.setConsignee(orderDeliveryDO.getUserName());
        if (isOverseas) {
            maikeShipperAddressCmd.setProvince(null);
            maikeShipperAddressCmd.setCity(null);
            maikeShipperAddressCmd.setDistrict(null);
        }
        return maikeShipperAddressCmd;
    }

    private MaikeSettlementInfo setOrderSettlementInfo() {
        MaikeSettlementInfo info = new MaikeSettlementInfo();
        info.setWay(0);
        info.setOrder_percent(1.00D);
        info.setShipments_percent(1.00D);
        info.setTake_percent(1.00D);
        return info;
    }

    private MaikeOrderInfo getOrderInfo(String remark, List<OrderGoodsDO> orderGoodList,
        List<OrderGoodsDiyDO> orderGoodsDiyDOList, List<OrderGoodsDistributorCostDO> goodsDistributorCostDOList) {
        MaikeOrderInfo maikeOrderInfo = new MaikeOrderInfo();
        maikeOrderInfo.setRemark(remark);
        maikeOrderInfo.setThird_party_type("DIY_SYS");
        List<MaikeDetailInfo> list = new ArrayList<>();
        LOGGER.info("处理麦客工厂订单参数：" + JSON.toJSONString(orderGoodList));
        Map<Integer, OrderGoodsDO> orderGoodsDOMap =
            orderGoodList.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));

        Map<Integer, OrderGoodsDistributorCostDO> goodsDistributorCostDOMap =
            goodsDistributorCostDOList.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCostDO -> orderGoodsDistributorCostDO));

        // 材质和型号关联关系集合 、key 为model_id+"_"+material_id
        Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap = new HashMap<>();

        List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList =
            orderDetailPictureLocalServiceI.listByOrderId(orderGoodList.get(0).getOrderId());
        // 同步尚未同步到FTP的图片
        orderDetailPictureLocalDOList =
            orderDetailPictureLocalServiceI.setUploadUnSyncToFTP(orderDetailPictureLocalDOList, orderGoodsDiyDOList);
        Map<Integer, OrderDetailPictureLocalDO> pictureLocalDOMap = orderDetailPictureLocalDOList.stream()
            .collect(Collectors.toMap(OrderDetailPictureLocalDO::getOrderGoodsDiyId, orderDetail -> orderDetail));

        for (int x = 0; x < orderGoodsDiyDOList.size(); x++) {
            OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOList.get(x);
            MaikeDetailInfo info = new MaikeDetailInfo();
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsDiyDO.getOrderGoodsId());

            OrderGoodsDistributorCostDO orderGoodsDistributorCostDO =
                goodsDistributorCostDOMap.get(orderGoodsDiyDO.getOrderGoodsId());

            info.setQuantity(orderGoodsDO.getItemCount().intValue());
            info.setPrice(orderGoodsDistributorCostDO.getActualPrice());
            OrderDetailPictureLocalDO pictureLocal = pictureLocalDOMap.get(orderGoodsDiyDO.getId());

            if (pictureLocal == null) {
                LOGGER.info("查询不到麦客的本地FTP信息：" + JSON.toJSONString(orderGoodsDiyDO));
                pictureLocal = orderDetailPictureLocalServiceI.save(orderGoodsDiyDO.getPreviewImage(),
                    orderGoodsDiyDO.getGenerateImage(), orderGoodsDiyDO.getLabelUrl(), orderGoodsDiyDO);
            }
            // 打印图
            info.setFile_path(orderGoodsDiyDO.getGenerateImage());
            info.setEffect_file_path(orderGoodsDiyDO.getPreviewImage());
            info.setLabel_file_path(orderGoodsDiyDO.getLabelUrl());
            // 本地
            info.setFile_local_path(pictureLocal.getGenerateImageUrl());
            info.setEffect_file_local_path(pictureLocal.getImageUrl());
            info.setLabel_file_local_path(pictureLocal.getLabelUrl());
            if (StringUtils.isBlank(pictureLocal.getLabelUrl())) {
                LOGGER.info("再次同步标签信息到麦客FTP：" + JSON.toJSONString(orderGoodsDiyDO));
                String local =
                    orderDetailPictureLocalServiceI.setLabelUrl(orderGoodsDiyDO.getId(), orderGoodsDiyDO.getLabelUrl());
                info.setLabel_file_local_path(local);
            }

            ModelMaterialRelevanceDTORpcQry modelMaterialRelevanceDTORpcQry =
                factoryValidatorQryExe.getModelMaterialRelevanceByModelIdAndMaterialId(relevanceDTORpcQryMap,
                    orderGoodsDiyDO.getModelId(), orderGoodsDiyDO.getMaterialId());

            // MaikeSkuInfo maikeSkuInfo = getSkuInfoByThirdSkuNo(customInfo.getThirdSku());
            info.setArea(String.valueOf(1));
            info.setProduct_name(orderGoodsDO.getItemName());
            info.setSpecification(orderGoodsDiyDO.getModelName());
            info.setUnit("个");
            info.setSku_number(modelMaterialRelevanceDTORpcQry.getThirdSku());
            if (StringUtils.isBlank(modelMaterialRelevanceDTORpcQry.getThirdSku())) {
                LOGGER.error("【" + orderGoodsDiyDO.getMaterialName() + "】和【" + orderGoodsDiyDO.getModelName()
                    + "】找不到麦客系统的sku编码");
                throw OrderException.buildException("【" + orderGoodsDiyDO.getMaterialName() + "】和【"
                    + orderGoodsDiyDO.getModelName() + "】找不到麦客系统的sku编码");
            }
            if (StringUtils.isBlank(info.getLabel_file_local_path())
                || StringUtils.isBlank(info.getLabel_file_path())) {
                LOGGER.error("订单号【" + orderGoodsDO.getOrderId() + "】标签url不能为空" + JSON.toJSONString(info));
                throw OrderException.buildException("订单号【" + orderGoodsDO.getOrderId() + "】标签url不能为空");
            }
            if (StringUtils.isBlank(info.getEffect_file_local_path())
                || StringUtils.isBlank(info.getEffect_file_path())) {
                LOGGER.error("订单号【" + orderGoodsDO.getOrderId() + "】预览图url不能为空" + JSON.toJSONString(info));
                throw OrderException.buildException("订单号【" + orderGoodsDO.getOrderId() + "】预览图url不能为空");
            }
            if (StringUtils.isBlank(info.getFile_local_path()) || StringUtils.isBlank(info.getFile_path())) {
                LOGGER.error("订单号【" + orderGoodsDO.getOrderId() + "】打印图url不能为空" + JSON.toJSONString(info));
                throw OrderException.buildException("订单号【" + orderGoodsDO.getOrderId() + "】打印图url不能为空");
            }
            // 判断打印图是否以pdf、
            if (!info.getFile_path().toLowerCase().endsWith(".pdf")) {
                LOGGER.error("订单号【" + orderGoodsDO.getOrderId() + "】打印图url格式非pdf" + JSON.toJSONString(info));
                throw OrderException.buildException("订单号【" + orderGoodsDO.getOrderId() + "】打印图url非pdf");
            }
            if (!info.getFile_local_path().toLowerCase().endsWith(".pdf")) {
                LOGGER.error("订单号【" + orderGoodsDO.getOrderId() + "】本地打印图url格式非pdf" + JSON.toJSONString(info));
                throw OrderException.buildException("订单号【" + orderGoodsDO.getOrderId() + "】本地打印图url非pdf");
            }
            list.add(info);
        }
        maikeOrderInfo.setDetail_info_list(list);
        return maikeOrderInfo;
    }
}
