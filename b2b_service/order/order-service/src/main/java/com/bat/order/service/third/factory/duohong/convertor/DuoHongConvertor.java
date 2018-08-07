package com.bat.order.service.third.factory.duohong.convertor;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.bat.order.service.third.factory.validator.FactoryValidatorQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.label.dto.LabelDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.order.order.dto.factory.duohong.DuoHongOrderAddCmd;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.tenant.TenantContext;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: 组装多鸿订单参数
 * @date 2018/05/26 9:58
 */
@Component
@Slf4j
public class DuoHongConvertor {

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private FactoryValidatorQryExe factoryValidatorQryExe;

    @SneakyThrows
    public DuoHongOrderAddCmd toDuoHongOrderAddCmd(Integer orderId, String factoryNo) {
        log.info("组装工厂,{}", factoryNo);
        log.info("组装工厂参数,{}", orderId);
        Response<PlatformTenantDiyFactoryRpcDTO> response =
            platformTenantServiceRpc.diyFactoryConfig(TenantContext.getTenantNo(), factoryNo);
        if (response.isSuccess()) {
            OrderInfoDO orderInfo = factoryValidatorQryExe.getOrderInfo(orderId);
            OrderDistributorDataDO orderDistributorData = factoryValidatorQryExe.getOrderDistributorData(orderId);
            String orderNo = orderInfo.getOrderNo();

            PlatformTenantDiyFactoryRpcDTO data = response.getData();
            String shopName = data.getShopName();

            DuoHongOrderAddCmd req = new DuoHongOrderAddCmd();
            DuoHongOrderAddCmd.Trade trade = getShippingAddressInfo(orderInfo, shopName, factoryNo);

            List<DuoHongOrderAddCmd.Trade.Order> orders = new ArrayList<>();

            List<OrderGoodsDiyDO> orderGoodsDiyDOList =
                factoryValidatorQryExe.listOrderGoodsDiy(orderInfo.getId(), factoryNo);
            Map<Integer, OrderGoodsDO> orderGoodsDOMap = factoryValidatorQryExe.getOrderGoodsMap(orderInfo.getId());

            // 材质和型号关联关系集合 、key 为model_id+"_"+material_id
            Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap = new HashMap<>();
            for (int i = 0; i < orderGoodsDiyDOList.size(); i++) {
                OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOList.get(i);
                ModelMaterialRelevanceDTORpcQry qry =
                    factoryValidatorQryExe.getModelMaterialRelevanceByModelIdAndMaterialId(relevanceDTORpcQryMap,
                        orderGoodsDiyDO.getModelId(), orderGoodsDiyDO.getMaterialId());
                Integer orderGoodsId = orderGoodsDiyDO.getOrderGoodsId();
                OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsId);
                DuoHongOrderAddCmd.Trade.Order order = new DuoHongOrderAddCmd.Trade.Order();
                order.setTid(orderNo);
                String oid = orderNo + (i + 1);
                order.setOid(oid);
                order.setOuterSkuId(qry.getThirdSku() + "+" + oid);
                order.setNum(orderGoodsDO.getItemCount() + "");
                order.setPicPath(orderGoodsDiyDO.getPreviewImage());
                order.setUploadPicPath(orderGoodsDiyDO.getGenerateImage());
                orders.add(order);
                // 添加标签赠品 标签为空 不推送
                LabelDTORpcQry labelDTORpc = factoryValidatorQryExe.getLabelByModelIdAndMaterialId(orderDistributorData,
                    orderGoodsDiyDO, factoryNo);
                if (labelDTORpc != null) {
                    DuoHongOrderAddCmd.Trade.Order labelGiftOrder = new DuoHongOrderAddCmd.Trade.Order();
                    labelGiftOrder.setTid(orderNo);
                    String labelGiftOid = oid + 1;
                    labelGiftOrder.setOid(labelGiftOid);
                    labelGiftOrder.setOuterSkuId(labelDTORpc.getThirdSkuNo());
                    labelGiftOrder.setNum(orderGoodsDO.getItemCount() + "");
                    orders.add(labelGiftOrder);
                }
            }

            trade.setOrders(orders);
            List<DuoHongOrderAddCmd.Trade> trades = Collections.singletonList(trade);
            req.setTrades(trades);
            log.info("组装完的参数 json:{}", JSON.toJSONString(req));
            return req;
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    private DuoHongOrderAddCmd.Trade getShippingAddressInfo(OrderInfoDO orderInfo, String shopName,
        String factoryCode) {
        if (StringUtils.isNoneBlank(shopName)) {
            OrderDeliveryDO orderDeliveryDO = factoryValidatorQryExe.getOrderDelivery(orderInfo.getId(), factoryCode);
            DuoHongOrderAddCmd.Trade trade = new DuoHongOrderAddCmd.Trade();
            trade.setTid(orderInfo.getOrderNo());
            trade.setShopName(shopName);
            trade.setBuyerNick(orderDeliveryDO.getUserName());
            trade.setReceiverName(orderDeliveryDO.getUserName());
            trade.setReceiverState(orderDeliveryDO.getProvinceName());
            trade.setReceiverCity(orderDeliveryDO.getCityName());
            trade.setReceiverDistrict(orderDeliveryDO.getDistrictName());
            trade.setReceiverAddress(orderDeliveryDO.getAddress());
            trade.setReceiverMobile(orderDeliveryDO.getMobile());
            trade.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDeliveryDO.getCreateTime()));
            return trade;
        } else {
            throw OrderException.buildException("多鸿工厂同步，店铺名称不能为空");
        }
    }
}
