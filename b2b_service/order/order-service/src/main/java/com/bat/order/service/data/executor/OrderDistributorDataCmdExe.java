package com.bat.order.service.data.executor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import com.bat.order.service.data.convertor.OrderDataConvertor;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.executor.OrderRpcExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.OrderPayStatusCmd;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.mq.dto.OrderPayDTO;
import com.bat.order.service.cost.executor.OrderDistributorCostCmdExe;
import com.bat.order.service.cost.executor.OrderDistributorCostQryExe;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderDistributorDataCmdExe {

    @Resource
    private OrderDistributorDataMapper orderDistributorDataMapper;
    @Resource
    private OrderRpcExe orderRpcExe;
    @Resource
    private OrderDistributorCostCmdExe orderDistributorCostCmdExe;
    @Resource
    private OrderDistributorCostQryExe orderDistributorCostQryExe;

    @Resource
    private MessageSendService sendService;

    /**
     * 订单付款，更新分销订单付款状态
     * 
     * @param cmd
     * @param distributorCostDOS
     */
    public void orderPay(OrderPayDTO cmd, List<OrderDistributorCostDO> distributorCostDOS,
        List<OrderDistributorDataDO> distributorDataDOS) {
        OrderDataConvertor.orderPayDistributor(cmd.getPadTime(), distributorCostDOS, distributorDataDOS);
        orderDistributorDataMapper.updateList(distributorDataDOS);
        for (OrderDistributorDataDO orderDistributorDataDO : distributorDataDOS) {
            if (orderDistributorDataDO.getOrderStatus() != null
                && orderDistributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()) {
                sendService.orderConfirmMsg(orderDistributorDataDO.getOrderId());
                break;
            }
        }
    }

    /**
     * 根据C端客户
     * 
     * @param orderCustomerData
     * @return
     */
    public OrderDistributorDataDO createOrderDistributorData(OrderInfoDO order, OrderCustomerDataDO orderCustomerData,
        DistributorRpcDTO distributor, OrderDistributorCostDO distributorCost,
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS) {
        OrderDistributorDataDO distributorDataDO =
            OrderDataConvertor.toOrderDistributorData(order, orderCustomerData, distributor, distributorCost);
        // 兑换卡
        // 包邮情况订单自动确认，已收款，且结算方式与兑换卡订单一致
        // 收邮费订单，不做处理
        if (!CollectionUtils.isEmpty(exchangeCodeDOS)) {
            AtomicBoolean mailFlag = new AtomicBoolean(false);
            AtomicReference<OrderGoodsExchangeCodeDO> orderExchange = new AtomicReference(null);
            exchangeCodeDOS.forEach(exchangeCodeDO -> {
                if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                    mailFlag.set(true);
                    orderExchange.set(exchangeCodeDO);
                }
            });
            if (mailFlag.get()) {
                OrderDistributorDataDO exchangeDistributorDataDO = null;
                if (orderExchange.get().getExchangeOrderId() != null
                    && orderExchange.get().getDistributorId() != null) {
                    exchangeDistributorDataDO = orderDistributorDataMapper.getByOrderIdAndDistributorId(
                        orderExchange.get().getExchangeOrderId(), orderExchange.get().getDistributorId());
                }
                if (exchangeDistributorDataDO != null) {
                    distributorDataDO.setPayStatus(exchangeDistributorDataDO.getPayStatus());
                    if (exchangeDistributorDataDO.getPayWay() == PayWay.Alipay.getValue().shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.WeChat.getValue().shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Offline_payment.getValue().shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Balance_payment.getValue().shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Kuaiqian_payment.getValue().shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Balance_Alipay_payment.getValue()
                            .shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Balance_WeChat_payment.getValue()
                            .shortValue()
                        || exchangeDistributorDataDO.getPayWay() == PayWay.Balance_Kuaiqian_payment.getValue()
                            .shortValue()) {
                        // 设置为已付款
                        distributorDataDO.setPayStatus(PayStatus.PAID.getValue());
                    }
                    distributorDataDO.setPayWay(exchangeDistributorDataDO.getPayWay());
                }
                distributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());

            }
        }
        orderDistributorDataMapper.insert(distributorDataDO);
        sendService.newOrderMsg(distributorDataDO.getOrderId(), distributorDataDO.getDistributorId());
        if (distributorDataDO.getOrderStatus() != null
            && distributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()
            && distributorDataDO.getTreeNode() == 1) {
            sendService.orderConfirmMsg(distributorDataDO.getOrderId());
        }
        return distributorDataDO;
    }

    /**
     * ERP订单变更（包含分销层数据变化）
     *
     * @param cmd
     * @return
     */
    public OrderDistributorDataDO orderChangeByErp(ErpOrderChangeCmd cmd, Map<String, List<Object>> changeMap) {
        List<OrderDistributorDataDO> distributorDataDOS =
            orderDistributorDataMapper.listByOrderErpNoAndErpFlag(cmd.getOrderNo(), Constant.ERP_FLAG_1);
        OrderDistributorDataDO erpDistributorData = distributorDataDOS.stream()
            .filter(distributorDataDO -> distributorDataDO.getErpFlag().equals(Constant.ERP_FLAG_1)).findFirst().get();
        List<OrderDistributorDataDO> treeNodeDistributorDataDO = distributorDataDOS.stream()
            .filter(distributorDataDO -> distributorDataDO.getTreeNode() > erpDistributorData.getTreeNode())
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(treeNodeDistributorDataDO)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_ERP_CHANGE_TREE_NODE_ERROR);
        }
        // 币种变更,不影响多级分销数据
        CurrencyRpcDO currency = orderRpcExe.getCurrencyByErpNo(cmd.getFSettleCurrId());
        if (!erpDistributorData.getCurrencyType().equals(currency.getCurrencyCode())) {
            CurrencyRateRpcDTO rateRpcDTO = null;
            List<Object> changes = new ArrayList<>();
            changes.add(erpDistributorData.getCurrencyType());
            changes.add(cmd.getFSettleCurrId());
            changeMap.put("修改币种", changes);
            // TODO 本位币默认为人民币 CNY 或者 RMB
            if (!currency.getCurrencyCode().equals("CNY") && !currency.getCurrencyCode().equals("RMB")) {
                rateRpcDTO = orderRpcExe.getCurrencyRate(currency.getCurrencyCode(), "CNY");
            }
            CurrencyRateRpcDTO finalRateRpcDTO = rateRpcDTO;
            distributorDataDOS.forEach(distributorDataDO -> {
                distributorDataDO.setCurrencyType(currency.getCurrencyCode());
                if (finalRateRpcDTO != null) {
                    distributorDataDO.setCurrentRates(finalRateRpcDTO.getExchangeRate());
                } else {
                    distributorDataDO.setCurrentRates(new BigDecimal(1));
                }
            });
            orderDistributorDataMapper.updateList(distributorDataDOS);
            for (OrderDistributorDataDO orderDistributorDataDO : distributorDataDOS) {
                if (orderDistributorDataDO.getOrderStatus() != null
                    && orderDistributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()
                    && orderDistributorDataDO.getTreeNode() == 1) {
                    sendService.orderConfirmMsg(orderDistributorDataDO.getOrderId());
                    break;
                }
            }
        }
        return erpDistributorData;
    }

    /**
     * 根据分销数据创建上级分销层数据
     *
     * @param distributorDataDO
     * @return
     */
    public OrderDistributorDataDO createOrderDistributorData(OrderInfoDO order,
        OrderDistributorDataDO distributorDataDO, DistributorRpcDTO distributor,
        OrderDistributorCostDO ancestorDistributorCost) {
        log.info("~~~~~~~~~~~distributor:{}", distributor);
        log.info("~~~~~~~~~~~ancestorDistributorCost:{}", ancestorDistributorCost);
        // 查询自己是否设置了付款方式
        Short payWay = null;
        if (ObjectUtil.isNotEmpty(distributor.getDistributionPayWay()) && distributor.getDistributionPayWay() != 0) {
            payWay = distributor.getDistributionPayWay();
        }

        OrderDistributorDataDO ancestorDistributorDataDO = OrderDataConvertor.toOrderDistributorData(order,
            distributorDataDO, distributor, ancestorDistributorCost, payWay);
        orderDistributorDataMapper.insert(ancestorDistributorDataDO);
        sendService.newOrderMsg(ancestorDistributorDataDO.getOrderId(), ancestorDistributorDataDO.getDistributorId());
        if (distributorDataDO.getOrderStatus() != null
            && distributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()
            && distributorDataDO.getTreeNode() == 1) {
            sendService.orderConfirmMsg(distributorDataDO.getOrderId());
        }
        return ancestorDistributorDataDO;
    }

    /**
     * 根据分销数据更新上级分销层数据收款状态和订单状态
     *
     * @return
     */
    public void updateOrderDistributorData(OrderDistributorDataDO distributorData,
        OrderDistributorDataDO ancestorDistributorDataDO) {
        log.info("~~~~~~~~~~~~~OrderDistributorDataDO:{}", distributorData);
        if (distributorData.getDistributionMode() == null
            || !distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_3)) {
            ancestorDistributorDataDO.setPayStatus(distributorData.getPayStatus());
            ancestorDistributorDataDO.setPayTime(distributorData.getPayTime());
            ancestorDistributorDataDO.setUpdateTime(new Date());
            ancestorDistributorDataDO.setOrderStatus(distributorData.getOrderStatus());
            ancestorDistributorDataDO.setPayWay(distributorData.getPayWay());
            orderDistributorDataMapper.updateByPrimaryKey(ancestorDistributorDataDO);
        }
    }

    /**
     * 更新订单分销层数据
     * 
     * @param distributorData
     */
    public void updateOrderDistributorData(OrderDistributorDataDO distributorData) {
        orderDistributorDataMapper.updateByPrimaryKey(distributorData);
    }

    /**
     * 更新订单付款状态
     *
     * @param cmd
     */
    public void orderPayStatus(OrderPayStatusCmd cmd) {
        OrderDistributorCostDO distributorCostDO =
            orderDistributorCostQryExe.getByOrderIdAndDistributorId(cmd.getOrderId(), cmd.getDistributorId());
        BigDecimal amount = distributorCostDO.getPayAmount().subtract(cmd.getRefundedAmount());
        if (distributorCostDO.getRefundedAmount() != null) {
            amount = amount.subtract(distributorCostDO.getRefundedAmount());
        }
        if (amount.compareTo(new BigDecimal(0)) > 0) {
            orderDistributorDataMapper.updateOrderPayStatus(cmd.getOrderId(), cmd.getDistributorId(),
                PayStatus.PARTIAL_REFUND.getValue());
        } else {
            orderDistributorDataMapper.updateOrderPayStatus(cmd.getOrderId(), cmd.getDistributorId(),
                cmd.getPayStatus());
        }
        if (cmd.getRefundedAmount() != null) {
            orderDistributorCostCmdExe.updateOrderRefundedAmount(cmd.getOrderId(), cmd.getDistributorId(),
                cmd.getRefundedAmount());
        }
    }

    public void updateList(List<OrderDistributorDataDO> distributorDataDOS) {
        orderDistributorDataMapper.updateList(distributorDataDOS);
    }

    public List<OrderDistributorDataDO> paymentMethod(List<Integer> orderIds, Integer distributorId) {
        List<OrderDistributorDataDO> distributorDataDOS =
            orderDistributorDataMapper.listByOrderIdsAndDistributorId(orderIds, distributorId);
        return distributorDataDOS;
    }

    public List<OrderDistributorDataDO> queryOrderDistributorDataByOrderId(Integer orderId, Integer treeNode) {
        return orderDistributorDataMapper.queryOrderDistributorDataByOrderId(orderId, treeNode);
    }

    public OrderDistributorDataDO findATier1DistributorOrderDistributorData(Integer primaryDistributorId,
        Integer orderId) {
        return orderDistributorDataMapper.findATier1DistributorOrderDistributorData(primaryDistributorId, orderId);
    }
}
