package com.bat.order.service.data.convertor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.OrderType;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.order.api.data.dto.data.OrderExtendDataDTO;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 23:20
 */
@Slf4j
public class OrderDataConvertor {
    public static OrderExtendDataDTO toOrderExtendDataDTO(OrderExtendDataDO orderExtendDataDO) {
        if (orderExtendDataDO != null) {
            OrderExtendDataDTO dto = new OrderExtendDataDTO();
            org.springframework.beans.BeanUtils.copyProperties(orderExtendDataDO, dto);
            return dto;
        }
        return null;
    }

    public static void orderPayCustomer(Date padTime, List<OrderCustomerCostDO> customerCostDOS,
        List<OrderCustomerDataDO> customerDataDOS) {
        Map<Integer, OrderCustomerCostDO> customerCostDOMap = customerCostDOS.stream()
            .collect(Collectors.toMap(OrderCustomerCostDO::getOrderId, customerCostDO -> customerCostDO));
        customerDataDOS.forEach(customerDataDO -> {
            OrderCustomerCostDO orderCustomerCostDO = customerCostDOMap.get(customerDataDO.getOrderId());
            customerDataDO.setPayTime(padTime);
            customerDataDO.setUpdateTime(padTime);
            BigDecimal payAmount = orderCustomerCostDO.getPayAmount();
            if (payAmount != null && orderCustomerCostDO.getPaidAmount() != null) {
                payAmount = payAmount.subtract(orderCustomerCostDO.getPaidAmount());
            }
            if (payAmount == null || payAmount.doubleValue() == 0) {
                customerDataDO.setPayStatus(PayStatus.PAID.getValue());
            } else {
                customerDataDO.setPayStatus(PayStatus.PARTIAL_PAYMENT.getValue());
            }
            customerDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
        });
    }

    public static void orderPayDistributor(Date padTime, List<OrderDistributorCostDO> distributorCostDOS,
        List<OrderDistributorDataDO> distributorDataDOS) {
        Map<Integer, OrderDistributorCostDO> distributorCostDOMap = distributorCostDOS.stream()
            .collect(Collectors.toMap(OrderDistributorCostDO::getOrderId, distributorCostDO -> distributorCostDO));
        distributorDataDOS.forEach(distributorDataDO -> {
            OrderDistributorCostDO distributorCostDO = distributorCostDOMap.get(distributorDataDO.getOrderId());
            distributorCostDO.setDistributorDataDO(distributorDataDO);
            distributorDataDO.setPayTime(padTime);
            distributorDataDO.setUpdateTime(padTime);
            BigDecimal payAmount = distributorCostDO.getPayAmount();
            if (payAmount != null && distributorCostDO.getDepositAmount() != null) {
                payAmount = payAmount.subtract(distributorCostDO.getDepositAmount());
            }
            if (payAmount != null && distributorCostDO.getPaidAmount() != null) {
                payAmount = payAmount.subtract(distributorCostDO.getPaidAmount());
            }
            if (payAmount == null || payAmount.doubleValue() == 0) {
                distributorDataDO.setPayStatus(PayStatus.PAID.getValue());
            } else {
                distributorDataDO.setPayStatus(PayStatus.PARTIAL_PAYMENT.getValue());
            }
            distributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
        });
    }

    public static OrderDistributorDataDO toOrderDistributorData(OrderInfoDO order, OrderCustomerDataDO customerDataDO,
        DistributorRpcDTO distributor, OrderDistributorCostDO distributorCost) {
        OrderDistributorDataDO distributorDataDO = new OrderDistributorDataDO();
        distributorDataDO.setOrderId(customerDataDO.getOrderId());
        distributorDataDO.setDistributorId(distributor.getId());
        distributorDataDO.setDistributorName(distributor.getName());
        distributorDataDO.setCompanyName(distributor.getCompanyName());
        distributorDataDO.setDistributorAncestorId(distributor.getDistributorAncestorId());
        distributorDataDO.setDistributorAncestorName(distributor.getDistributorAncestorName());
        distributorDataDO.setDistributorAncestorCompanyName(distributor.getDistributorAncestorCompanyName());
        distributorDataDO.setDirectFlag(Constant.DIRECT_FLAG_0);
        distributorDataDO.setErpFlag(distributor.getErpFlag());
        distributorDataDO.setTreeNode(distributor.getTreeNode());
        distributorDataDO.setDistributionMode(distributor.getAncestorDistributionMode());
        // 结算方式类型，1为立即支付，2为期间结算
        if (distributor.getPayWay().equals(Constant.PAY_TYPE_1)
            || (distributor.getCustomerMode() == null || !distributor.getCustomerMode().equals(Constant.CUSTOMER_MODE_3))) {
            distributorDataDO.setPayWay(customerDataDO.getPayWay());
        } else {
            distributorDataDO.setPayWay(PayWay.Period_settlement.getValue());
        }
        if ((distributor.getCustomerMode() == null || !distributor.getCustomerMode().equals(Constant.CUSTOMER_MODE_3))) {
            distributorDataDO.setPayStatus(customerDataDO.getPayStatus());
            distributorDataDO.setPayTime(customerDataDO.getPayTime());
        } else {
            distributorDataDO.setPayStatus(PayStatus.UNPAID.getValue());
        }
        // 如果是0元订单默认已支付
        if (distributorCost.getPayAmount().doubleValue() == 0) {
            distributorDataDO.setPayStatus(PayStatus.PAID.getValue());
        }
        // 同步erp情况
        // 期间结算：在库订单和定制订单为已确认、在途订单和MTO订单为待确认
        // 现款线上支付（支付宝、微信、快钱、余额支付）订单：未支付情况统一为待确认
        // 现款线下订单：在库订单为已确认。定制订单、在途订单和MTO订单为待确认
        distributorDataDO.setOrderStatus(OrderStatus.PENDING.getValue());
        if (distributor.getErpFlag().equals(Constant.ERP_FLAG_1)) {
            if (order.getStockType().equals(OrderType.IN.getValue())
                && (distributorDataDO.getPayWay().equals(PayWay.Period_settlement.getValue())
                    || distributorDataDO.getPayWay().equals(PayWay.Offline_payment.getValue())
                    || !distributorDataDO.getPayStatus().equals(PayStatus.UNPAID.getValue()))) {
                distributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
            } else if (order.getStockType().equals(OrderType.DIY.getValue())
                && (distributorDataDO.getPayWay().equals(PayWay.Period_settlement.getValue())
                    || !distributorDataDO.getPayStatus().equals(PayStatus.UNPAID.getValue()))) {
                distributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
            }
        } else {
            if (distributor.getTreeNode() > 1 && distributor.getAncestorDistributionAutoFlag().equals(Constant.AUTO_FLAG_1)) {
                distributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
            } else {
                distributorDataDO.setOrderStatus(OrderStatus.PENDING.getValue());
            }
        }
        distributorDataDO.setCurrencyType("CNY");
        distributorDataDO.setCurrentRates(new BigDecimal(1));
        distributorDataDO.setRemark(customerDataDO.getRemark());
        Date date = new Date(System.currentTimeMillis());
        distributorDataDO.setCreateTime(date);
        distributorDataDO.setUpdateTime(date);
        return distributorDataDO;
    }

    public static OrderDistributorDataDO toOrderDistributorData(OrderInfoDO order,
        OrderDistributorDataDO distributorDataDO, DistributorRpcDTO distributor,
        OrderDistributorCostDO ancestorDistributorCost, Short payWay) {
        OrderDistributorDataDO ancestorDistributorDataDO = new OrderDistributorDataDO();
        ancestorDistributorDataDO.setOrderId(distributorDataDO.getOrderId());
        ancestorDistributorDataDO.setDistributorId(distributor.getId());
        ancestorDistributorDataDO.setDistributorName(distributor.getName());
        ancestorDistributorDataDO.setCompanyName(distributor.getCompanyName());
        ancestorDistributorDataDO.setDistributorAncestorId(distributor.getDistributorAncestorId());
        ancestorDistributorDataDO.setDistributorAncestorName(distributor.getDistributorAncestorName());
        ancestorDistributorDataDO.setDistributorAncestorCompanyName(distributor.getDistributorAncestorCompanyName());
        ancestorDistributorDataDO.setDirectFlag(Constant.DIRECT_FLAG_0);
        ancestorDistributorDataDO.setTreeNode(distributor.getTreeNode());

        // 如果上级收款是平台收款，则当前这级分层也是平台收款
        if (distributorDataDO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)) {
            ancestorDistributorDataDO.setDistributionMode(Constant.DISTRIBUTION_MODE_1);
        } else {
            // 否则拿上级收款方式
            ancestorDistributorDataDO.setDistributionMode(distributor.getAncestorDistributionMode());
        }
        // 上级分销模式不为自己收款情况及包含上级收款和平台首款情况，上级订单付款状态为本级订单付款状态。否则为未付款状态。
        if (distributorDataDO.getDistributionMode() == null
            || !distributorDataDO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_3)) {
            ancestorDistributorDataDO.setPayStatus(distributorDataDO.getPayStatus());
            ancestorDistributorDataDO.setPayTime(distributorDataDO.getPayTime());
        } else {
            ancestorDistributorDataDO.setPayStatus(PayStatus.UNPAID.getValue());
        }
        // 结算方式类型，1为立即支付，2为期间结算
        // 结算方式为立即支付或已付款状态情况，付款方式默认为本级订单的付款方式
        // 如果自己设置了付款方式就按照自己设置的来
        if (ObjectUtil.isNotEmpty(payWay)) {
            ancestorDistributorDataDO.setPayWay(payWay);
        } else {
            if (distributor.getPayWay() == null || distributor.getPayWay().equals(Constant.PAY_TYPE_1)
                || ancestorDistributorDataDO.getPayStatus().equals(PayStatus.PAID.getValue())) {
                // 查询分销商的id
                ancestorDistributorDataDO.setPayWay(distributorDataDO.getPayWay());
            } else {
                ancestorDistributorDataDO.setPayWay(PayWay.Period_settlement.getValue());
            }
        }
        // 如果是0元订单默认已支付
        if (ancestorDistributorCost.getPayAmount().doubleValue() == 0) {
            distributorDataDO.setPayStatus(PayStatus.PAID.getValue());
        }
        // 如果分销数据已经同步erp，上级分销层数据不再同步erp,且订单状态与同步erp层数据同步（注意无需上级分销审核）
        ancestorDistributorDataDO.setOrderStatus(OrderStatus.PENDING.getValue());
        ancestorDistributorDataDO.setErpFlag(distributor.getErpFlag());
        if (distributorDataDO.getErpFlag().equals(Constant.ERP_FLAG_1)) {
            ancestorDistributorDataDO.setErpFlag(Constant.ERP_FLAG_0);
            ancestorDistributorDataDO.setOrderStatus(distributorDataDO.getOrderStatus());
        } else {
            // 同步erp情况
            // 期间结算：在库订单和定制订单为已确认、在途订单和MTO订单为待确认
            // 现款线上支付（支付宝、微信、快钱、余额支付）订单：未支付情况统一为待确认
            // 现款线下订单：在库订单为已确认。定制订单、在途订单和MTO订单为待确认
            if (distributor.getErpFlag().equals(Constant.ERP_FLAG_1)) {
                if (order.getStockType().equals(OrderType.IN.getValue())
                    && (ancestorDistributorDataDO.getPayWay().equals(PayWay.Period_settlement.getValue())
                        || ancestorDistributorDataDO.getPayWay().equals(PayWay.Offline_payment.getValue())
                        || !ancestorDistributorDataDO.getPayStatus().equals(PayStatus.UNPAID.getValue()))) {
                    ancestorDistributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                } else if (order.getStockType().equals(OrderType.DIY.getValue())
                    && (ancestorDistributorDataDO.getPayWay().equals(PayWay.Period_settlement.getValue())
                        || !ancestorDistributorDataDO.getPayStatus().equals(PayStatus.UNPAID.getValue()))) {
                    ancestorDistributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                }
            } else {
                if ((distributor.getTreeNode() > 1
                    && distributor.getAncestorDistributionAutoFlag().equals(Constant.AUTO_FLAG_1))) {
                    ancestorDistributorDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                } else {
                    ancestorDistributorDataDO.setOrderStatus(OrderStatus.PENDING.getValue());
                }
            }
        }
        // 币种和汇率为本级订单的币种和汇率
        ancestorDistributorDataDO.setCurrencyType(distributorDataDO.getCurrencyType());
        ancestorDistributorDataDO.setCurrentRates(distributorDataDO.getCurrentRates());
        ancestorDistributorDataDO.setRemark(distributorDataDO.getRemark());
        Date date = new Date(System.currentTimeMillis());
        ancestorDistributorDataDO.setCreateTime(date);
        ancestorDistributorDataDO.setUpdateTime(date);
        return ancestorDistributorDataDO;
    }
}
