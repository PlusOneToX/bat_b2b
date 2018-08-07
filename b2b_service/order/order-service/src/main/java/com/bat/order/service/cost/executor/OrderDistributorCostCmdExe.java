package com.bat.order.service.cost.executor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.ConfigQry;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;
import com.bat.order.service.message.MessageConvertor;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.executor.OrderRpcExe;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.cost.OrderDistributorCommissionMapper;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCommissionDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.mq.dto.OrderPayDTO;
import com.bat.order.mq.dto.OrderRefundDTO;
import com.bat.order.mq.dto.OrderVoucherDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderDistributorCostCmdExe {
    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;
    @Resource
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostMapper;
    @Resource
    private OrderDistributorDataCmdExe distributorDataCmdExe;
    @Resource
    private MessageSendService sendService;
    @Resource
    private OrderDistributorCommissionMapper orderDistributorCommissionMapper;
    @Resource
    private ConfigQry configQry;
    @Resource
    private OrderRpcExe orderRpcExe;
    @Resource
    private CommonRpcQryExe commonRpcQryExe;

    @Resource
    private OrderDistributorDataMapper orderDistributorDataMapper;

    @Transactional(rollbackFor = Exception.class)
    public void create(OrderDistributorCostDO orderDistributorCostDO) {
        orderDistributorCostDO.setCreateTime(new Date());
        orderDistributorCostDO.setUpdateTime(new Date());
        orderDistributorCostMapper.insert(orderDistributorCostDO);
    }

    /**
     * 分销订单支付（支持合并支付）
     * 
     * @param cmd
     * @return
     */
    public void orderPay(OrderPayDTO cmd) {
        List<OrderDistributorCostDO> distributorCostDOS =
            orderDistributorCostMapper.listByOrderIdsAndDistributorId(cmd.getOrderIds(), cmd.getDistributorId());

        // 查询order_distributor_data
        List<OrderDistributorDataDO> orderDistributorDataDOS =
            distributorDataCmdExe.paymentMethod(cmd.getOrderIds(), cmd.getDistributorId());

        // 查询订单直属分销商信息表
        OrderDistributorDataDO distributorData = null;
        DistributorExtendDataRpcDTO extendDataRpcDTO = null;
        // List<OrderDistributorDataDO> itemList = null;

        if (!CollectionUtils.isEmpty(orderDistributorDataDOS)) {
            distributorData = orderDistributorDataDOS.get(0);

            // 如果层级大于1且当前付款分销商是平台收款，则需要将他的所有上级订单状态跟付款状态都改了
            // if (distributorData.getTreeNode() > 1
            // && distributorData.getDistributionMode().equals(DISTRIBUTION_MODE_1)) {
            //
            // itemList = distributorDataCmdExe.queryOrderDistributorDataByOrderId(distributorData.getOrderId(),
            // distributorData.getTreeNode());
            // }

            // 当订单分销收款方式为上级收款情况，还需找到订单分销上级收款方式
            if (distributorData.getDistributionMode() != null
                && distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)) {
                extendDataRpcDTO =
                    orderRpcExe.getAncestorDistributorExtendData(distributorData.getDistributorAncestorId());
            }
            // 分销商收款情况(一直找到最后一个收款对象)上级分销商收款情况继续找上级分销收款方式
            while (extendDataRpcDTO != null && extendDataRpcDTO.getDistributionMode() != null
                && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)) {
                extendDataRpcDTO = orderRpcExe.getAncestorDistributorExtendData(extendDataRpcDTO.getDistributorId());
            }
        }
        // 修改order_distributor_cost
        OrderCostConvertor.orderPayDistributor(distributorCostDOS, cmd, distributorData, extendDataRpcDTO);
        orderDistributorCostMapper.updateList(distributorCostDOS);
        // 更新明细费用平台金额
        if (ObjectUtils.isNotEmpty(distributorData) && distributorData.getTreeNode() > 1
            && distributorData.getDistributionMode() != null
            && (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
                || (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                    && extendDataRpcDTO.getDistributionMode() != null
                    && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))) {

            // 查询order_goods_distributor_cost
            List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOS = orderGoodsDistributorCostMapper
                .orderGoodsDistributorCostByOrderIdsAndDistributorId(cmd.getOrderIds(), cmd.getDistributorId());

            orderGoodsDistributorCostDOS.forEach(x -> {
                x.setPlatformPrice(x.getActualPrice());
            });
            // 修改order_goods_distributor_cost
            orderGoodsDistributorCostMapper.updateList(orderGoodsDistributorCostDOS);
        }

        // 修改order_distributor_data
        distributorDataCmdExe.orderPay(cmd, distributorCostDOS, orderDistributorDataDOS);

        // 同步erp订单层数据需同步erp，且生成收款单
        OrderDistributorCostDO orderDistributorCostDO = distributorCostDOS.get(0);
        Short erpFlag = distributorCostDOS.get(0).getDistributorDataDO().getErpFlag();
        if (erpFlag != null && erpFlag.equals(Constant.ERP_FLAG_1)
            && StringUtils.isNotBlank(orderDistributorCostDO.getOutTradeNo())) {
            // 发送生成收款单消息
            OrderVoucherDTO voucherDTO =
                MessageConvertor.toOrderVoucherDTO(orderDistributorCostDO, cmd.getCounterpartyType());
            sendService.orderVoucher(voucherDTO);
        }
    }

    /**
     * 根据C端客户订单生产分销层费用
     * 
     * @param orderCustomerCost
     * @param orderGoodsDistributorCosts
     * @param orderGoodsDOS
     * @param distributor
     * @return
     */
    public OrderDistributorCostDO createOrderDistributorCost(OrderDeliveryDO orderDelivery,
        OrderCustomerCostDO orderCustomerCost, List<OrderGoodsDistributorCostDO> orderGoodsDistributorCosts,
        List<OrderGoodsDO> orderGoodsDOS, DistributorRpcDTO distributor,
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS) {
        OrderDistributorCostDO distributorCostDO = OrderCostConvertor.toOrderDistributorCostDO(orderCustomerCost,
            distributor, orderGoodsDOS, orderGoodsDistributorCosts);
        AtomicBoolean mailFlag = new AtomicBoolean(false);
        if (!CollectionUtils.isEmpty(exchangeCodeDOS)) {
            exchangeCodeDOS.forEach(exchangeCodeDO -> {
                if (exchangeCodeDO.getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                    mailFlag.set(true);
                }
            });
        }
        // 计算分销层运费(注意：包邮兑换卡不收取邮费)
        if (mailFlag.get()) {
            distributorCostDO.setDistributionAmount(new BigDecimal(0));
        } else {
            LogisticsCalculationRpcDTO logisticsCalculation = orderRpcExe.getLogisticsCalculation(
                OrderCostConvertor.toLogisticsCalculationRpcQry(orderDelivery.getDistributionId(), orderDelivery,
                    commonRpcQryExe.calcDiyWeight(orderGoodsDOS), distributorCostDO));
            if (logisticsCalculation != null) {
                distributorCostDO.setDistributionAmount(logisticsCalculation.getCost());
            } else {
                distributorCostDO.setDistributionAmount(new BigDecimal(0));
            }
        }
        // 计算应付款金额
        BigDecimal actualAmount =
            distributorCostDO.getGoodsAmount().subtract(distributorCostDO.getGoodsPromotionAmount())
                .subtract(distributorCostDO.getOrderPromotionAmount());
        if (actualAmount.doubleValue() > 0) {
            distributorCostDO.setPayAmount(actualAmount.add(distributorCostDO.getDistributionAmount()));
        } else {
            distributorCostDO.setPayAmount(distributorCostDO.getDistributionAmount());
        }
        orderDistributorCostMapper.insert(distributorCostDO);
        return distributorCostDO;
    }

    /**
     * 根据分销层订单生产上级分销层费用
     *
     * @param distributorCost
     * @param orderGoodsDistributorCosts
     * @param orderGoodsDOS
     * @return
     */
    public OrderDistributorCostDO createOrderDistributorCost(OrderDistributorCostDO distributorCost,
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCosts, List<OrderGoodsDO> orderGoodsDOS,
        OrderDistributorDataDO distributorData, DistributorExtendDataRpcDTO extendDataRpcDTO) {
        OrderDistributorCostDO ancestorDistributorCost = OrderCostConvertor.toOrderDistributorCostDO(distributorCost,
            distributorData, orderGoodsDOS, orderGoodsDistributorCosts, extendDataRpcDTO);
        orderDistributorCostMapper.insert(ancestorDistributorCost);
        return ancestorDistributorCost;
    }

    /**
     * 根据分销层订单更新上级分销层平台收款金额
     *
     * @param distributorCost
     * @return
     */
    public void updateOrderDistributorCost(OrderDistributorCostDO distributorCost,
        OrderDistributorCostDO ancestorDistributorCost) {
        ancestorDistributorCost.setPlatformAmount(distributorCost.getPayAmount());
        ancestorDistributorCost.setOutTradeNo(distributorCost.getOutTradeNo());
        ancestorDistributorCost.setUpdateTime(new Date());
        orderDistributorCostMapper.updateByPrimaryKey(ancestorDistributorCost);
    }

    /**
     * C端客户归属分销商分佣计算
     * 
     * @param distributor
     * @param orderCustomerCost
     * @param distributorCost
     */
    public void createOrderDistributorCommission(DistributorRpcDTO distributor, OrderCustomerCostDO orderCustomerCost,
        OrderDistributorCostDO distributorCost) {
        OrderDistributorCommissionDO commissionDO =
            OrderCostConvertor.toOrderDistributorCommissionDO(distributor, orderCustomerCost, distributorCost);
        if (commissionDO != null) {
            orderDistributorCommissionMapper.insert(commissionDO);
            // 发送分佣消息
            sendService.orderCommission(commissionDO);
        }
    }

    /**
     * ERP订单明细变更重新计算订单费用
     * 
     * @param distributorCostDOS
     */
    public OrderRefundDTO orderChangeByErp(ErpOrderChangeCmd cmd, DistributorRpcDTO distributor,
        List<OrderGoodsDistributorCostDO> distributorCostDOS, OrderDistributorDataDO erpDistributorData,
        Map<String, List<Object>> changeMap) {
        OrderConfig orderConfig = configQry.getTenantErpConfig();
        OrderRefundDTO refundDTO = null;
        List<ErpOrderChangeDetailCmd> orderDetails = cmd.getOrderDetails();
        Optional<ErpOrderChangeDetailCmd> optional = orderDetails.stream()
            .filter(orderDetail -> orderDetail.getItemNo().equals(orderConfig.getWlfItemNo())).findFirst();
        if (!CollectionUtils.isEmpty(distributorCostDOS) || (optional != null && optional.isPresent())) {
            OrderDistributorCostDO orderDistributorCostDO = orderDistributorCostMapper
                .getByOrderIdAndDistributorId(erpDistributorData.getOrderId(), erpDistributorData.getDistributorId());
            // 修改运费
            if (optional != null && optional.isPresent()) {
                ErpOrderChangeDetailCmd orderChangeDetailCmd = optional.get();
                BigDecimal distributionAmount = orderDistributorCostDO.getDistributionAmount();
                if (distributionAmount == null) {
                    distributionAmount = new BigDecimal(0);
                }
                // A 增加 B 修改 D 删除
                List<Object> changes = new ArrayList<>();
                if (orderChangeDetailCmd.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_A)) {
                    changes.add(0);
                    changes.add(orderChangeDetailCmd.getItemTaxPrice().doubleValue());
                    orderDistributorCostDO.setDistributionAmount(orderChangeDetailCmd.getItemTaxPrice());
                } else if (orderChangeDetailCmd.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_B)) {
                    changes.add(distributionAmount.doubleValue());
                    changes.add(orderChangeDetailCmd.getItemTaxPrice().doubleValue());
                    orderDistributorCostDO.setDistributionAmount(orderChangeDetailCmd.getItemTaxPrice());
                } else if (orderChangeDetailCmd.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_D)) {
                    changes.add(distributionAmount.doubleValue());
                    changes.add(0);
                    orderDistributorCostDO.setDistributionAmount(new BigDecimal(0));
                }
                changeMap.put("修改运费", changes);
            }
            // 修改订单明细费用
            BigDecimal changeAmount =
                OrderCostConvertor.toOrderDistributorCostDOChange(orderDistributorCostDO, distributorCostDOS);
            // 已付款订单不支持订单金额增加
            if (erpDistributorData.getPayStatus().equals(PayStatus.PAID.getValue())
                && changeAmount.compareTo(new BigDecimal(0)) > 0) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_ERP_CHANGE_PAW_WAY_AMOUNT_ERROR,
                    MessageUtils.get(CommonErrorCode.B_ORDER_ERP_CHANGE_PAW_WAY_AMOUNT_ERROR));
            }
            orderDistributorCostMapper.updateByPrimaryKey(orderDistributorCostDO);
            // 已付款订单需处理订单退款,发送退款申请
            if (!erpDistributorData.getPayStatus().equals(PayStatus.UNPAID.getValue())
                && !erpDistributorData.getPayStatus().equals(PayStatus.REFUND.getValue())
                && changeAmount.compareTo(new BigDecimal(0)) < 0
                && (orderDistributorCostDO.getPaidAmount() != null
                    && orderDistributorCostDO.getPaidAmount().doubleValue() > 0)
                || (orderDistributorCostDO.getDepositAmount() != null
                    && orderDistributorCostDO.getDepositAmount().doubleValue() > 0)) {
                erpDistributorData.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                refundDTO = OrderCostConvertor.toOrderChangeRefundDTO(changeAmount, erpDistributorData,
                    orderDistributorCostDO, null, "ERP", "ERP订单变更", distributor);
            }
        }
        return refundDTO;
    }

    /**
     * 更新退款金额
     * 
     * @param orderId
     * @param refundedAmount
     */
    public void updateOrderRefundedAmount(Integer orderId, Integer distributorId, BigDecimal refundedAmount) {
        orderDistributorCostMapper.updateOrderRefundedAmount(orderId, distributorId, refundedAmount);
    }

    /**
     * 更新订单层级费用数据
     * 
     * @param orderId
     */
    public void orderChangeCost(Integer orderId) {

    }

}
