package com.bat.order.service.cost.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.Constant;
import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.order.executor.OrderRpcExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeB2BOrderDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@Component
public class OrderGoodsDistributorCostQryExe {
    @Resource
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostDOMapper;
    @Resource
    private OrderRpcExe orderRpcExe;

    public Integer checkIsGroup(Integer orderId) {
        return orderGoodsDistributorCostDOMapper.checkIsGroup(orderId);
    }

    public List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return orderGoodsDistributorCostDOMapper.listByOrderIdAndDistributorId(orderId, distributorId);
    }

    /**
     * 根据C端客户明细费用生成分销层明细费用
     * 
     * @return
     */
    public List<OrderGoodsDistributorCostDO> createOrderGoodsDistributorCostByCustomer(List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, List<OrderGoodsExchangeCodeDO> exchangeCodeDOS,
        DistributorRpcDTO distributor, OrderCustomerCostDO orderCustomerCostDO,
        Map<String, ExchangeCodeB2BOrderDTORpcQry> exchangeCodeMap) {
        List<OrderGoodsDistributorCostDO> distributorCostDOS = new ArrayList<>();
        // 适配兑换卡业务
        if (!CollectionUtils.isEmpty(exchangeCodeDOS)
            && exchangeCodeDOS.get(0).getMailSetting().equals(Constant.MAIL_SETTING_1)) {
            List<OrderGoodsDistributorCostDO> exchangeDistributorCostDOS = new ArrayList<>();
            // 兑换卡类型 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
            List<Integer> orderGoodsIds = exchangeCodeDOS.stream()
                .map(OrderGoodsExchangeCodeDO::getExchangeOrderGoodsId).distinct().collect(Collectors.toList());
            exchangeDistributorCostDOS = orderGoodsDistributorCostDOMapper
                .listByOrderGoodsIdsAndDistributorId(orderGoodsIds, exchangeCodeDOS.get(0).getDistributorId());
            distributorCostDOS =
                OrderCostConvertor.toOrderGoodsDistributorCostDOListByCustomer(orderGoodsCustomerCostDOS, orderGoodsDOS,
                    exchangeCodeDOS, exchangeDistributorCostDOS, distributor, orderCustomerCostDO, exchangeCodeMap);
        } else {
            List<GoodsItemPriceRpcDTO> priceRpcDTOS = orderRpcExe
                .listDistributorGoodsItemPrice(OrderCostConvertor.toGoodsItemPriceRpcQry(orderGoodsDOS, distributor));
            Map<Integer, OrderGoodsDO> orderGoodsDOMap =
                orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
            Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO));
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPrices = orderRpcExe
                .goodsItemPromotionPriceDistributor(OrderCostConvertor.toGoodsItemPromotionPriceRpcQryByCustomer(
                    orderGoodsDOMap, orderGoodsCustomerCostDOS, priceRpcDTOMap, distributor));
            Map<Integer,
                GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceMap = goodsItemPromotionPrices.stream()
                    .collect(Collectors.toMap(GoodsItemPromotionPriceRpcDTO::getSerialNumber,
                        goodsItemPromotionPrice -> goodsItemPromotionPrice));
            distributorCostDOS =
                OrderCostConvertor.toOrderGoodsDistributorCostDOListByCustomer(orderGoodsCustomerCostDOS, orderGoodsDOS,
                    priceRpcDTOMap, goodsItemPromotionPriceMap, distributor, orderCustomerCostDO);
        }
        orderGoodsDistributorCostDOMapper.insertList(distributorCostDOS);
        return distributorCostDOS;
    }

    /**
     * 根据分销层明细费用生成上级分销层明细费用
     *
     * @return
     */
    public List<OrderGoodsDistributorCostDO> createOrderGoodsDistributorCostByDistributor(OrderInfoDO order,
        List<OrderGoodsDO> orderGoodsDOS, List<OrderGoodsExchangeCodeDO> exchangeCodeDOS,
        List<OrderGoodsDistributorCostDO> distributorCosts, DistributorRpcDTO ancestorDistributor,
        OrderDistributorDataDO distributorData, OrderDistributorCostDO distributorCost,
        DistributorExtendDataRpcDTO extendDataRpcDTO) {
        List<OrderGoodsDistributorCostDO> ancestorDistributorCosts = new ArrayList<>();
        // 适配兑换卡业务
        if (distributorData.getErpFlag().equals(Constant.ERP_FLAG_0) && !CollectionUtils.isEmpty(exchangeCodeDOS)) {
            List<OrderGoodsDistributorCostDO> exchangeDistributorCostDOS = new ArrayList<>();
            // 兑换卡类型 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
            if (exchangeCodeDOS.get(0).getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                List<Integer> exchangeOrderGoodsIds = exchangeCodeDOS.stream()
                    .map(OrderGoodsExchangeCodeDO::getExchangeOrderGoodsId).distinct().collect(Collectors.toList());
                exchangeDistributorCostDOS = orderGoodsDistributorCostDOMapper
                    .listByOrderGoodsIdsAndDistributorId(exchangeOrderGoodsIds, ancestorDistributor.getId());
            }
            ancestorDistributorCosts =
                OrderCostConvertor.toOrderGoodsDistributorCostDOListByDistributor(distributorCosts, orderGoodsDOS,
                    exchangeCodeDOS, exchangeDistributorCostDOS, distributorData, distributorCost, extendDataRpcDTO);
        } else {
            List<GoodsItemPriceRpcDTO> priceRpcDTOS = orderRpcExe.listDistributorGoodsItemPrice(
                OrderCostConvertor.toGoodsItemPriceRpcQry(orderGoodsDOS, ancestorDistributor));
            Map<Integer, OrderGoodsDO> orderGoodsDOMap =
                orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
            Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO));
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPrices = orderRpcExe
                .goodsItemPromotionPriceDistributor(OrderCostConvertor.toGoodsItemPromotionPriceRpcQryByDistributor(
                    order, orderGoodsDOMap, distributorCosts, priceRpcDTOMap, ancestorDistributor));
            Map<Integer,
                GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceMap = goodsItemPromotionPrices.stream()
                    .collect(Collectors.toMap(GoodsItemPromotionPriceRpcDTO::getSerialNumber,
                        goodsItemPromotionPrice -> goodsItemPromotionPrice));
            // 生成上级分销层明细费用
            ancestorDistributorCosts =
                OrderCostConvertor.toOrderGoodsDistributorCostDOListByDistributor(distributorCosts, orderGoodsDOS,
                    priceRpcDTOMap, goodsItemPromotionPriceMap, distributorData, distributorCost, extendDataRpcDTO);
            orderGoodsDistributorCostDOMapper.insertList(ancestorDistributorCosts);
        }
        return ancestorDistributorCosts;
    }

    /**
     * 根据分销层明细费用更新上级分销层明细平台收款金额
     *
     * @return
     */
    public void updateOrderGoodsDistributorCostByDistributor(List<OrderGoodsDistributorCostDO> distributorCosts,
        List<OrderGoodsDistributorCostDO> ancestorGoodsDistributorCosts) {
        Map<Integer, OrderGoodsDistributorCostDO> distributorCostMap =
            distributorCosts.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCost -> orderGoodsDistributorCost));
        ancestorGoodsDistributorCosts.forEach(ancestorDistributorCostDO -> {
            OrderGoodsDistributorCostDO distributorCostDO =
                distributorCostMap.get(ancestorDistributorCostDO.getOrderGoodsId());
            ancestorDistributorCostDO.setPlatformPrice(distributorCostDO.getActualPrice());
            ancestorDistributorCostDO.setUpdateTime(new Date());
        });
        orderGoodsDistributorCostDOMapper.updateList(ancestorGoodsDistributorCosts);
    }

    /**
     * 根据订单id和分销商id获取订单明细直属分销商费用
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    public List<OrderGoodsDistributorCostDO> listOrderGoodsDistributorCost(Integer orderId, Integer distributorId) {
        return orderGoodsDistributorCostDOMapper.listByOrderIdAndDistributorId(orderId, distributorId);
    }

    public List<OrderGoodsDistributorCostDO> listOrderGoodsDistributorCostByOrderId(Integer orderId) {
        return orderGoodsDistributorCostDOMapper.listByOrderId(orderId);
    }

    public List<OrderGoodsDistributorCostDO> listOrderGoodsDistributorCostByOrderGoodsId(Integer orderGoodsId) {
        return orderGoodsDistributorCostDOMapper.listByOrderGoodsId(orderGoodsId);
    }
}
