package com.bat.order.service.cost.convertor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.order.dao.cost.dataobject.*;
import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.constant.FlexibleRpcConstant;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeB2BOrderDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.cost.dto.data.OrderInvoiceDTO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.mq.dto.GoodsSaleDTO;
import com.bat.order.mq.dto.OrderPayDTO;
import com.bat.order.mq.dto.OrderRefundDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/28 12:12
 */
@Slf4j
public class OrderCostConvertor {
    public static OrderInvoiceDTO toOrderInvoiceDTO(OrderInvoiceDO orderInvoiceDO) {
        if (orderInvoiceDO != null) {
            OrderInvoiceDTO dto = new OrderInvoiceDTO();
            BeanUtils.copyProperties(orderInvoiceDO, dto);
            return dto;
        }
        return null;
    }

    public static void orderPayCustomer(List<OrderCustomerCostDO> customerCostDOS, OrderPayDTO cmd) {
        BigDecimal paidAmount = cmd.getPaidAmount();
        customerCostDOS.forEach(customerCostDO -> {
            customerCostDO.setUpdateTime(cmd.getPadTime());
            customerCostDO.setOutTradeNo(cmd.getOutTradeNo());
            BigDecimal payAmount = customerCostDO.getPayAmount();
            if (customerCostDO.getPaidAmount() != null) {
                payAmount = payAmount.subtract(customerCostDO.getPaidAmount());
            }
            if (cmd.getPaidAmount() != null && payAmount.compareTo(cmd.getPaidAmount()) > 0) {
                customerCostDO.setPaidAmount(cmd.getPaidAmount());
                cmd.setPaidAmount(BigDecimal.ZERO);
            } else {
                customerCostDO.setPaidAmount(payAmount);
                cmd.setPaidAmount(cmd.getPaidAmount().subtract(payAmount));
            }
        });
        // 有外部 反写回来的优惠金额 需要扣除(全场券)
        if (cmd.getAlipayVoucherBizFlag()) {
            customerCostDOS.forEach(orderCustomerCostDO -> {
                // 应付款金额 除以 总付款金额 * diffAmount
                BigDecimal diffAmountItem =
                    orderCustomerCostDO.getPaidAmount().divide(paidAmount, 16, RoundingMode.HALF_UP)
                        .multiply(cmd.getDiffAmount()).setScale(4, RoundingMode.HALF_UP);
                BigDecimal orderPromotionAmount = orderCustomerCostDO.getOrderPromotionAmount();
                if (orderPromotionAmount == null) {
                    orderPromotionAmount = BigDecimal.ZERO;
                }
                BigDecimal orderPromotionAmountNew = orderPromotionAmount.add(diffAmountItem);
                orderCustomerCostDO.setOrderPromotionAmount(orderPromotionAmountNew);
                orderCustomerCostDO.setPayAmount(orderCustomerCostDO.getPayAmount().subtract(diffAmountItem));
                orderCustomerCostDO.setPaidAmount(orderCustomerCostDO.getPaidAmount().subtract(diffAmountItem));
                orderCustomerCostDO.setAlipayVoucherAmount(diffAmountItem);
            });
        }
    }

    public static void orderPayDistributor(List<OrderDistributorCostDO> distributorCostDOS, OrderPayDTO cmd,
                                           OrderDistributorDataDO orderDistributorDataDO, DistributorExtendDataRpcDTO extendDataRpcDTO) {
        distributorCostDOS.forEach(distributorCostDO -> {
            // 如果付款方式是平台收款，则需要设置平台收款金额
            if (ObjectUtils.isNotEmpty(orderDistributorDataDO) && orderDistributorDataDO.getTreeNode() > 1
                && orderDistributorDataDO.getDistributionMode() != null
                && (orderDistributorDataDO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
                    || (orderDistributorDataDO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                        && extendDataRpcDTO.getDistributionMode() != null
                        && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))) {
                distributorCostDO.setPlatformAmount(distributorCostDO.getPayAmount());
            }
            distributorCostDO.setUpdateTime(cmd.getPadTime());
            distributorCostDO.setOutTradeNo(cmd.getOutTradeNo());
            // 注意余额支付（支持部分支付）
            BigDecimal payAmount = distributorCostDO.getPayAmount();
            if (distributorCostDO.getDepositAmount() != null) {
                payAmount = payAmount.subtract(distributorCostDO.getDepositAmount());
            }
            if (distributorCostDO.getPaidAmount() != null) {
                payAmount = payAmount.subtract(distributorCostDO.getPaidAmount());
            }
            if (cmd.getDepositAmount() != null && payAmount.compareTo(cmd.getDepositAmount()) == 1) {
                distributorCostDO.setDepositAmount(cmd.getDepositAmount());
                cmd.setDepositAmount(new BigDecimal(0));
                payAmount = payAmount.subtract(cmd.getDepositAmount());
                if (cmd.getPaidAmount() != null && payAmount.compareTo(cmd.getPaidAmount()) == 1) {
                    distributorCostDO.setPaidAmount(cmd.getPaidAmount());
                    cmd.setPaidAmount(new BigDecimal(0));
                } else {
                    distributorCostDO.setPaidAmount(payAmount);
                    cmd.setPaidAmount(cmd.getPaidAmount().subtract(payAmount));
                }
            } else {
                distributorCostDO.setDepositAmount(payAmount);
                cmd.setDepositAmount(cmd.getDepositAmount().subtract(payAmount));
            }
        });
    }

    public static GoodsItemPriceRpcQry toGoodsItemPriceRpcQry(List<OrderGoodsDO> orderGoodsDOS,
        DistributorRpcDTO distributor) {
        GoodsItemPriceRpcQry rpcQry = new GoodsItemPriceRpcQry();
        rpcQry.setDistributorId(distributor.getId());
        rpcQry.setRetailPriceFlag(Constant.RETAIL_PRICE_FLAG_0);
        List<Integer> itemIds = new ArrayList<>();
        List<GoodsItemRpc> goodsItems = new ArrayList<>();
        rpcQry.setGoodsItems(goodsItems);
        orderGoodsDOS.forEach(orderGoodsDO -> {
            if (!itemIds.contains(orderGoodsDO.getItemId())) {
                GoodsItemRpc rpc = new GoodsItemRpc();
                rpc.setGoodsId(orderGoodsDO.getGoodsId());
                rpc.setItemId(orderGoodsDO.getItemId());
                goodsItems.add(rpc);
                itemIds.add(orderGoodsDO.getItemId());
            }
        });
        return rpcQry;
    }

    public static GoodsItemPromotionPriceRpcQry toGoodsItemPromotionPriceRpcQryByCustomer(
        Map<Integer, OrderGoodsDO> orderGoodsDOMap, List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, DistributorRpcDTO distributor) {
        GoodsItemPromotionPriceRpcQry rpcQry = new GoodsItemPromotionPriceRpcQry();
        rpcQry.setDistributorId(distributor.getId());
        List<com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry> goodsItemPrices = new ArrayList<>();
        rpcQry.setGoodsItemPrices(goodsItemPrices);
        orderGoodsCustomerCostDOS.forEach(orderGoodsCustomerCostDO -> {
            if (orderGoodsCustomerCostDO.getSpellGroupId() != null
                || orderGoodsCustomerCostDO.getOrderPromotionId() != null
                || orderGoodsCustomerCostDO.getGoodsPromotionId() != null) {
                com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry goodsItemPriceRpcQry =
                    new com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry();
                goodsItemPrices.add(goodsItemPriceRpcQry);
                BeanUtils.copyProperties(orderGoodsCustomerCostDO, goodsItemPriceRpcQry);
                OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(orderGoodsDO.getItemId());
                BeanUtils.copyProperties(orderGoodsDO, goodsItemPriceRpcQry);
                goodsItemPriceRpcQry.setSalePrice(priceRpcDTO.getSalePrice());
                goodsItemPriceRpcQry.setItemType(orderGoodsCustomerCostDO.getItemType());
                rpcQry.setOrderTime(orderGoodsCustomerCostDO.getCreateTime());
            }
        });
        return rpcQry;
    }

    public static GoodsItemPromotionPriceRpcQry toGoodsItemPromotionPriceRpcQryByDistributor(OrderInfoDO order,
        Map<Integer, OrderGoodsDO> orderGoodsDOMap, List<OrderGoodsDistributorCostDO> goodsDistributorCosts,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, DistributorRpcDTO distributor) {
        GoodsItemPromotionPriceRpcQry rpcQry = new GoodsItemPromotionPriceRpcQry();
        rpcQry.setDistributorId(distributor.getId());
        rpcQry.setOrderTime(order.getCreateTime());
        List<com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry> goodsItemPrices = new ArrayList<>();
        rpcQry.setGoodsItemPrices(goodsItemPrices);
        goodsDistributorCosts.forEach(goodsDistributorCost -> {
            if (goodsDistributorCost.getSpellGroupId() != null || goodsDistributorCost.getOrderPromotionId() != null
                || goodsDistributorCost.getGoodsPromotionId() != null) {
                com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry goodsItemPriceRpcQry =
                    new com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry();
                goodsItemPrices.add(goodsItemPriceRpcQry);
                BeanUtils.copyProperties(goodsDistributorCost, goodsItemPriceRpcQry);
                OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(goodsDistributorCost.getOrderGoodsId());
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(orderGoodsDO.getItemId());
                BeanUtils.copyProperties(orderGoodsDO, goodsItemPriceRpcQry);
                goodsItemPriceRpcQry.setSalePrice(priceRpcDTO.getSalePrice());
                goodsItemPriceRpcQry.setItemType(goodsDistributorCost.getItemType());
            }
        });
        return rpcQry;
    }

    public static List<OrderGoodsDistributorCostDO> toOrderGoodsDistributorCostDOListByCustomer(
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, List<OrderGoodsDO> orderGoodsDOS,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap,
        Map<Integer, GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceMap, DistributorRpcDTO distributor,
        OrderCustomerCostDO orderCustomerCostDO) {
        List<OrderGoodsDistributorCostDO> distributorCostDOS = new ArrayList<>();
        Map<Integer, OrderGoodsCustomerCostDO> customerCostDOMap = orderGoodsCustomerCostDOS.stream().collect(Collectors
            .toMap(OrderGoodsCustomerCostDO::getOrderGoodsId, orderGoodsCustomerCostDO -> orderGoodsCustomerCostDO));
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoodsDO -> {
            OrderGoodsDistributorCostDO distributorCostDO = new OrderGoodsDistributorCostDO();
            distributorCostDOS.add(distributorCostDO);
            distributorCostDO.setOrderId(orderGoodsDO.getOrderId());
            distributorCostDO.setOrderGoodsId(orderGoodsDO.getId());
            distributorCostDO.setDistributorId(distributor.getId());
            distributorCostDO.setCreateTime(date);
            distributorCostDO.setUpdateTime(date);
            GoodsItemPromotionPriceRpcDTO rpcDTO = goodsItemPromotionPriceMap.get(orderGoodsDO.getSerialNumber());
            if (rpcDTO != null) {
                distributorCostDO.setSalePrice(rpcDTO.getSalePrice());
                BeanUtils.copyProperties(rpcDTO, distributorCostDO);
                distributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            } else {
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(orderGoodsDO.getItemId());
                distributorCostDO.setSalePrice(priceRpcDTO.getSalePrice());
                distributorCostDO.setActualPrice(priceRpcDTO.getSalePrice());
                distributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            }
            if ((distributor.getCustomerMode() == null || distributor.getCustomerMode().equals(Constant.CUSTOMER_MODE_1))
                && StringUtils.isNotBlank(orderCustomerCostDO.getOutTradeNo())) {
                OrderGoodsCustomerCostDO customerCostDO = customerCostDOMap.get(orderGoodsDO.getId());
                distributorCostDO.setPlatformPrice(customerCostDO.getActualPrice());
            }
        });
        return distributorCostDOS;
    }

    public static List<OrderGoodsDistributorCostDO> toOrderGoodsDistributorCostDOListByCustomer(
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS, List<OrderGoodsDistributorCostDO> exchangeDistributorCostDOS,
        DistributorRpcDTO distributor, OrderCustomerCostDO orderCustomerCostDO,
        Map<String, ExchangeCodeB2BOrderDTORpcQry> exchangeCodeMap) {
        List<OrderGoodsDistributorCostDO> distributorCostDOS = new ArrayList<>();
        Map<Integer, OrderGoodsCustomerCostDO> customerCostDOMap = orderGoodsCustomerCostDOS.stream().collect(Collectors
            .toMap(OrderGoodsCustomerCostDO::getOrderGoodsId, orderGoodsCustomerCostDO -> orderGoodsCustomerCostDO));
        Map<Integer, List<OrderGoodsExchangeCodeDO>> exchangeCodeDOSMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(exchangeCodeDOS)) {
            exchangeCodeDOSMap.putAll(
                exchangeCodeDOS.stream().collect(Collectors.groupingBy(OrderGoodsExchangeCodeDO::getOrderGoodsId)));
        }
        Map<Integer, OrderGoodsDistributorCostDO> exchangeDistributorCostDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(exchangeDistributorCostDOS)) {
            exchangeDistributorCostDOMap.putAll(exchangeDistributorCostDOS.stream().collect(Collectors.toMap(
                OrderGoodsDistributorCostDO::getOrderGoodsId, exchangeDistributorCostDO -> exchangeDistributorCostDO)));
        }
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoodsDO -> {
            OrderGoodsDistributorCostDO distributorCostDO = new OrderGoodsDistributorCostDO();
            distributorCostDOS.add(distributorCostDO);
            distributorCostDO.setOrderId(orderGoodsDO.getOrderId());
            distributorCostDO.setOrderGoodsId(orderGoodsDO.getId());
            distributorCostDO.setDistributorId(distributor.getId());
            distributorCostDO.setCreateTime(date);
            distributorCostDO.setUpdateTime(date);
            List<OrderGoodsExchangeCodeDO> orderGoodsExchangeCodeDOS = exchangeCodeDOSMap.get(orderGoodsDO.getId());
            if (CollectionUtils.isEmpty(orderGoodsExchangeCodeDOS)) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_EXCHANGER_GOODS_ID_ERROR,
                    MessageUtils.get(CommonErrorCode.B_ORDER_EXCHANGER_GOODS_ID_ERROR));
            }
            OrderGoodsExchangeCodeDO exchangeCodeDO = orderGoodsExchangeCodeDOS.get(0);
            OrderGoodsCustomerCostDO customerCostDO = customerCostDOMap.get(orderGoodsDO.getId());
            // 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
            distributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                if (exchangeCodeDO.getExchangeOrderGoodsId() != null) {
                    OrderGoodsDistributorCostDO exchangeDistributorCostDO =
                        exchangeDistributorCostDOMap.get(exchangeCodeDO.getExchangeOrderGoodsId());
                    distributorCostDO.setSalePrice(exchangeDistributorCostDO.getActualPrice());
                    distributorCostDO.setActualPrice(exchangeDistributorCostDO.getActualPrice());
                } else {
                    distributorCostDO.setSalePrice(BigDecimal.ZERO);
                    distributorCostDO.setActualPrice(BigDecimal.ZERO);
                    ExchangeCodeB2BOrderDTORpcQry exchangeCodeB2BOrder =
                        exchangeCodeMap.get(exchangeCodeDO.getSecretCode());
                    if (exchangeCodeB2BOrder != null && exchangeCodeB2BOrder.getExchangeWay() != null
                        && exchangeCodeB2BOrder.getExchangeWay() == FlexibleRpcConstant.EXCHANGE_WAY_2
                        && exchangeCodeB2BOrder.getDistributorQuanyiPrice() != null) {
                        distributorCostDO.setSalePrice(exchangeCodeB2BOrder.getDistributorQuanyiPrice());
                        distributorCostDO.setActualPrice(exchangeCodeB2BOrder.getDistributorQuanyiPrice());
                    }
                }
            } else if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_2)) {
                distributorCostDO.setItemType(customerCostDO.getItemType());
                distributorCostDO.setSalePrice(customerCostDO.getActualPrice());
                distributorCostDO.setActualPrice(customerCostDO.getActualPrice());
            }
            if ((distributor.getCustomerMode() == null || distributor.getCustomerMode().equals(Constant.CUSTOMER_MODE_1))
                && StringUtils.isNotBlank(orderCustomerCostDO.getOutTradeNo())) {
                distributorCostDO.setPlatformPrice(customerCostDO.getActualPrice());
            }
        });
        return distributorCostDOS;
    }

    public static List<OrderGoodsDistributorCostDO> toOrderGoodsDistributorCostDOListByDistributor(
        List<OrderGoodsDistributorCostDO> distributorCosts, List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS, List<OrderGoodsDistributorCostDO> exchangeDistributorCostDOS,
        OrderDistributorDataDO distributorData, OrderDistributorCostDO distributorCost,
        DistributorExtendDataRpcDTO extendDataRpcDTO) {
        List<OrderGoodsDistributorCostDO> ancestorDistributorCostDOS = new ArrayList<>();
        Map<Integer, OrderGoodsDistributorCostDO> distributorCostMap =
            distributorCosts.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCost -> orderGoodsDistributorCost));
        Map<Integer, List<OrderGoodsExchangeCodeDO>> exchangeCodeDOSMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(exchangeCodeDOS)) {
            exchangeCodeDOSMap.putAll(
                exchangeCodeDOS.stream().collect(Collectors.groupingBy(OrderGoodsExchangeCodeDO::getOrderGoodsId)));
        }
        Map<Integer, OrderGoodsDistributorCostDO> exchangeDistributorCostDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(exchangeDistributorCostDOS)) {
            exchangeDistributorCostDOMap.putAll(exchangeDistributorCostDOS.stream().collect(Collectors.toMap(
                OrderGoodsDistributorCostDO::getOrderGoodsId, exchangeDistributorCostDO -> exchangeDistributorCostDO)));
        }
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoodsDO -> {
            OrderGoodsDistributorCostDO ancestorDistributorCostDO = new OrderGoodsDistributorCostDO();
            ancestorDistributorCostDOS.add(ancestorDistributorCostDO);
            ancestorDistributorCostDO.setOrderId(orderGoodsDO.getOrderId());
            ancestorDistributorCostDO.setOrderGoodsId(orderGoodsDO.getId());
            ancestorDistributorCostDO.setDistributorId(distributorData.getDistributorAncestorId());
            ancestorDistributorCostDO.setCreateTime(date);
            ancestorDistributorCostDO.setUpdateTime(date);
            List<OrderGoodsExchangeCodeDO> orderGoodsExchangeCodeDOS = exchangeCodeDOSMap.get(orderGoodsDO.getId());
            OrderGoodsExchangeCodeDO exchangeCodeDO = orderGoodsExchangeCodeDOS.get(0);
            // 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
            if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                OrderGoodsDistributorCostDO exchangeDistributorCostDO =
                    exchangeDistributorCostDOMap.get(exchangeCodeDO.getExchangeOrderGoodsId());
                ancestorDistributorCostDO.setSalePrice(exchangeDistributorCostDO.getActualPrice());
                ancestorDistributorCostDO.setActualPrice(exchangeDistributorCostDO.getActualPrice());
                ancestorDistributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            } else if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_2)) {
                OrderGoodsDistributorCostDO distributorCostDO = distributorCostMap.get(orderGoodsDO.getId());
                ancestorDistributorCostDO.setSalePrice(distributorCostDO.getActualPrice());
                ancestorDistributorCostDO.setActualPrice(distributorCostDO.getActualPrice());
                ancestorDistributorCostDO.setItemType(distributorCostDO.getItemType());
            }
            // 平台收款情况,且收款凭证号不为空情况(如果是上级收款，需找到非上级收款方式)
            if ((distributorData.getDistributionMode() == null
                || distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
                || (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                    && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))
                && StringUtils.isNotBlank(distributorCost.getOutTradeNo())) {
                OrderGoodsDistributorCostDO distributorCostDO = distributorCostMap.get(orderGoodsDO.getId());
                ancestorDistributorCostDO.setPlatformPrice(distributorCostDO.getActualPrice());
            }
        });
        return ancestorDistributorCostDOS;
    }

    public static List<OrderGoodsDistributorCostDO> toOrderGoodsDistributorCostDOListByDistributor(
        List<OrderGoodsDistributorCostDO> distributorCosts, List<OrderGoodsDO> orderGoodsDOS,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap,
        Map<Integer, GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceMap, OrderDistributorDataDO distributorData,
        OrderDistributorCostDO distributorCost, DistributorExtendDataRpcDTO extendDataRpcDTO) {
        List<OrderGoodsDistributorCostDO> ancestorDistributorCostDOS = new ArrayList<>();
        Map<Integer, OrderGoodsDistributorCostDO> distributorCostMap =
            distributorCosts.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCost -> orderGoodsDistributorCost));
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoodsDO -> {
            OrderGoodsDistributorCostDO ancestorDistributorCostDO = new OrderGoodsDistributorCostDO();
            ancestorDistributorCostDOS.add(ancestorDistributorCostDO);
            ancestorDistributorCostDO.setOrderId(orderGoodsDO.getOrderId());
            ancestorDistributorCostDO.setOrderGoodsId(orderGoodsDO.getId());
            ancestorDistributorCostDO.setDistributorId(distributorData.getDistributorAncestorId());
            ancestorDistributorCostDO.setCreateTime(date);
            ancestorDistributorCostDO.setUpdateTime(date);
            GoodsItemPromotionPriceRpcDTO rpcDTO = goodsItemPromotionPriceMap.get(orderGoodsDO.getSerialNumber());
            if (rpcDTO != null) {
                BeanUtils.copyProperties(rpcDTO, ancestorDistributorCostDO);
                ancestorDistributorCostDO.setSalePrice(rpcDTO.getSalePrice());
                if (rpcDTO.getActualPrice() == null) {
                    ancestorDistributorCostDO.setActualPrice(rpcDTO.getSalePrice());
                }
                ancestorDistributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            } else {
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(orderGoodsDO.getItemId());
                ancestorDistributorCostDO.setSalePrice(priceRpcDTO.getSalePrice());
                ancestorDistributorCostDO.setActualPrice(priceRpcDTO.getSalePrice());
                ancestorDistributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            }
            // 平台收款情况,且收款凭证号不为空情况(如果是上级收款，需找到非上级收款方式)
            if ((distributorData.getDistributionMode() == null
                || distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
                || (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                    && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))
                && StringUtils.isNotBlank(distributorCost.getOutTradeNo())) {
                OrderGoodsDistributorCostDO distributorCostDO = distributorCostMap.get(orderGoodsDO.getId());
                ancestorDistributorCostDO.setPlatformPrice(distributorCostDO.getActualPrice());
            }
        });
        return ancestorDistributorCostDOS;
    }

    public static OrderDistributorCostDO toOrderDistributorCostDO(OrderDistributorCostDO distributorCost,
        OrderDistributorDataDO distributorData, List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCosts, DistributorExtendDataRpcDTO extendDataRpcDTO) {
        Map<Integer, OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOMap =
            orderGoodsDistributorCosts.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCost -> orderGoodsDistributorCost));
        OrderDistributorCostDO ancestorDistributorCost = new OrderDistributorCostDO();
        ancestorDistributorCost.setOrderId(distributorCost.getOrderId());
        ancestorDistributorCost.setDistributorId(distributorData.getDistributorAncestorId());
        // 商品总价
        AtomicReference<BigDecimal> goodsAmount = new AtomicReference<>(new BigDecimal(0));
        // 商品促销金额
        AtomicReference<BigDecimal> goodsPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        // 订单促销金额
        AtomicReference<BigDecimal> orderPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoods -> {
            OrderGoodsDistributorCostDO orderGoodsCost = orderGoodsDistributorCostDOMap.get(orderGoods.getId());
            BigDecimal goodsSaleAmount =
                orderGoodsCost.getSalePrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            goodsAmount.set(goodsAmount.get().add(goodsSaleAmount));
            BigDecimal goodsActualAmount =
                orderGoodsCost.getActualPrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            if (orderGoodsCost.getGoodsPromotionId() != null || orderGoodsCost.getSpellGroupId() != null) {
                goodsPromotionAmount.set(goodsPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount)));
            } else if (orderGoodsCost.getOrderPromotionId() != null) {
                orderPromotionAmount.set(orderPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount)));
            }
        });
        ancestorDistributorCost.setGoodsAmount(goodsAmount.get());
        ancestorDistributorCost.setGoodsPromotionAmount(goodsPromotionAmount.get());
        ancestorDistributorCost.setOrderPromotionAmount(orderPromotionAmount.get());
        ancestorDistributorCost.setDistributionAmount(distributorCost.getDistributionAmount());
        // 平台收款情况,且收款凭证号不为空情况(如果是上级收款，需找到非上级收款方式)
        if ((distributorData.getDistributionMode() == null
            || distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
            || (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))
            && StringUtils.isNotBlank(distributorCost.getOutTradeNo())) {
            ancestorDistributorCost.setPlatformAmount(distributorCost.getPayAmount());
            ancestorDistributorCost.setOutTradeNo(distributorCost.getOutTradeNo());
        }
        // 计算应付款金额
        BigDecimal actualAmount =
            ancestorDistributorCost.getGoodsAmount().subtract(ancestorDistributorCost.getGoodsPromotionAmount())
                .subtract(ancestorDistributorCost.getOrderPromotionAmount());
        if (actualAmount.doubleValue() > 0) {
            ancestorDistributorCost.setPayAmount(actualAmount.add(ancestorDistributorCost.getDistributionAmount()));
        } else {
            ancestorDistributorCost.setPayAmount(ancestorDistributorCost.getDistributionAmount());
        }
        ancestorDistributorCost.setCreateTime(date);
        ancestorDistributorCost.setUpdateTime(date);
        return ancestorDistributorCost;
    }

    public static OrderDistributorCostDO toOrderDistributorCostDO(OrderCustomerCostDO orderCustomerCost,
        DistributorRpcDTO distributor, List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCosts) {
        Map<Integer, OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOMap =
            orderGoodsDistributorCosts.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                orderGoodsDistributorCost -> orderGoodsDistributorCost));
        OrderDistributorCostDO distributorCostDO = new OrderDistributorCostDO();
        distributorCostDO.setOrderId(orderCustomerCost.getOrderId());
        distributorCostDO.setDistributorId(distributor.getId());
        // 商品总价
        AtomicReference<BigDecimal> goodsAmount = new AtomicReference<>(new BigDecimal(0));
        // 商品促销金额
        AtomicReference<BigDecimal> goodsPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        // 订单促销金额
        AtomicReference<BigDecimal> orderPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        Date date = new Date(System.currentTimeMillis());
        orderGoodsDOS.forEach(orderGoods -> {
            OrderGoodsDistributorCostDO orderGoodsCost = orderGoodsDistributorCostDOMap.get(orderGoods.getId());
            BigDecimal goodsSaleAmount =
                orderGoodsCost.getSalePrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            goodsAmount.set(goodsAmount.get().add(goodsSaleAmount));
            BigDecimal goodsActualAmount =
                orderGoodsCost.getActualPrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            if (orderGoodsCost.getGoodsPromotionId() != null || orderGoodsCost.getSpellGroupId() != null) {
                goodsPromotionAmount.set(goodsPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount)));
            } else if (orderGoodsCost.getOrderPromotionId() != null) {
                orderPromotionAmount.set(orderPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount)));
            }
        });
        distributorCostDO.setGoodsAmount(goodsAmount.get());
        distributorCostDO.setGoodsPromotionAmount(goodsPromotionAmount.get());
        distributorCostDO.setOrderPromotionAmount(orderPromotionAmount.get());
        if ((distributor.getCustomerMode() == null || distributor.getCustomerMode().equals(Constant.CUSTOMER_MODE_1))
            && StringUtils.isNotBlank(orderCustomerCost.getOutTradeNo())) {
            distributorCostDO.setPlatformAmount(orderCustomerCost.getPayAmount());
            distributorCostDO.setOutTradeNo(orderCustomerCost.getOutTradeNo());
        }
        distributorCostDO.setCreateTime(date);
        distributorCostDO.setUpdateTime(date);
        return distributorCostDO;
    }

    public static OrderDistributorCommissionDO toOrderDistributorCommissionDO(DistributorRpcDTO distributor,
                                                                              OrderCustomerCostDO orderCustomerCost, OrderDistributorCostDO distributorCost) {
        OrderDistributorCommissionDO commissionDO = new OrderDistributorCommissionDO();
        commissionDO.setOrderId(distributorCost.getOrderId());
        BigDecimal amount = orderCustomerCost.getPayAmount().subtract(distributorCost.getPayAmount());
        if (amount.doubleValue() == 0) {
            return null;
        } else {
            if (amount.doubleValue() < 0) {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_2);
            } else {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_1);
            }
        }
        commissionDO.setAmount(amount.abs());
        commissionDO.setDistributorAncestorId(distributor.getId());
        commissionDO.setOrderDescendantPrice(orderCustomerCost.getPayAmount());
        commissionDO.setOrderAncestorPrice(distributorCost.getPayAmount());
        Date date = new Date(System.currentTimeMillis());
        commissionDO.setCreateTime(date);
        commissionDO.setUpdateTime(date);
        return commissionDO;
    }

    public static List<OrderGoodsDistributorCostDO> toAddOrderGoodsDistributorCostDOList(
        List<ErpOrderChangeDetailCmd> orderDetails, List<OrderGoodsDO> addOrderGoodsDOS,
        List<GoodsItemPriceRpcDTO> priceRpcDTOS, OrderDistributorDataDO erpDistributorData, Date time) {
        List<OrderGoodsDistributorCostDO> distributorCostDOS = new ArrayList<>();
        Map<Integer, ErpOrderChangeDetailCmd> orderChangeDetailCmdMap = orderDetails.stream()
            .collect(Collectors.toMap(ErpOrderChangeDetailCmd::getItemOrderId, orderDetail -> orderDetail));
        Map<Integer, GoodsItemPriceRpcDTO> goodsItemPriceRpcDTOMap = priceRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO));
        addOrderGoodsDOS.forEach(addOrderGoodsDO -> {
            ErpOrderChangeDetailCmd orderChangeDetail = orderChangeDetailCmdMap.get(addOrderGoodsDO.getSerialNumber());
            GoodsItemPriceRpcDTO priceRpcDTO = goodsItemPriceRpcDTOMap.get(addOrderGoodsDO.getItemId());
            OrderGoodsDistributorCostDO distributorCostDO = new OrderGoodsDistributorCostDO();
            distributorCostDO.setOrderGoodsDO(addOrderGoodsDO);
            distributorCostDO.setDistributorId(erpDistributorData.getDistributorId());
            distributorCostDO.setOrderId(addOrderGoodsDO.getOrderId());
            distributorCostDO.setOrderGoodsId(addOrderGoodsDO.getId());
            distributorCostDO.setItemType(addOrderGoodsDO.getItemType());
            if (priceRpcDTO != null) {
                distributorCostDO.setSalePrice(priceRpcDTO.getSalePrice());
            } else {
                distributorCostDO.setSalePrice(orderChangeDetail.getItemTaxPrice());
            }
            if (orderChangeDetail.getIsFree()) {
                distributorCostDO.setActualPrice(new BigDecimal(0));
                distributorCostDO.setItemType(Constant.ITEM_TYPE_2);
            } else {
                distributorCostDO.setActualPrice(orderChangeDetail.getItemTaxPrice());
                distributorCostDO.setItemType(Constant.ITEM_TYPE_1);
            }
            if (distributorCostDO.getActualPrice().doubleValue() > distributorCostDO.getSalePrice().doubleValue()) {
                distributorCostDO.setSalePrice(distributorCostDO.getActualPrice());
            }
            distributorCostDO.setCreateTime(time);
            distributorCostDO.setUpdateTime(time);
            distributorCostDOS.add(distributorCostDO);
        });
        return distributorCostDOS;
    }

    public static List<OrderGoodsDistributorCostDO> toChangeOrderGoodsDistributorCostDOList(
        Map<Integer, OrderGoodsDO> orderGoodsDOMap, List<ErpOrderChangeDetailCmd> changeOrderGoodss,
        List<OrderGoodsDistributorCostDO> distributorCostDOS, Map<String, List<Object>> changeMap) {
        List<OrderGoodsDistributorCostDO> changeDistributorCostDOS = new ArrayList<>();
        Map<Integer, OrderGoodsDistributorCostDO> distributorCostDOMap = distributorCostDOS.stream().collect(
            Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId, distributorCostDO -> distributorCostDO));
        changeOrderGoodss.forEach(changeOrderGoods -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(changeOrderGoods.getItemOrderId());
            OrderGoodsDistributorCostDO distributorCostDO = distributorCostDOMap.get(orderGoodsDO.getId());
            distributorCostDO.setOrderGoodsDO(orderGoodsDO);
            if (distributorCostDO.getActualPrice().doubleValue()
                - changeOrderGoods.getItemTaxPrice().doubleValue() != 0) {
                List<Object> changes = new ArrayList<>();
                changes.add(distributorCostDO.getActualPrice().doubleValue());
                changes.add(changeOrderGoods.getItemTaxPrice().doubleValue());
                changeMap.put(orderGoodsDO.getItemCode() + "修改价格", changes);
                distributorCostDO.setActualPrice(changeOrderGoods.getItemTaxPrice());
                if (distributorCostDO.getActualPrice().compareTo(distributorCostDO.getSalePrice()) > 0) {
                    distributorCostDO.setSalePrice(distributorCostDO.getActualPrice());
                }
                if (distributorCostDO.getActualPrice().compareTo(new BigDecimal(0)) == 0) {
                    distributorCostDO.setItemType(Constant.ITEM_TYPE_2);
                }
                changeDistributorCostDOS.add(distributorCostDO);
            }
        });
        return changeDistributorCostDOS;
    }

    public static void toOrderGoodsDistributorCostDOList(List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsDistributorCostDO> distributorCostDOS, List<GoodsSaleDTO> saleDTOS) {
        Map<Integer, OrderGoodsDO> orderGoodsDOMap =
            orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
        Map<Integer, GoodsSaleDTO> saleDTOMap =
            saleDTOS.stream().collect(Collectors.toMap(GoodsSaleDTO::getOrderGoodsId, goodsSaleDTO -> goodsSaleDTO));
        distributorCostDOS.forEach(distributorCostDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(distributorCostDO.getOrderGoodsId());
            GoodsSaleDTO saleDTO = saleDTOMap.get(distributorCostDO.getOrderGoodsId());
            if (saleDTO != null) {
                saleDTO.setSpellGroupId(distributorCostDO.getSpellGroupId());
                saleDTO.setGoodsPromotionId(distributorCostDO.getGoodsPromotionId());
                saleDTO.setOrderPromotionId(distributorCostDO.getOrderPromotionId());
            }
            distributorCostDO.setOrderGoodsDO(orderGoodsDO);
        });
    }

    public static BigDecimal toOrderDistributorCostDOChange(OrderDistributorCostDO orderDistributorCostDO,
        List<OrderGoodsDistributorCostDO> distributorCostDOS) {
        AtomicReference<BigDecimal> goodsAmount = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> goodsPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> orderPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        BigDecimal payAmount = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(distributorCostDOS)) {
            distributorCostDOS.forEach(distributorCostDO -> {
                OrderGoodsDO orderGoodsDO = distributorCostDO.getOrderGoodsDO();
                goodsAmount.set(goodsAmount.get()
                    .add(new BigDecimal(
                        distributorCostDO.getSalePrice().doubleValue() * orderGoodsDO.getItemCount().intValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP)));
                if (distributorCostDO.getActualPrice().compareTo(distributorCostDO.getSalePrice()) != 0) {
                    if (distributorCostDO.getGoodsPromotionId() != null
                        || distributorCostDO.getSpellGroupId() != null) {
                        goodsPromotionAmount.set(goodsPromotionAmount.get().add(new BigDecimal(
                            distributorCostDO.getSalePrice().subtract(distributorCostDO.getActualPrice()).doubleValue()
                                * orderGoodsDO.getItemCount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else if (distributorCostDO.getOrderPromotionId() != null) {
                        orderPromotionAmount.set(orderPromotionAmount.get().add(new BigDecimal(
                            distributorCostDO.getSalePrice().subtract(distributorCostDO.getActualPrice()).doubleValue()
                                * orderGoodsDO.getItemCount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else {
                        goodsPromotionAmount.set(goodsPromotionAmount.get().add(new BigDecimal(
                            distributorCostDO.getSalePrice().subtract(distributorCostDO.getActualPrice()).doubleValue()
                                * orderGoodsDO.getItemCount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            });
            orderDistributorCostDO.setGoodsPromotionAmount(goodsPromotionAmount.get());
            orderDistributorCostDO.setOrderPromotionAmount(orderPromotionAmount.get());
            orderDistributorCostDO.setGoodsAmount(goodsAmount.get());
        }
        payAmount = orderDistributorCostDO.getGoodsAmount().subtract(orderDistributorCostDO.getGoodsPromotionAmount())
            .subtract(orderDistributorCostDO.getOrderPromotionAmount())
            .add(orderDistributorCostDO.getDistributionAmount());
        BigDecimal changeAmount = payAmount.subtract(orderDistributorCostDO.getPayAmount());
        orderDistributorCostDO.setPayAmount(payAmount);
        return changeAmount;
    }

    public static OrderRefundDTO toOrderChangeRefundDTO(BigDecimal changeAmount,
        OrderDistributorDataDO distributorDataDO, OrderDistributorCostDO distributorCostDO, String operatorId,
        String operatorName, String remark, DistributorRpcDTO erpDistributor) {
        changeAmount = changeAmount.abs();
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setReceiverType(Constant.RECEIVER_TYPE_1);
        refundDTO.setDistributorId(distributorDataDO.getDistributorId());
        refundDTO.setDistributorName(distributorDataDO.getDistributorName());
        refundDTO.setCompanyName(distributorDataDO.getCompanyName());
        refundDTO.setDistributorRefundId(distributorDataDO.getDistributorAncestorId());
        refundDTO.setOrderId(distributorDataDO.getOrderId());
        refundDTO.setBusinessTypes(Constant.REFUND_BUSINESS_TYPE_2);
        refundDTO.setOutTradeNo(distributorCostDO.getOutTradeNo());
        // 分销层数据可能存在混合退款方式
        if (distributorCostDO.getDepositAmount() != null && distributorCostDO.getDepositAmount().doubleValue() > 0) {
            if (distributorCostDO.getDepositAmount().compareTo(changeAmount) > 0) {
                refundDTO.setDepositAmount(
                    changeAmount.setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                changeAmount = new BigDecimal(0);
            } else {
                refundDTO.setDepositAmount(distributorCostDO.getDepositAmount());
                changeAmount = changeAmount.subtract(distributorCostDO.getDepositAmount());
            }
        }
        refundDTO
            .setCashAmount(changeAmount.setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
        // 同步erp层，现款线上(非快钱)且开启erp余额同步情况为其他退款方式，现款下线统一为其他退款方式
        if (distributorDataDO.getErpFlag().equals(Constant.ERP_FLAG_1)) {
            if (erpDistributor.getErpBalanceFlag().equals(Constant.ERP_BALANCE_FLAG_0)
                && StringUtils.isNotBlank(distributorCostDO.getOutTradeNo())
                && !distributorDataDO.getPayWay().equals(PayWay.Kuaiqian_payment.getValue())
                && !distributorDataDO.getPayWay().equals(PayWay.Balance_Kuaiqian_payment.getValue())
                && refundDTO.getCashAmount().doubleValue() > 0) {
                // 未开启erp同步，现款线上，非快钱且现金大于0情况
                if (refundDTO.getDepositAmount() != null && refundDTO.getDepositAmount().doubleValue() > 0) {
                    refundDTO.setRefundType(Constant.REFUND_TYPE_4);
                } else {
                    refundDTO.setRefundType(Constant.REFUND_TYPE_1);
                }
            } else if (erpDistributor.getErpBalanceFlag().equals(Constant.ERP_BALANCE_FLAG_0)
                && ((StringUtils.isNotBlank(distributorCostDO.getOutTradeNo())
                    && (distributorDataDO.getPayWay().equals(PayWay.Kuaiqian_payment.getValue())
                        || distributorDataDO.getPayWay().equals(PayWay.Balance_Kuaiqian_payment.getValue())
                        || refundDTO.getCashAmount().doubleValue() == 0))
                    || distributorDataDO.getPayWay().equals(PayWay.Balance_payment.getValue()))) {
                // 未开启erp同步，现款线上，快钱或现金为零情况
                refundDTO.setRefundType(Constant.REFUND_TYPE_2);
            } else {
                // 其他情况为其他退款方式
                refundDTO.setRefundType(Constant.REFUND_TYPE_3);
            }
        }
        if (distributorDataDO.getDistributionMode().equals(Constant.CUSTOMER_MODE_1)) {
            refundDTO.setRefundMode(Constant.REFUND_MODE_1);
        } else {
            // 分销商自己收款
            refundDTO.setDistributorRefundId(distributorDataDO.getDistributorId());
            refundDTO.setRefundMode(Constant.REFUND_MODE_2);
        }
        refundDTO.setRemark(remark);
        if (StringUtils.isNotBlank(operatorId)) {
            refundDTO.setOperatorId(Integer.valueOf(operatorId));
        }
        refundDTO.setOperatorName(operatorName);
        return refundDTO;
    }

    public static LogisticsCalculationRpcQry toLogisticsCalculationRpcQry(Integer logisticsId, OrderDeliveryDO delivery,
        List<OrderGoodsDO> orderGoodss, OrderDistributorCostDO orderDistributorCostDO) {
        LogisticsCalculationRpcQry rpcQry = new LogisticsCalculationRpcQry();
        rpcQry.setLogisticsId(logisticsId);
        BeanUtils.copyProperties(delivery, rpcQry);
        AtomicReference<Double> weight = new AtomicReference<>(0.00);
        orderGoodss.forEach(orderGoods -> {
            if (orderGoods.getWeight() == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_WEIGHT_NULL,
                    "\"" + orderGoods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_WEIGHT_NULL));
            }
            weight.set(weight.get() + orderGoods.getWeight().doubleValue() * orderGoods.getItemCount());
        });
        rpcQry.setWeight(weight.get());
        rpcQry.setPrice(orderDistributorCostDO.getGoodsAmount());
        return rpcQry;
    }

    public static List<OrderRefundDTO> toOrderChangeRefundDTO(List<OrderGoodsDO> orderGoodsDOS,
        OrderCustomerDataDO customerDataDO, OrderCustomerCostDO orderCustomerCostDO,
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, List<OrderDistributorDataDO> distributorDataDOS,
        List<OrderDistributorCostDO> distributorCostDOS, List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS,
        List<OrderDistributorCostDO> changeDistributorCostDOS, List<OrderDistributorDataDO> changeDistributorDataDOS,
        String remark, String operatorId, String operatorName) {
        List<OrderRefundDTO> refundDTOS = new ArrayList<>();
        Map<Integer, OrderGoodsDO> orderGoodsDOMap =
            orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
        Map<Integer, OrderDistributorCostDO> distributorCostDOMap = distributorCostDOS.stream().collect(
            Collectors.toMap(OrderDistributorCostDO::getDistributorId, distributorCostDO -> distributorCostDO));
        Map<Integer, List<OrderGoodsDistributorCostDO>> goodsDistributorCostDOSMap = goodsDistributorCostDOS.stream()
            .collect(Collectors.groupingBy(OrderGoodsDistributorCostDO::getDistributorId));
        if (customerDataDO != null) {
            // C端客户订单
            if (StringUtils.isNotBlank(orderCustomerCostDO.getOutTradeNo())) {
                // 非零订单，原路返还（C端暂不支持快钱）
                OrderRefundDTO refundDTO = new OrderRefundDTO();
                refundDTO.setReceiverType(Constant.RECEIVER_TYPE_2);
                refundDTO.setCustomerId(customerDataDO.getCustomerId());
                refundDTO.setCustomerName(customerDataDO.getCustomerName());
                refundDTO.setOrderId(customerDataDO.getOrderId());
                refundDTO.setBusinessTypes(Constant.REFUND_BUSINESS_TYPE_2);
                refundDTO.setOutTradeNo(orderCustomerCostDO.getOutTradeNo());
                refundDTO.setDepositAmount(new BigDecimal(0));
                final BigDecimal[] actualAmount = {new BigDecimal(0)};
                final BigDecimal[] goodsAmount = {new BigDecimal(0)};
                orderGoodsCustomerCostDOS.forEach(orderGoodsCustomerCostDO -> {
                    OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
                    actualAmount[0] = actualAmount[0]
                        .add(new BigDecimal(orderGoodsDO.getItemCount().intValue()
                            * orderGoodsCustomerCostDO.getActualPrice().doubleValue()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                    goodsAmount[0] = goodsAmount[0]
                        .add(new BigDecimal(orderGoodsDO.getItemCount().intValue()
                            * orderGoodsCustomerCostDO.getSalePrice().doubleValue()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                });
                // 需计算运费
                BigDecimal refundAmount = new BigDecimal(0);
                if (orderCustomerCostDO.getDistributionAmount() != null) {
                    refundAmount = orderCustomerCostDO.getPayAmount().subtract(actualAmount[0])
                        .subtract(orderCustomerCostDO.getDistributionAmount());
                } else {
                    refundAmount = orderCustomerCostDO.getPayAmount().subtract(actualAmount[0]);
                }
                if (refundAmount.compareTo(new BigDecimal(0)) > 0) {
                    customerDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                    orderCustomerCostDO.setPayAmount(orderCustomerCostDO.getPayAmount().subtract(refundAmount));
                    orderCustomerCostDO.setGoodsAmount(goodsAmount[0]);
                    if (goodsAmount[0].compareTo(actualAmount[0]) != 0) {
                        if (orderCustomerCostDO.getGoodsPromotionAmount() != null
                            && orderCustomerCostDO.getGoodsPromotionAmount().compareTo(new BigDecimal(0)) > 0) {
                            orderCustomerCostDO.setGoodsPromotionAmount(goodsAmount[0].subtract(actualAmount[0]));
                        } else if (orderCustomerCostDO.getOrderPromotionAmount() != null
                            && orderCustomerCostDO.getOrderPromotionAmount().compareTo(new BigDecimal(0)) > 0) {
                            orderCustomerCostDO.setOrderPromotionAmount(goodsAmount[0].subtract(actualAmount[0]));
                        } else if (orderCustomerCostDO.getOrderCouponAmount() != null
                            && orderCustomerCostDO.getOrderCouponAmount().compareTo(new BigDecimal(0)) > 0) {
                            orderCustomerCostDO.setOrderCouponAmount(goodsAmount[0].subtract(actualAmount[0]));
                        }
                    }
                    refundDTO.setCashAmount(
                        refundAmount.setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                    refundDTO.setRefundType(Constant.REFUND_TYPE_1);
                    refundDTO.setRemark(remark);
                    if (StringUtils.isNotBlank(operatorId)) {
                        refundDTO.setOperatorId(Integer.valueOf(operatorId));
                    }
                    refundDTO.setOperatorName(operatorName);
                    if (customerDataDO.getCustomerMode().equals(Constant.CUSTOMER_MODE_1)) {
                        refundDTO.setRefundMode(Constant.REFUND_MODE_1);
                    } else {
                        // 分销商收款
                        refundDTO.setDistributorRefundId(customerDataDO.getDistributorId());
                        refundDTO.setRefundMode(Constant.REFUND_MODE_2);
                    }
                    refundDTOS.add(refundDTO);
                }
            }
        }
        // 分销订单处理退款
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            distributorDataDOS.forEach(distributorDataDO -> {
                if (distributorDataDO.getErpFlag().equals(Constant.ERP_FLAG_0)) {
                    OrderDistributorCostDO distributorCostDO =
                        distributorCostDOMap.get(distributorDataDO.getDistributorId());
                    List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOS =
                        goodsDistributorCostDOSMap.get(distributorDataDO.getDistributorId());
                    // 重新计算订单费用
                    final BigDecimal[] actualAmount = {new BigDecimal(0)};
                    final BigDecimal[] goodsAmount = {new BigDecimal(0)};
                    orderGoodsDistributorCostDOS.forEach(orderGoodsDistributorCostDO -> {
                        OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsDistributorCostDO.getOrderGoodsId());
                        actualAmount[0] = actualAmount[0]
                            .add(new BigDecimal(orderGoodsDO.getItemCount().intValue()
                                * orderGoodsDistributorCostDO.getActualPrice().doubleValue()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                        goodsAmount[0] = goodsAmount[0]
                            .add(new BigDecimal(orderGoodsDO.getItemCount().intValue()
                                * orderGoodsDistributorCostDO.getActualPrice().doubleValue()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                    });
                    BigDecimal refundAmount = new BigDecimal(0);
                    // 需计算运费
                    if (distributorCostDO.getDistributionAmount() != null) {
                        refundAmount = distributorCostDO.getPayAmount().subtract(actualAmount[0])
                            .subtract(distributorCostDO.getDistributionAmount());
                    } else {
                        refundAmount = distributorCostDO.getPayAmount().subtract(actualAmount[0]);
                    }
                    if (refundAmount.compareTo(new BigDecimal(0)) > 0) {
                        distributorDataDO.setPayStatus(PayStatus.PARTIAL_REFUND.getValue());
                        distributorCostDO.setPayAmount(distributorCostDO.getPayAmount().subtract(refundAmount));
                        distributorCostDO.setGoodsAmount(goodsAmount[0]);
                        distributorCostDO.setRefundedAmount(refundAmount);
                        if (goodsAmount[0].compareTo(actualAmount[0]) != 0) {
                            if (distributorCostDO.getGoodsPromotionAmount() != null
                                && distributorCostDO.getGoodsPromotionAmount().compareTo(new BigDecimal(0)) > 0) {
                                distributorCostDO.setGoodsPromotionAmount(goodsAmount[0].subtract(actualAmount[0]));
                            } else if (distributorCostDO.getOrderPromotionAmount() != null
                                && distributorCostDO.getOrderPromotionAmount().compareTo(new BigDecimal(0)) > 0) {
                                distributorCostDO.setOrderPromotionAmount(goodsAmount[0].subtract(actualAmount[0]));
                            }
                        }
                        changeDistributorCostDOS.add(distributorCostDO);
                        changeDistributorDataDOS.add(distributorDataDO);
                        // 已付款（注意：有可能平台收款）、且已付金额不为空情况需生成退款申请数据
                        if (!distributorDataDO.getPayStatus().equals(PayStatus.UNPAID.getValue())
                            && !distributorDataDO.getPayStatus().equals(PayStatus.REFUND.getValue())
                            && ((distributorCostDO.getPaidAmount() != null
                                && distributorCostDO.getPaidAmount().doubleValue() > 0)
                                || (distributorCostDO.getDepositAmount() != null
                                    && distributorCostDO.getDepositAmount().doubleValue() > 0))) {
                            distributorDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                            // 分销层生成退款申请数据
                            toOrderChangeRefundDTO(distributorDataDO, distributorCostDO, refundAmount, refundDTOS,
                                remark, operatorId, operatorName);
                        }
                    }
                }
            });
        }
        return refundDTOS;
    }

    public static void toOrderChangeRefundDTO(OrderDistributorDataDO distributorDataDO,
        OrderDistributorCostDO distributorCostDO, BigDecimal refundAmount, List<OrderRefundDTO> refundDTOS,
        String remark, String operatorId, String operatorName) {
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTOS.add(refundDTO);
        refundDTO.setReceiverType(Constant.RECEIVER_TYPE_1);
        refundDTO.setDistributorId(distributorDataDO.getDistributorId());
        refundDTO.setDistributorName(distributorDataDO.getDistributorName());
        refundDTO.setCompanyName(distributorDataDO.getCompanyName());
        refundDTO.setDistributorRefundId(distributorDataDO.getDistributorAncestorId());
        refundDTO.setOrderId(distributorDataDO.getOrderId());
        refundDTO.setBusinessTypes(Constant.REFUND_BUSINESS_TYPE_2);
        refundDTO.setOutTradeNo(distributorCostDO.getOutTradeNo());
        // 分销层数据可能存在混合退款方式
        if (refundDTO.getDepositAmount() != null && refundDTO.getDepositAmount().doubleValue() > 0) {
            if (refundDTO.getDepositAmount().compareTo(refundAmount) > 0) {
                refundDTO.setDepositAmount(refundAmount);
                refundAmount = new BigDecimal(0);
            } else {
                refundDTO.setDepositAmount(refundDTO.getDepositAmount());
                refundAmount = refundAmount.subtract(refundDTO.getDepositAmount());
            }
        }
        refundDTO
            .setCashAmount(refundAmount.setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
        // 分销层数据且非同步erp层，现款线上统一为原路返还，余额支付退回余额，现款下线统一为其他退款方式
        if (StringUtils.isNotBlank(distributorCostDO.getOutTradeNo())
            && !distributorDataDO.getPayWay().equals(PayWay.Kuaiqian_payment.getValue())
            && !distributorDataDO.getPayWay().equals(PayWay.Balance_Kuaiqian_payment.getValue())) {
            if (refundDTO.getDepositAmount() != null && refundDTO.getDepositAmount().doubleValue() > 0) {
                refundDTO.setRefundType(Constant.REFUND_TYPE_4);
            } else {
                refundDTO.setRefundType(Constant.REFUND_TYPE_1);
            }
        } else {
            refundDTO.setRefundType(Constant.REFUND_TYPE_2);
        }
        if (distributorDataDO.getDistributionMode().equals(Constant.CUSTOMER_MODE_1)) {
            refundDTO.setRefundMode(Constant.REFUND_MODE_1);
        } else {
            // 分销商自己收款
            refundDTO.setDistributorRefundId(distributorDataDO.getDistributorId());
            refundDTO.setRefundMode(Constant.REFUND_MODE_2);
        }
        refundDTO.setRemark(remark);
        if (StringUtils.isNotBlank(operatorId)) {
            refundDTO.setOperatorId(Integer.valueOf(operatorId));
        }
        refundDTO.setOperatorName(operatorName);
    }

    public static List<OrderGoodsCustomerCostDO> orderPayGoodsCustomer(List<OrderCustomerCostDO> customerCostDOS,
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCosts, OrderPayDTO cmd) {
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS = new ArrayList<>();
        if (!cmd.getAlipayVoucherBizFlag()) {
            return orderGoodsCustomerCostDOS;
        }
        Map<Integer, List<OrderGoodsCustomerCostDO>> orderGoodsCustomerCostMap =
            orderGoodsCustomerCosts.stream().collect(Collectors.groupingBy(OrderGoodsCustomerCostDO::getOrderId));
        for (OrderCustomerCostDO customerCostDO : customerCostDOS) {
            // 优惠金额
            BigDecimal voucherAmount = customerCostDO.getAlipayVoucherAmount();
            if (voucherAmount == null || voucherAmount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            List<OrderGoodsCustomerCostDO> costDOList = orderGoodsCustomerCostMap.get(customerCostDO.getOrderId());
            if (CollectionUtils.isEmpty(costDOList)) {
                continue;
            }
            BigDecimal sumActualPrice = costDOList.stream().map(OrderGoodsCustomerCostDO::getActualPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            costDOList.forEach(orderGoodsCustomerCostDO -> {
                BigDecimal voucherAmountNew =
                    orderGoodsCustomerCostDO.getActualPrice().divide(sumActualPrice, 16, RoundingMode.HALF_UP)
                        .multiply(voucherAmount).setScale(4, RoundingMode.HALF_UP);
                orderGoodsCustomerCostDO
                    .setActualPrice(orderGoodsCustomerCostDO.getActualPrice().subtract(voucherAmountNew));
            });
            orderGoodsCustomerCostDOS.addAll(costDOList);
        }
        return orderGoodsCustomerCostDOS;
    }
}
