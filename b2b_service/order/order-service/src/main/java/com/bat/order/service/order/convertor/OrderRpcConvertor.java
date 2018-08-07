package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.data.dao.OrderCustomerDO;
import com.bat.order.service.common.data.dto.*;
import com.bat.order.service.common.enumtype.PayStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeUseCmd;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.order.order.dto.OrderInfoCmd;
import com.bat.dubboapi.order.order.dto.data.OrderCostRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderDTO;
import com.bat.dubboapi.order.order.dto.data.OrderTradeRpcDTO;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.data.dto.*;

public class OrderRpcConvertor {

    /**
     * 
     * @param distributorCostDOS
     *            付款分销商订单总费用直属分销商表信息
     * @param distributorDataDOS
     *            付款分销商订单直属分销商信息表信息
     * @param extendDataRpcDTO
     *            该笔订单收款的分销商
     * @param orderInfoDOMap
     *            订单信息
     * @param counterpartyType
     *            付款人类型1分销商 2 C端客户
     * @return
     */
    public static OrderTradeRpcDTO toOrderTradeRpcDTOForDistributor(List<OrderDistributorCostDO> distributorCostDOS,
        List<OrderDistributorDataDO> distributorDataDOS, DistributorExtendDataRpcDTO extendDataRpcDTO,
        Map<Integer, OrderInfoDO> orderInfoDOMap, Short counterpartyType) {
        OrderTradeRpcDTO rpcDTO = new OrderTradeRpcDTO();
        rpcDTO.setFlag(CommonErrorCode.B_TRADE_SUCCESS);
        List<OrderCostRpcDTO> costRpcDTOS = new ArrayList<>();
        rpcDTO.setOrderCosts(costRpcDTOS);
        Map<Integer, OrderDistributorDataDO> distributorDataDOMap = distributorDataDOS.stream()
            .collect(Collectors.toMap(OrderDistributorDataDO::getOrderId, distributorDataDO -> distributorDataDO));
        // 循环付款分销商订单总费用直属分销商表信息
        distributorCostDOS.forEach(distributorCostDO -> {
            // 获取指定订单信息
            OrderDistributorDataDO distributorDataDO = distributorDataDOMap.get(distributorCostDO.getOrderId());
            OrderInfoDO orderInfoDO = orderInfoDOMap.get(distributorCostDO.getOrderId());
            // 已付款
            if (distributorDataDO.getPayStatus().equals(PayStatus.PAID.getValue())) {
                rpcDTO.setFlag(CommonErrorCode.B_TRADE_PAID_ERROR);
                rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_TRADE_PAID_ERROR));
                return;
            }
            // 货币类型判空
            if (StringUtils.isBlank(rpcDTO.getCurrencyType())) {
                rpcDTO.setDistributorId(distributorDataDO.getDistributorAncestorId());
                rpcDTO.setOrganizationId(orderInfoDO.getOrganizationId());
                rpcDTO.setCounterpartyType(counterpartyType);
                if (StringUtils.isNotBlank(distributorDataDO.getCurrencyType())) {
                    rpcDTO.setCurrencyType(distributorDataDO.getCurrencyType());
                } else {
                    rpcDTO.setCurrencyType("CNY");
                }
                rpcDTO.setPayWay(distributorDataDO.getPayWay());
                if (extendDataRpcDTO == null || extendDataRpcDTO.getDistributionMode() == null) {
                    rpcDTO.setTradeMode(Constant.TRADE_MODE_1);
                } else if (extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_3)) {
                    rpcDTO.setTradeMode(Constant.TRADE_MODE_2);
                } else if (extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)) {
                    rpcDTO.setTradeMode(Constant.TRADE_MODE_1);
                }
            } else if (StringUtils.isNotBlank(distributorDataDO.getCurrencyType())
                && !rpcDTO.getCurrencyType().equals(distributorDataDO.getCurrencyType())) {
                rpcDTO.setFlag(CommonErrorCode.B_TRADE_CURRENCY_TYPE_ERROR);
                rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_TRADE_CURRENCY_TYPE_ERROR));
                return;
            } else if (!rpcDTO.getPayWay().equals(distributorDataDO.getPayWay())) {
                rpcDTO.setFlag(CommonErrorCode.B_TRADE_PAY_WAY_ERROR);
                rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_TRADE_PAY_WAY_ERROR));
                return;
            }
            OrderCostRpcDTO costRpcDTO = new OrderCostRpcDTO();
            BeanUtils.copyProperties(distributorCostDO, costRpcDTO);
            costRpcDTOS.add(costRpcDTO);
        });
        return rpcDTO;
    }

    public static OrderTradeRpcDTO toOrderTradeRpcDTOForCustomer(List<OrderCustomerCostDO> customerCostDOS,
        List<OrderCustomerDataDO> customerDataDOS, DistributorExtendDataRpcDTO extendDataRpcDTO,
        Map<Integer, OrderInfoDO> orderInfoDOMap, Short counterpartyType) {
        OrderTradeRpcDTO rpcDTO = new OrderTradeRpcDTO();
        rpcDTO.setFlag(CommonErrorCode.B_TRADE_SUCCESS);
        List<OrderCostRpcDTO> costRpcDTOS = new ArrayList<>();
        rpcDTO.setOrderCosts(costRpcDTOS);
        Map<Integer, OrderCustomerDataDO> customerDataDOMap = customerDataDOS.stream()
            .collect(Collectors.toMap(OrderCustomerDataDO::getOrderId, customerDataDO -> customerDataDO));
        for (int x = 0; x < customerDataDOS.size(); x++) {
            if (StringUtils.isNotBlank(customerDataDOS.get(x).getShopCode())) {
                // 属于门店订单、可能要走分账模式、服务商下单
                rpcDTO.setShopOrderFlag(true);
                break;
            }
        }

        customerCostDOS.forEach(customerCostDO -> {
            OrderCustomerDataDO customerDataDO = customerDataDOMap.get(customerCostDO.getOrderId());
            OrderInfoDO orderInfoDO = orderInfoDOMap.get(customerCostDO.getOrderId());
            boolean needPayFlag = true;
            if (customerDataDO.getPayStatus().equals(PayStatus.PAID.getValue())) {
                /*rpcDTO.setFlag(B_TRADE_PAID_ERROR);
                rpcDTO.setMsg(MessageUtils.get(B_TRADE_PAID_ERROR));
                return;*/
                needPayFlag = false;
            }
            if (StringUtils.isBlank(rpcDTO.getCurrencyType())) {
                rpcDTO.setDistributorId(customerDataDO.getDistributorId());
                rpcDTO.setOrganizationId(orderInfoDO.getOrganizationId());
                rpcDTO.setCounterpartyType(counterpartyType);
                // C端客户暂不支持外币
                rpcDTO.setCurrencyType("CNY");
                rpcDTO.setPayWay(customerDataDO.getPayWay());
                if (extendDataRpcDTO.getCustomerMode().equals(Constant.DISTRIBUTION_MODE_3)) {
                    rpcDTO.setTradeMode(Constant.TRADE_MODE_2);
                } else if (extendDataRpcDTO.getCustomerMode().equals(Constant.DISTRIBUTION_MODE_1)) {
                    rpcDTO.setTradeMode(Constant.TRADE_MODE_1);
                }
            } else if (!rpcDTO.getPayWay().equals(customerDataDO.getPayWay())) {
                /*  rpcDTO.setFlag(B_TRADE_PAY_WAY_ERROR);
                rpcDTO.setMsg(MessageUtils.get(B_TRADE_PAY_WAY_ERROR));
                return;*/
                needPayFlag = false;
            }
            OrderCostRpcDTO costRpcDTO = new OrderCostRpcDTO();
            BeanUtils.copyProperties(customerCostDO, costRpcDTO);
            if (needPayFlag) {
                costRpcDTOS.add(costRpcDTO);
            }
        });
        return rpcDTO;
    }

    public static List<ExchangeCodeUseCmd> toExchangeCodeUseCmdList(Integer userId, String userName,
        List<OrderGoodsDO> orderGoodss) {
        List<ExchangeCodeUseCmd> cmds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderGoodss)) {
            orderGoodss.forEach(orderGoods -> {
                ExchangeCodeUseCmd cmd = new ExchangeCodeUseCmd();
                List<String> secretCodes = orderGoods.getExchangeCodes().stream()
                    .map(OrderGoodsExchangeCodeDO::getSecretCode).collect(Collectors.toList());
                cmd.setOrderId(orderGoods.getOrderId());
                cmd.setOrderGoodId(orderGoods.getId());
                cmd.setUserId(userId);
                cmd.setUserName(userName);
                cmd.setSecretCodeList(secretCodes);
                cmds.add(cmd);
            });
        }
        return cmds;
    }

    public static OrderInfoDTO toOrderInfoDTO(OrderInfoCmd cmd) {
        OrderInfoDTO dto = new OrderInfoDTO();
        BeanUtils.copyProperties(cmd, dto);
        if (dto.getInvoiceFlag() == null) {
            dto.setInvoiceFlag(Constant.INVOICE_FLAG_0);
        }
        OrderDeliveryDTO delivery = new OrderDeliveryDTO();
        BeanUtils.copyProperties(cmd.getDelivery(), delivery);
        dto.setDelivery(delivery);
        List<OrderGoodsDTO> goodss = new ArrayList<>();
        dto.setGoodss(goodss);
        cmd.getGoodss().forEach(goods -> {
            OrderGoodsDTO goodsDTO = new OrderGoodsDTO();
            BeanUtils.copyProperties(goods, goodsDTO);
            OrderGoodsDiyDTO diyDTO = new OrderGoodsDiyDTO();
            BeanUtils.copyProperties(goods.getDiy(), diyDTO);
            goodsDTO.setDiy(diyDTO);
            goodss.add(goodsDTO);
        });
        List<OrderLogisticsDTO> logisticss = new ArrayList<>();
        dto.setLogisticss(logisticss);
        cmd.getLogisticss().forEach(logistics -> {
            OrderLogisticsDTO logisticsDTO = new OrderLogisticsDTO();
            BeanUtils.copyProperties(logistics, logisticsDTO);
            logisticss.add(logisticsDTO);
        });
        return dto;
    }

    public static List<OrderDTO> toOrderDTO(List<OrderInfoDO> orders) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(order -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setOrderNo(order.getOrderNo());
            orderDTOS.add(orderDTO);
        });
        return orderDTOS;
    }

    public static GoodsItemPriceRpcQry toGoodsItemPriceRpcQry(List<OrderGoodsDO> orderGoodsDOS, Integer distributorId) {
        GoodsItemPriceRpcQry rpcQry = new GoodsItemPriceRpcQry();
        rpcQry.setDistributorId(distributorId);
        rpcQry.setRetailPriceFlag(Constant.RETAIL_PRICE_FLAG_0);
        List<GoodsItemRpc> goodsItems = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();
        rpcQry.setGoodsItems(goodsItems);
        orderGoodsDOS.forEach(orderGoodsDO -> {
            if (!itemIds.contains(orderGoodsDO.getItemId())) {
                GoodsItemRpc goodsItemRpc = new GoodsItemRpc();
                goodsItemRpc.setGoodsId(orderGoodsDO.getGoodsId());
                goodsItemRpc.setItemId(orderGoodsDO.getItemId());
                goodsItems.add(goodsItemRpc);
                itemIds.add(orderGoodsDO.getItemId());
            }
        });
        return rpcQry;
    }

    public static List<ExchangeCodeUseCmd> toExchangeCodeUseList(List<OrderCustomerDO> orderCustomerDOList) {
        List<ExchangeCodeUseCmd> exchangeCodeUseCmds = new ArrayList<>();
        orderCustomerDOList.forEach(orderCustomerDO -> {
            List<OrderGoodsDO> orderGoodss = orderCustomerDO.getOrderGoodss();
            orderGoodss.forEach(orderGoods -> {
                ExchangeCodeUseCmd exchangeCodeUseCmd = new ExchangeCodeUseCmd();
                OrderCustomerDataDO orderCustomerData = orderCustomerDO.getOrderCustomerData();
                exchangeCodeUseCmd.setUserId(orderCustomerData.getCustomerId());
                exchangeCodeUseCmd.setUserName(orderCustomerData.getCustomerName());
                exchangeCodeUseCmd.setOrderId(orderCustomerData.getOrderId());
                exchangeCodeUseCmd.setOrderGoodId(orderGoods.getId());
                List<String> secretCodeList = new ArrayList<>();
                exchangeCodeUseCmd.setSecretCodeList(secretCodeList);
                List<OrderGoodsExchangeCodeDO> exchangeCodes = orderGoods.getExchangeCodes();
                exchangeCodes.forEach(exchangeCode -> {
                    secretCodeList.add(exchangeCode.getSecretCode());
                });
                exchangeCodeUseCmds.add(exchangeCodeUseCmd);
            });
        });
        return exchangeCodeUseCmds;
    }
}
