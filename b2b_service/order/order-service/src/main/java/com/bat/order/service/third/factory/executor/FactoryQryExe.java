package com.bat.order.service.third.factory.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.error.OrderDistributorErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.label.api.LabelServiceRpc;
import com.bat.dubboapi.flexible.label.dto.LabelDTORpcQry;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderGoodsDiySimpleDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.dubboapi.flexible.model.api.ModelMaterialRelevanceServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.api.PictureServiceRpc;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.cost.OrderGoodsDistributorCostServiceI;
import com.bat.order.api.data.OrderDistributorDataServiceI;
import com.bat.order.api.data.OrderExtendDataServiceI;
import com.bat.order.api.deliver.OrderDeliveryServiceI;
import com.bat.order.api.order.OrderGoodsDiyServiceI;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/26 13:45
 */
@Component
@Slf4j
public class FactoryQryExe {

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @Autowired
    private OrderDistributorDataServiceI orderDistributorDataServiceI;

    @Autowired
    private OrderExtendDataServiceI orderExtendDataServiceI;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderGoodsDiyServiceI orderGoodsDiyServiceI;

    @Autowired
    private OrderDeliveryServiceI orderDeliveryServiceI;

    @Autowired
    private OrderGoodsDistributorCostServiceI orderGoodsDistributorCostServiceI;

    @DubboReference(check = false, timeout = 10000)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private ModelMaterialRelevanceServiceRpc modelMaterialRelevanceServiceRpc;

    @DubboReference(check = false, timeout = 20000)
    private LabelServiceRpc labelServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private PictureServiceRpc pictureServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private SystemLogisticsServiceRpc logisticsServiceRpc;

    public OrderInfoDO getOrderInfo(Integer orderId) {
        OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderId);
        return orderInfoDO;
    }

    public OrderDistributorDataDO getOrderDistributorData(Integer orderId) {
        List<OrderDistributorDataDO> orderDistributorDataDOS =
            orderDistributorDataServiceI.listByCondition(orderId, null, OrderInfoConstant.ORDER_ERP_FLAG_YES);
        if (CollectionUtils.isEmpty(orderDistributorDataDOS)) {
            throw OrderException.buildException(OrderDistributorErrorCode.O_ORDER_DISTRIBUTOR_DATA_NULL);
        }
        return orderDistributorDataDOS.get(0);
    }

    public OrderDistributorDataDO getFirstTreeNode(Integer orderId) {
        return orderDistributorDataServiceI.getFirstTreeNode(orderId);
    }

    public OrderExtendDataDO getOrderExtendData(Integer orderId) {
        return orderExtendDataServiceI.getByOrderId(orderId);
    }

    public List<OrderGoodsDO> listOrderGoods(Integer orderId) {
        return orderGoodsServiceI.listByOrderId(orderId);
    }

    public List<OrderGoodsDiyDO> listOrderGoodsDiy(Integer orderId) {
        return orderGoodsDiyServiceI.listByOrderId(orderId);
    }

    public OrderDeliveryDO getOrderDelivery(Integer orderId) {
        return orderDeliveryServiceI.getByOrderId(orderId);
    }

    public RegionRpcDTO getRegionById(Integer regionId) {
        Response<RegionRpcDTO> response = systemRegionServiceRpc.getRegionById(regionId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public void updateOrderDelivery(OrderDeliveryDO orderDeliveryDO) {
        orderDeliveryServiceI.update(orderDeliveryDO);
    }

    public ModelMaterialRelevanceDTORpcQry getModelMaterialRelevanceByModelIdAndMaterialId(
        Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap, Integer modelId, Integer materialId) {
        String key = modelId + "-" + materialId;
        ModelMaterialRelevanceDTORpcQry qry = relevanceDTORpcQryMap.get(key);
        if (qry == null) {
            com.bat.dubboapi.flexible.common.Response<ModelMaterialRelevanceDTORpcQry> response =
                modelMaterialRelevanceServiceRpc.getByModelIdAndMaterialId(modelId, materialId);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            } else {
                ModelMaterialRelevanceDTORpcQry data = response.getData();
                relevanceDTORpcQryMap.put(key, data);
                return data;
            }
        } else {
            return qry;
        }
    }

    public List<LabelDTORpcQry> listLabelByModelIdAndMaterialId(Integer distributorId, Integer categoryId,
        Integer pictureId) {
        com.bat.dubboapi.flexible.common.Response<List<LabelDTORpcQry>> response =
            labelServiceRpc.listDiyLabelByCondition(distributorId, categoryId, pictureId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return orderGoodsDistributorCostServiceI.listByOrderIdAndDistributorId(orderId, distributorId);
    }

    public PictureDTORpcQry getPictureById(Integer pictureId) {
        com.bat.dubboapi.flexible.common.Response<PictureDTORpcQry> response =
            pictureServiceRpc.getDTORpcById(pictureId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public GoodsItemRpcDTO goodsItemByCode(String itemCode) {
        com.bat.dubboapi.goods.common.Response<GoodsItemRpcDTO> response =
            goodsServiceRpc.goodsItemByCode(itemCode);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    /**
     * 重新生成 标签
     * 
     * @param factoryNo
     * @param orderGoodsDiyDOList
     */
    public boolean retryCreateLabel(String factoryNo, List<OrderGoodsDiyDO> orderGoodsDiyDOList) {
        log.info("工厂：{} 开始重新生成标签", factoryNo);
        OrderLableCmd orderLableCmd = new OrderLableCmd();
        List<OrderGoodsDiySimpleDTO> diySimpleDTOList = new ArrayList<>();
        orderGoodsDiyDOList.stream().forEach(orderGoodsDiyDO -> {
            if (StringUtils.isBlank(orderGoodsDiyDO.getLabelUrl())) {
                OrderGoodsDiySimpleDTO simpleDTO = BeanUtils.copy(orderGoodsDiyDO, OrderGoodsDiySimpleDTO.class);
                diySimpleDTOList.add(simpleDTO);
            }
        });
        if (diySimpleDTOList.size() == 0) {
            return false;
        }
        orderLableCmd.setDiySimpleDTOList(diySimpleDTOList);
        Integer orderId = orderGoodsDiyDOList.get(0).getOrderId();
        orderLableCmd.setOrderId(orderId);
        OrderDistributorDataDO orderDistributorData = getOrderDistributorData(orderId);
        orderLableCmd.setDistributorId(orderDistributorData.getDistributorId());
        com.bat.dubboapi.flexible.common.Response<List<OrderDiyLabelDTO>> labelResp =
            labelServiceRpc.createOrderDiyLabel(factoryNo, orderLableCmd);
        log.info("生成标签、返回{}", JSON.toJSONString(labelResp));
        if (labelResp == null) {
            return false;
        }
        if (!labelResp.isSuccess()) {
            log.error(labelResp.getErrCode(), labelResp.getErrMessage());
            return false;
        }
        List<OrderDiyLabelDTO> orderDiyLabelDTOList = labelResp.getData();
        Map<Integer, OrderDiyLabelDTO> labelDTOMap = orderDiyLabelDTOList.stream()
            .collect(Collectors.toMap(OrderDiyLabelDTO::getId, orderDiyLabelDTO -> orderDiyLabelDTO));
        List<OrderGoodsDiyDO> updateList = new ArrayList<>();
        orderGoodsDiyDOList.stream().forEach(orderGoodsDiyDO -> {
            OrderDiyLabelDTO orderDiyLabelDTO = labelDTOMap.get(orderGoodsDiyDO.getId());
            if (orderDiyLabelDTO != null) {
                // 没有生成标签、dubbo接口生成了
                orderGoodsDiyDO.setLabelId(orderDiyLabelDTO.getLabelId());
                orderGoodsDiyDO.setLabelUrl(orderDiyLabelDTO.getLabelUrl());
                orderGoodsDiyDO.setUpdateTime(new Date());
                updateList.add(orderGoodsDiyDO);
            }
        });
        // 批量修改
        orderGoodsDiyServiceI.updateList(updateList);
        return true;
    }

    /**
     * 根据配送方式id获取配送方式信息
     * @param distributionId
     * @return
     */
    public LogisticsRpcDTO getLogisticsByDistributionId(Integer distributionId){
        Response<LogisticsRpcDTO> response = logisticsServiceRpc.getLogisticsById(distributionId);
        if(response.isSuccess()){
            return response.getData();
        }else {
            return null;
        }
    }
}
