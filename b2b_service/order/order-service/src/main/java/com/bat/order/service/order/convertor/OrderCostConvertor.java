package com.bat.order.service.order.convertor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.order.dao.cost.dataobject.*;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.order.api.order.dto.common.OrderInfoParamDTO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.mq.dto.OrderRefundDTO;

public class OrderCostConvertor {

    /**
     * 设置order_goods_id
     * 
     * @param orderInfoParamDTO
     */
    public static void setOrderGoodsIdByCost(OrderInfoParamDTO orderInfoParamDTO) {
        List<OrderGoodsDO> orderGoodsDOList = orderInfoParamDTO.getOrderGoodsDOList();
        if (orderGoodsDOList == null || orderGoodsDOList.size() == 0) {
            return;
        }
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList =
            orderInfoParamDTO.getOrderGoodsDistributorCostDOList();
        if (orderGoodsDistributorCostDOList != null && orderGoodsDistributorCostDOList.size() > 0) {
            for (int x = 0; x < orderGoodsDistributorCostDOList.size(); x++) {
                orderGoodsDistributorCostDOList.get(x).setOrderGoodsId(orderGoodsDOList.get(x).getId());
            }
        }
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList =
            orderInfoParamDTO.getOrderGoodsCustomerCostDOList();
        if (orderGoodsCustomerCostDOList == null || orderGoodsCustomerCostDOList.size() == 0) {
            return;
        }
        for (int x = 0; x < orderGoodsCustomerCostDOList.size(); x++) {
            orderGoodsCustomerCostDOList.get(x).setOrderGoodsId(orderGoodsDOList.get(x).getId());
        }

    }

    public static void toOrderCancelRefundDTO(Map<Integer, OrderGoodsDO> orderGoodsDOMap,
                                              OrderCustomerDataDO customerDataDO, OrderCustomerCostDO orderCustomerCostDO,
                                              List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, List<OrderDistributorDataDO> distributorDataDOS,
                                              List<OrderDistributorCostDO> distributorCostDOS, List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS,
                                              List<OrderRefundDTO> refundDTOS, String remark, String operatorId, String operatorName,
                                              DistributorRpcDTO erpDistributor, List<String> couponNos) {
        Map<Integer, OrderDistributorCostDO> distributorCostDOMap = distributorCostDOS.stream().collect(
            Collectors.toMap(OrderDistributorCostDO::getDistributorId, distributorCostDO -> distributorCostDO));
        Map<Integer, List<OrderGoodsDistributorCostDO>> goodsDistributorCostDOSMap = goodsDistributorCostDOS.stream()
            .collect(Collectors.groupingBy(OrderGoodsDistributorCostDO::getDistributorId));
        if (customerDataDO != null) {
            // C端客户订单
            if (StringUtils.isNotBlank(orderCustomerCostDO.getOutTradeNo())) {
                customerDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                // 非零订单，原路返还（C端暂不支持快钱）
                OrderRefundDTO refundDTO = new OrderRefundDTO();
                refundDTOS.add(refundDTO);
                refundDTO.setReceiverType(Constant.RECEIVER_TYPE_2);
                refundDTO.setCustomerId(customerDataDO.getCustomerId());
                refundDTO.setCustomerName(customerDataDO.getCustomerName());
                refundDTO.setOrderId(customerDataDO.getOrderId());
                refundDTO.setBusinessTypes(Constant.REFUND_BUSINESS_TYPE_1);
                refundDTO.setOutTradeNo(orderCustomerCostDO.getOutTradeNo());
                final BigDecimal[] amount = {new BigDecimal(0)};
                orderGoodsCustomerCostDOS.forEach(orderGoodsCustomerCostDO -> {
                    OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
                    if (orderGoodsDO.getUnDeliverCount() != null && orderGoodsDO.getUnDeliverCount().intValue() > 0) {
                        amount[0] = amount[0].add(new BigDecimal(orderGoodsDO.getUnDeliverCount().intValue()
                            * orderGoodsCustomerCostDO.getActualPrice().doubleValue()));
                        if (!StringUtils.isNotBlank(orderGoodsCustomerCostDO.getCouponNo())
                            && (orderGoodsDO.getDeliverCount() == null || orderGoodsDO.getDeliverCount() == 0)) {
                            couponNos.add(orderGoodsCustomerCostDO.getCouponNo());
                        }
                    }
                });
                // 如果非整单取消情况，运费不退回
                if (amount[0].compareTo(
                    orderCustomerCostDO.getPayAmount().subtract(orderCustomerCostDO.getDistributionAmount())) == 0) {
                    amount[0] = amount[0].add(orderCustomerCostDO.getDistributionAmount());
                }
                refundDTO.setDepositAmount(new BigDecimal(0));
                refundDTO.setCashAmount(
                    amount[0].setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
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
            }
            // 非分销商自己收款情况需处理C端客户归属分销商佣金，发货时产生佣金
            // if (!customerDataDO.getCustomerMode().equals(CUSTOMER_MODE_3)
            // && !CollectionUtils.isEmpty(distributorDataDOS)) {
            // OrderDistributorDataDO distributorDataDO = distributorDataDOS.stream()
            // .filter(dataDO -> dataDO.getDistributorId().equals(customerDataDO.getDistributorId())).findFirst()
            // .get();
            // toOrderCancelCommissionDTO(distributorDataDO, orderGoodsCustomerCostDOS, orderGoodsDOMap,
            // goodsDistributorCostDOSMap, commissionDOS);
            // }
        }
        // 分销订单处理退款
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            distributorDataDOS.forEach(distributorDataDO -> {
                OrderDistributorCostDO distributorCostDO =
                    distributorCostDOMap.get(distributorDataDO.getDistributorId());
                List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOS =
                    goodsDistributorCostDOSMap.get(distributorDataDO.getDistributorId());
                // 已付款（注意：有可能平台收款）、且已付金额不为空情况需生成退款申请数据
                if (distributorDataDO.getPayStatus().equals(PayStatus.PAID.getValue())) {
                    if ((distributorCostDO.getPaidAmount() != null
                        && distributorCostDO.getPaidAmount().doubleValue() > 0)
                        || (distributorCostDO.getDepositAmount() != null
                            && distributorCostDO.getDepositAmount().doubleValue() > 0)) {
                        // 分销层生成退款申请数据
                        distributorDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                        toOrderCancelRefundDTO(orderGoodsDOMap, distributorDataDO, distributorCostDO,
                            orderGoodsDistributorCostDOS, refundDTOS, remark, operatorId, operatorName, erpDistributor);
                    } else {
                        distributorDataDO.setPayStatus(PayStatus.REFUND.getValue());
                        orderGoodsDOMap.values().forEach(orderGoodsDO -> {
                            if (orderGoodsDO.getDeliverCount() != null && orderGoodsDO.getDeliverCount() > 0) {
                                distributorDataDO.setPayStatus(PayStatus.PARTIAL_REFUND.getValue());
                            }
                        });
                    }
                }
                // 平台收款或上级分销商收款情况需处理佣金（发货时产生佣金）
                // if (distributorDataDO.getTreeNode() > 1
                // && !distributorDataDO.getDistributionMode().equals(DISTRIBUTION_MODE_3)) {
                // // 1级分销商情况，非直接下单，且非1级分销商付款情况
                // OrderDistributorDataDO outTradeNoDistributorDataDO = null;
                // if (StringUtils.isNotBlank(distributorCostDO.getOutTradeNo())
                // && distributorDataDO.getTreeNode() == 2
                // && distributorDataDO.getDistributionMode().equals(DISTRIBUTION_MODE_1)) {
                // // 获取支付凭证归属分销商
                // List<Integer> distributorIds = distributorCostDOS.stream()
                // .filter(costDO -> StringUtils.isNotBlank(costDO.getOutTradeNo())
                // && costDO.getOutTradeNo().equals(distributorCostDO.getOutTradeNo()))
                // .map(OrderDistributorCostDO::getDistributorId).collect(Collectors.toList());
                // outTradeNoDistributorDataDO = distributorDataDOS.stream()
                // .filter(dataDO -> distributorIds.contains(dataDO.getDistributorId()))
                // .max(Comparator.comparing(OrderDistributorDataDO::getTreeNode)).get();
                // }
                // toOrderCancelCommissionDTO(outTradeNoDistributorDataDO, distributorDataDO, orderGoodsDOMap,
                // goodsDistributorCostDOSMap, commissionDOS);
                // }
            });
        }
    }

    public static void toOrderCancelCommissionDTO(OrderDistributorDataDO distributorDataDO,
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS, Map<Integer, OrderGoodsDO> orderGoodsDOMap,
        Map<Integer, List<OrderGoodsDistributorCostDO>> goodsDistributorCostDOSMap,
        List<OrderDistributorCommissionDO> commissionDOS) {
        List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS =
            goodsDistributorCostDOSMap.get(distributorDataDO.getDistributorId());
        Map<Integer, OrderGoodsDistributorCostDO> goodsDistributorCostDOMap =
            goodsDistributorCostDOS.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                goodsDistributorCostDO -> goodsDistributorCostDO));
        OrderDistributorCommissionDO commissionDO = new OrderDistributorCommissionDO();
        commissionDO.setOrderId(distributorDataDO.getOrderId());
        commissionDO.setDistributorAncestorId(distributorDataDO.getDistributorAncestorId());
        commissionDO.setDistributorDescendantId(distributorDataDO.getDistributorId());
        AtomicReference<BigDecimal> orderAncestorPrice = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> orderDescendantPrice = new AtomicReference<>(new BigDecimal(0));
        orderGoodsCustomerCostDOS.forEach(orderGoodsCustomerCostDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
            OrderGoodsDistributorCostDO goodsDistributorCostDO =
                goodsDistributorCostDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
            if (orderGoodsDO.getUnDeliverCount() != null && orderGoodsDO.getUnDeliverCount().intValue() > 0) {
                orderDescendantPrice.set(orderDescendantPrice.get()
                    .add(new BigDecimal(orderGoodsDO.getUnDeliverCount().intValue()
                        * orderGoodsCustomerCostDO.getActualPrice().doubleValue()).setScale(2,
                            BigDecimal.ROUND_HALF_UP)));
                orderAncestorPrice.set(orderAncestorPrice.get()
                    .add(new BigDecimal(orderGoodsDO.getUnDeliverCount().intValue()
                        * goodsDistributorCostDO.getActualPrice().doubleValue()).setScale(2,
                            BigDecimal.ROUND_HALF_UP)));
            }
        });
        commissionDO.setOrderAncestorPrice(orderAncestorPrice.get());
        commissionDO.setOrderDescendantPrice(orderDescendantPrice.get());
        BigDecimal amount = orderDescendantPrice.get().subtract(orderAncestorPrice.get());
        if (amount.doubleValue() != 0) {
            if (amount.doubleValue() < 0) {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_2);
            } else {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_1);
            }
            commissionDO.setAmount(amount.abs());
            commissionDOS.add(commissionDO);
        }
    }

    public static void toOrderCancelCommissionDTO(OrderDistributorDataDO outTradeNoDistributorDataDO,
        OrderDistributorDataDO distributorDataDO, Map<Integer, OrderGoodsDO> orderGoodsDOMap,
        Map<Integer, List<OrderGoodsDistributorCostDO>> goodsDistributorCostDOSMap,
        List<OrderDistributorCommissionDO> commissionDOS) {
        // 分销商订单明细费用 平台收款 1级分销商分佣需考虑下级分销分佣代收情况
        List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS = new ArrayList<>();
        if (outTradeNoDistributorDataDO != null) {
            goodsDistributorCostDOS
                .addAll(goodsDistributorCostDOSMap.get(outTradeNoDistributorDataDO.getDistributorId()));
        } else {
            goodsDistributorCostDOS.addAll(goodsDistributorCostDOSMap.get(distributorDataDO.getDistributorId()));
        }
        // 上级分销商订单明细费用
        List<OrderGoodsDistributorCostDO> upGoodsDistributorCostDOS =
            goodsDistributorCostDOSMap.get(distributorDataDO.getDistributorAncestorId());
        Map<Integer, OrderGoodsDistributorCostDO> upGoodsDistributorCostDOMap =
            upGoodsDistributorCostDOS.stream().collect(Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId,
                upGoodsDistributorCostDO -> upGoodsDistributorCostDO));
        OrderDistributorCommissionDO commissionDO = new OrderDistributorCommissionDO();
        commissionDO.setOrderId(distributorDataDO.getOrderId());
        commissionDO.setDistributorAncestorId(distributorDataDO.getDistributorAncestorId());
        commissionDO.setDistributorDescendantId(distributorDataDO.getDistributorId());
        AtomicReference<BigDecimal> orderAncestorPrice = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> orderDescendantPrice = new AtomicReference<>(new BigDecimal(0));
        goodsDistributorCostDOS.forEach(goodsDistributorCostDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(goodsDistributorCostDO.getOrderGoodsId());
            OrderGoodsDistributorCostDO upGoodsDistributorCostDO =
                upGoodsDistributorCostDOMap.get(goodsDistributorCostDO.getOrderGoodsId());
            if (orderGoodsDO.getUnDeliverCount() != null && orderGoodsDO.getUnDeliverCount().intValue() > 0) {
                orderDescendantPrice.set(orderDescendantPrice.get()
                    .add(new BigDecimal(orderGoodsDO.getUnDeliverCount().intValue()
                        * goodsDistributorCostDO.getActualPrice().doubleValue()).setScale(2,
                            BigDecimal.ROUND_HALF_UP)));
                orderAncestorPrice.set(orderAncestorPrice.get()
                    .add(new BigDecimal(orderGoodsDO.getUnDeliverCount().intValue()
                        * upGoodsDistributorCostDO.getActualPrice().doubleValue()).setScale(2,
                            BigDecimal.ROUND_HALF_UP)));
            }
        });
        commissionDO.setOrderAncestorPrice(orderAncestorPrice.get());
        commissionDO.setOrderDescendantPrice(orderDescendantPrice.get());
        BigDecimal amount = orderDescendantPrice.get().subtract(orderAncestorPrice.get());
        if (amount.doubleValue() != 0) {
            if (amount.doubleValue() < 0) {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_2);
            } else {
                commissionDO.setCommissionType(Constant.COMMISSION_TYPE_1);
            }
            commissionDO.setAmount(amount.abs());
            commissionDOS.add(commissionDO);
        }
    }

    public static void toOrderCancelRefundDTO(Map<Integer, OrderGoodsDO> orderGoodsDOMap,
        OrderDistributorDataDO distributorDataDO, OrderDistributorCostDO distributorCostDO,
        List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS, List<OrderRefundDTO> refundDTOS, String remark,
        String operatorId, String operatorName, DistributorRpcDTO erpDistributor) {
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTOS.add(refundDTO);
        refundDTO.setReceiverType(Constant.RECEIVER_TYPE_1);
        refundDTO.setDistributorId(distributorDataDO.getDistributorId());
        refundDTO.setDistributorName(distributorDataDO.getDistributorName());
        refundDTO.setCompanyName(distributorDataDO.getCompanyName());
        refundDTO.setDistributorRefundId(distributorDataDO.getDistributorAncestorId());
        refundDTO.setOrderId(distributorDataDO.getOrderId());
        refundDTO.setBusinessTypes(Constant.REFUND_BUSINESS_TYPE_1);
        refundDTO.setOutTradeNo(distributorCostDO.getOutTradeNo());
        final BigDecimal[] amount = {new BigDecimal(0)};
        goodsDistributorCostDOS.forEach(goodsDistributorCostDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(goodsDistributorCostDO.getOrderGoodsId());
            if (orderGoodsDO.getUnDeliverCount() != null && orderGoodsDO.getUnDeliverCount().intValue() > 0) {
                amount[0] = amount[0].add(new BigDecimal(
                    orderGoodsDO.getUnDeliverCount().intValue() * goodsDistributorCostDO.getActualPrice().doubleValue())
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        });
        // 如果非整单取消情况，运费不退回
        if (amount[0]
            .compareTo(distributorCostDO.getPayAmount().subtract(distributorCostDO.getDistributionAmount())) == 0) {
            amount[0] = amount[0].add(distributorCostDO.getDistributionAmount());
        }
        // 分销层数据可能存在混合退款方式
        if (distributorCostDO.getDepositAmount() != null && distributorCostDO.getDepositAmount().doubleValue() > 0) {
            if (distributorCostDO.getDepositAmount().compareTo(amount[0]) > 0) {
                refundDTO.setDepositAmount(
                    amount[0].setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                amount[0] = new BigDecimal(0);
            } else {
                refundDTO.setDepositAmount(distributorCostDO.getDepositAmount());
                amount[0] = amount[0].subtract(distributorCostDO.getDepositAmount());
            }
        }
        refundDTO.setCashAmount(amount[0].setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
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
        } else if (StringUtils.isNotBlank(distributorCostDO.getOutTradeNo())
            && !distributorDataDO.getPayWay().equals(PayWay.Kuaiqian_payment.getValue())
            && !distributorDataDO.getPayWay().equals(PayWay.Balance_Kuaiqian_payment.getValue())) {
            refundDTO.setRefundType(Constant.REFUND_TYPE_1);
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
}
