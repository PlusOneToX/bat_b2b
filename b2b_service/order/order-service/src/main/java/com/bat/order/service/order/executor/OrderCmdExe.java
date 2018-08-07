package com.bat.order.service.order.executor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.cost.executor.*;
import com.bat.order.service.message.MessageConvertor;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.convertor.OrderConvertor;
import com.bat.order.service.order.convertor.OrderCostConvertor;
import com.bat.order.service.order.executor.customer.UserCustomerOrderCmdExe;
import com.bat.order.service.order.validator.OrderInfoValidator;
import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import com.bat.order.service.third.financial.FinancialCmdExeRpc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeB2BOrderDTORpcQry;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.OrderGoodsExchangeCodeMapper;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.mq.dto.OrderRefundDTO;
import com.bat.order.mq.dto.OrderTimerDTO;
import com.bat.order.mq.dto.OrderTreeNodeDataDTO;
import com.bat.order.mq.dto.OrderVoucherDTO;
import com.bat.order.service.cost.executor.*;
import com.bat.order.service.data.executor.OrderCustomerDataCmdExe;
import com.bat.order.service.data.executor.OrderCustomerDataQryExe;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;
import com.bat.order.service.data.executor.OrderDistributorDataQryExe;
import com.bat.order.service.order.convertor.OrderDataConvertor;
import com.bat.order.service.order.dto.CancelOrderContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCmdExe {

    @Resource
    private OrderRpcExe orderRpcExe;
    @Resource
    private OrderQryExe orderQryExe;
    @Resource
    private OrderGoodsCustomerCostQryExe orderGoodsCustomerCostQryExe;
    @Resource
    private OrderGoodsDistributorCostQryExe orderGoodsDistributorCostQryExe;
    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;
    @Resource
    private OrderDistributorCostQryExe distributorCostQryExe;
    @Resource
    private OrderDistributorCostCmdExe distributorCostCmdExe;
    @Resource
    private OrderDistributorDataQryExe distributorDataQryExe;
    @Resource
    private OrderDistributorDataCmdExe distributorDataCmdExe;
    @Resource
    private OrderCustomerDataQryExe customerDataQryExe;
    @Resource
    private OrderCustomerDataCmdExe customerDataCmdExe;
    @Resource
    private OrderCustomerCostQryExe customerCostQryExe;
    @Resource
    private OrderConfig orderConfig;
    @Resource
    private OrderGoodsExchangeCodeMapper orderGoodsExchangeCodeMapper;
    @Resource
    private MessageSendService sendService;
    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;
    @Resource
    private OrderExtendDataMapper orderExtendDataMapper;
    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private OrderDistributorDataMapper orderDistributorDataMapper;
    @Resource
    private OrderCustomerDataMapper orderCustomerDataMapper;
    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;
    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;
    @Resource
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostMapper;
    @Resource
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostMapper;
    @Resource
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;
    @Resource
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private UserCustomerOrderCmdExe userCustomerOrderCmdExe;

    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCmdExe.class);

    @Autowired
    private FinancialCmdExeRpc financialCmdExeRpc;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 根据订单id和C端客户id或分销商id生成分销层级数据
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrderTreeNodeData(OrderTreeNodeDataDTO cmd) {
        LOGGER.info("开始生成分销层数据:{}", JSONObject.toJSONString(cmd));
        // 支持C端客户订单和分销订单
        OrderDistributorDataDO distributorData = null;
        List<OrderGoodsDistributorCostDO> goodsDistributorCosts = null;
        OrderDistributorCostDO distributorCost = null;
        OrderCustomerDataDO orderCustomerData = null;
        OrderCustomerCostDO orderCustomerCost = null;
        BigDecimal platformAmount = null;
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(cmd.getOrderId());
        OrderDeliveryDO orderDelivery = orderDeliveryDOMapper.getByOrderId(cmd.getOrderId());
        OrderInfoDO order = orderQryExe.getById(cmd.getOrderId());
        // 兑换卡业务处理
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS = new ArrayList<>();
        Map<String, ExchangeCodeB2BOrderDTORpcQry> exchangeCodeMap = new HashMap<>();
        if (orderConfig.getPlatformCards().contains(order.getOrderSource())) {
            exchangeCodeDOS = orderGoodsExchangeCodeMapper.listByOrderId(order.getId());
            List<String> secretCode =
                exchangeCodeDOS.stream().map(OrderGoodsExchangeCodeDO::getSecretCode).collect(Collectors.toList());
            List<ExchangeCodeB2BOrderDTORpcQry> exchangeCodeB2BOrders =
                orderRpcExe.listExchangeCodeB2BOrder(secretCode);
            exchangeCodeMap = exchangeCodeB2BOrders.stream()
                .collect(Collectors.toMap(ExchangeCodeB2BOrderDTORpcQry::getSecretCode, a -> a, (k1, k2) -> k1));
            if (exchangeCodeDOS.get(0).getExchangeOrderId() == null) {
                // 获取发货分销商对应的销售员，统计业绩用
                DistributorRpcDTO exchangeDistributor =
                    orderRpcExe.getDistributor(exchangeCodeB2BOrders.get(0).getDistributorId());
                OrderConvertor.updateOrderGoodsExchangeCodeDO(exchangeCodeB2BOrders, exchangeCodeDOS,
                    exchangeDistributor);
                orderGoodsExchangeCodeMapper.updateList(exchangeCodeDOS);
            }
        }
        // c端
        if (cmd.getCounterpartyType().equals(Constant.COUNTERPARTY_TYPE_2)) {
            orderCustomerData = customerDataQryExe.getOrderCustomerData(cmd.getOrderId(), cmd.getCustomerId());
            distributorData = orderDistributorDataMapper.getByOrderIdAndDistributorId(orderCustomerData.getOrderId(),
                orderCustomerData.getDistributorId());
            if (distributorData != null) {
                return;
            }
            DistributorRpcDTO distributor = orderRpcExe.getDistributor(orderCustomerData.getDistributorId());
            // 生成费用相关数据(明细费用和分销商费用)
            orderCustomerCost = customerCostQryExe.getOrderCustomerCost(cmd.getOrderId(), cmd.getCustomerId());
            List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS =
                orderGoodsCustomerCostQryExe.listOrderGoodsCustomerCost(cmd.getOrderId(), cmd.getCustomerId());
            goodsDistributorCosts =
                orderGoodsDistributorCostQryExe.createOrderGoodsDistributorCostByCustomer(orderGoodsDOS,
                    orderGoodsCustomerCostDOS, exchangeCodeDOS, distributor, orderCustomerCost, exchangeCodeMap);
            distributorCost = distributorCostCmdExe.createOrderDistributorCost(orderDelivery, orderCustomerCost,
                goodsDistributorCosts, orderGoodsDOS, distributor, exchangeCodeDOS);
            // 生成分销层数据（注意C端客户订单）
            distributorData = distributorDataCmdExe.createOrderDistributorData(order, orderCustomerData, distributor,
                distributorCost, exchangeCodeDOS);
            // 生成佣金数据，且同步更新分销商账户余额(平台收款情况才需计算佣金)，暂时统一订单发货后同步生成佣金
            // if (!distributor.getCustomerMode().equals(CUSTOMER_MODE_3)) {
            // distributorCostCmdExe.createOrderDistributorCommission(distributor, orderCustomerCost, distributorCost);
            // }

        } else {
            // 查询付款分销商的order_distributor_data
            distributorData = distributorDataQryExe.getOrderDistributorData(cmd.getOrderId(), cmd.getDistributorId());
            // 查询付款分销商的order_goods_distributor_cost
            goodsDistributorCosts =
                orderGoodsDistributorCostQryExe.listOrderGoodsDistributorCost(cmd.getOrderId(), cmd.getDistributorId());
            // 查询付款分销商的order_distributor_cost
            distributorCost =
                distributorCostQryExe.getByOrderIdAndDistributorId(cmd.getOrderId(), cmd.getDistributorId());
        }
        // 生成分销层级数据
        createOrderTreeNodeData(order, distributorData, goodsDistributorCosts, distributorCost, orderGoodsDOS,
            exchangeCodeDOS);
        // 处理分账
        try {
            if (cmd.getCounterpartyType().equals(Constant.COUNTERPARTY_TYPE_2)) {
                financialCmdExeRpc.dealwithShopSubAccount(orderCustomerData, orderCustomerCost, distributorCost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在线支付未支付自动关闭
     *
     * @param orderTimer
     */
    public void orderTimerPowerOff(OrderTimerDTO orderTimer) {
        log.info("在线支付未支付自动关闭:{}", JSONObject.toJSONString(orderTimer));
        // 更新订单状态
        Short orderStatus = null;
        Date time = new Date(System.currentTimeMillis());
        if (orderTimer.getCounterpartyType().equals(Constant.COUNTERPARTY_TYPE_2)) {
            OrderCustomerDataDO customerDataDO =
                customerDataQryExe.getOrderCustomerData(orderTimer.getOrderId(), orderTimer.getCustomerId());
            if (customerDataDO.getOrderStatus().equals(OrderStatus.PENDING.getValue())) {
                orderStatus = OrderStatus.CANCELLED.getValue();
                customerDataDO.setOrderStatus(orderStatus);
                customerDataDO.setUpdateTime(time);
                customerDataCmdExe.updateOrderCustomerData(customerDataDO);
            }
            userCustomerOrderCmdExe.dealFlagSendToSangxing(orderTimer.getOrderId());
        } else {
            // 分销订单
            OrderDistributorDataDO distributorData =
                distributorDataQryExe.getOrderDistributorData(orderTimer.getOrderId(), orderTimer.getDistributorId());
            if (distributorData.getOrderStatus().equals(OrderStatus.PENDING.getValue())) {
                orderStatus = OrderStatus.CANCELLED.getValue();
                distributorData.setOrderStatus(orderStatus);
                distributorData.setUpdateTime(time);
                distributorDataCmdExe.updateOrderDistributorData(distributorData);
                // 回退 代金券
                rollBackRebateVoucher(orderTimer.getOrderId());
            }
        }
        // 关闭订单解锁库存（非预售和定制订单）
        if (orderStatus != null && orderStatus.equals(OrderStatus.CANCELLED.getValue())) {
            OrderInfoDO order = orderQryExe.getById(orderTimer.getOrderId());
            List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_4);
            List<OrderTypeDO> mtoOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_2);
            if ((CollectionUtils.isEmpty(diyOrderTypes) || !order.getOrderTypeId().equals(diyOrderTypes.get(0).getId()))
                && (CollectionUtils.isEmpty(mtoOrderTypes)
                    || !order.getOrderTypeId().equals(mtoOrderTypes.get(0).getId()))) {
                // 解锁库存
                orderGoodsStockCmdExe.releaseOrderGoodsStock(order.getId());
            }
        }
        // 如果有使用优惠券或兑换卡，且订单被自动取消情况，需退回优惠券或兑换卡(暂时只支持C端客户)
        if (orderStatus != null && orderStatus.equals(OrderStatus.CANCELLED.getValue())
            && orderTimer.getCounterpartyType().equals(Constant.COUNTERPARTY_TYPE_2)) {
            OrderInfoDO order = orderInfoDOMapper.selectByPrimaryKey(orderTimer.getOrderId());
            List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS = orderGoodsCustomerCostQryExe
                .listOrderGoodsCustomerCost(orderTimer.getOrderId(), orderTimer.getCustomerId());
            List<String> couponNos = orderGoodsCustomerCostDOS.stream()
                .filter(orderGoodsCustomerCostDO -> StringUtils.isNotBlank(orderGoodsCustomerCostDO.getCouponNo()))
                .map(OrderGoodsCustomerCostDO::getCouponNo).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(couponNos)) {
                sendService.updateCouponStatusForOrderCancel(couponNos, Constant.COUPON_STATUS_2);
            }
            // 兑换卡情况，退回兑换卡
            if (orderConfig.getPlatformCards().contains(order.getOrderSource())) {
                sendService.orderCancelExchangeCode(order.getId());
            }
        }
    }

    /**
     * 根据分销数据同步erp订单，且生成上级分销层数据
     */
    public void createOrderTreeNodeData(OrderInfoDO order, OrderDistributorDataDO distributorData,
        List<OrderGoodsDistributorCostDO> goodsDistributorCosts, OrderDistributorCostDO distributorCost,
        List<OrderGoodsDO> orderGoodsDOS, List<OrderGoodsExchangeCodeDO> exchangeCodeDOS) {
        // 如果是定制订单 1级分销层数据需发送标签生成消息
        if (distributorData.getTreeNode() == 1) {
            List<OrderGoodsDiyDO> diyDOS = orderGoodsDiyDOMapper.listByOrderId(distributorData.getOrderId());
            if (!CollectionUtils.isEmpty(diyDOS)) {
                sendService.orderGoodsDiyLabel(distributorData.getDistributorId(), distributorData.getOrderId(),
                    diyDOS);
            }
            // 定制订单同步工厂消息(一级分销商订单才开始发送)
            List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_4);
            if (!CollectionUtils.isEmpty(diyOrderTypes)
                && order.getOrderTypeId().equals(diyOrderTypes.get(0).getId())) {
                // 获取定制订单同步工厂时间
                Integer orderAsynFactoryTime = orderRpcExe.getOrderAsynFactoryTime(distributorData.getDistributorId());
                sendService.orderAsynFactoryNew(order.getId(), orderAsynFactoryTime);
            }
        }
        // 如果是同步erp层数据，根据条件需发送同步erp消息
        if (distributorData.getErpFlag().equals(Constant.ERP_FLAG_1)) {
            OrderExtendDataDO orderExtendDataDO = orderExtendDataMapper.getByOrderId(distributorData.getOrderId());
            // 如果未生成订单扩展数据，补生成订单扩展数据
            if (orderExtendDataDO == null) {
                DistributorRpcDTO distributor = orderRpcExe.getDistributor(distributorData.getDistributorId());
                orderExtendDataDO = OrderConvertor.toOrderExtendDataDO(order.getId(), distributor);
                orderExtendDataMapper.insert(orderExtendDataDO);
            }
            // 现款线上支付情况需同步生成收款单
            if (StringUtils.isNotBlank(distributorCost.getOutTradeNo())) {
                OrderVoucherDTO voucherDTO = null;
                OrderCustomerCostDO customerCostDO = orderCustomerCostMapper.getByOrderId(order.getId());
                if (customerCostDO != null) {
                    voucherDTO =
                        MessageConvertor.toOrderVoucherDTO(distributorCost, distributorData, Constant.COUNTERPARTY_TYPE_2);
                } else {
                    voucherDTO =
                        MessageConvertor.toOrderVoucherDTO(distributorCost, distributorData, Constant.COUNTERPARTY_TYPE_1);
                }
                // 生成收款单
                sendService.orderVoucher(voucherDTO);
            }
            // 发送同步erp订单(区间结算、线下银行转账或已付款订单自动同步erp)
            if ((distributorData.getPayWay().equals(PayWay.Period_settlement.getValue())
                || distributorData.getPayWay().equals(PayWay.Offline_payment.getValue())
                || distributorData.getPayStatus().equals(PayStatus.PAID.getValue()))
                && StringUtils.isBlank(orderExtendDataDO.getOrderErpNo())) {
                sendService.orderAsynErpNew(order.getId());
            }
        }
        // 如果当前层级数据大于一级分销时继续生成分销层级数据
        if (distributorData.getTreeNode() > 1
            && distributorData.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
            // 根据订单id和上级分销商id查询并判断是否已生成分销层数据（order_distributor_data）
            OrderDistributorDataDO ancestorDistributorDataDO = orderDistributorDataMapper
                .getByOrderIdAndDistributorId(distributorData.getOrderId(), distributorData.getDistributorAncestorId());
            List<OrderGoodsDistributorCostDO> ancestorGoodsDistributorCosts =
                orderGoodsDistributorCostMapper.listByOrderIdAndDistributorId(distributorData.getOrderId(),
                    distributorData.getDistributorAncestorId());
            OrderDistributorCostDO ancestorDistributorCost = orderDistributorCostMapper
                .getByOrderIdAndDistributorId(distributorData.getOrderId(), distributorData.getDistributorAncestorId());
            // 查询上级分销商信息
            DistributorRpcDTO ancestorDistributor =
                orderRpcExe.getDistributor(distributorData.getDistributorAncestorId());
            // 当订单分销收款方式为上级收款情况，还需找到订单分销上级收款方式
            DistributorExtendDataRpcDTO extendDataRpcDTO = null;
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
            // 还未生成分销层数据，去生成，如果已生成分销层数据时更新分销层数据
            if (ancestorDistributorDataDO == null) {
                // order_goods_distributor_cost
                ancestorGoodsDistributorCosts = orderGoodsDistributorCostQryExe
                    .createOrderGoodsDistributorCostByDistributor(order, orderGoodsDOS, exchangeCodeDOS,
                        goodsDistributorCosts, ancestorDistributor, distributorData, distributorCost, extendDataRpcDTO);

                // order_distributor_cost
                ancestorDistributorCost = distributorCostCmdExe.createOrderDistributorCost(distributorCost,
                    ancestorGoodsDistributorCosts, orderGoodsDOS, distributorData, extendDataRpcDTO);

                // order_distributor_data
                ancestorDistributorDataDO = distributorDataCmdExe.createOrderDistributorData(order, distributorData,
                    ancestorDistributor, ancestorDistributorCost);
            } else {
                // 当平台收款情况，修改订单费用分销层数据的平台收款金额
                if ((distributorData.getDistributionMode() == null
                    || distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)
                    || (distributorData.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)
                        && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_1)))
                    && StringUtils.isNotBlank(distributorCost.getOutTradeNo())) {
                    orderGoodsDistributorCostQryExe.updateOrderGoodsDistributorCostByDistributor(goodsDistributorCosts,
                        ancestorGoodsDistributorCosts);
                    distributorCostCmdExe.updateOrderDistributorCost(distributorCost, ancestorDistributorCost);
                    distributorDataCmdExe.updateOrderDistributorData(distributorData, ancestorDistributorDataDO);
                }
            }
            // 递归 生成上级分销层数据
            createOrderTreeNodeData(order, ancestorDistributorDataDO, ancestorGoodsDistributorCosts,
                ancestorDistributorCost, orderGoodsDOS, exchangeCodeDOS);
        }
    }

    /**
     * 根据第三方订单编码取消订单（第三方系统取消订单）
     *
     * @param orderThirdpartyNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderByThirdparty(Integer orderId, String orderNo, String orderThirdpartyNo,
        Integer distributorId, String remark) {
        OrderInfoDO orderInfoDO = null;
        if (orderId != null) {
            orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(orderId);
        }
        if (orderInfoDO == null && StringUtils.isNotBlank(orderNo)) {
            orderInfoDO = orderInfoDOMapper.getByOrderNo(orderNo);
            orderId = orderInfoDO.getId();
        }
        if (orderInfoDO == null && StringUtils.isNotBlank(orderThirdpartyNo)) {
            if (distributorId == null) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_ID_NULL,
                    MessageUtils.get(CommonErrorCode.B_ORDER_DISTRIBUTOR_ID_NULL));
            }
            List<OrderExtendDataDO> extendDataDOS =
                orderExtendDataMapper.listByOrderThirdpartyNoAndDistributorId(orderThirdpartyNo, distributorId);
            if (!CollectionUtils.isEmpty(extendDataDOS)) {
                orderId = extendDataDOS.get(0).getOrderId();
            }
        }
        CancelOrderContext context = new CancelOrderContext();
        context.setOrderId(orderId);
        context.setRemark(remark);
        context.setOperatorName("第三方系统");
        context.setSource(CancelOrderContext.CancelOrderEnum.THIRD);
        cancelOrder(context);
    }

    /**
     * 根据erp订单编码取消订单（erp端取消订单）
     *
     * @param orderErpNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderCheckByErp(String orderErpNo, Short orderStatus, String remark) {
        OrderExtendDataDO orderExtendDataDO = orderExtendDataMapper.getByOrderErpNo(orderErpNo);
        OrderInfoValidator.checkOrderExtendDataDO(orderExtendDataDO);
        // 取消订单
        if (orderStatus.equals(OrderStatus.CANCELLED.getValue())) {
            // 取消订单统一入口
            CancelOrderContext context = new CancelOrderContext();
            context.setOrderId(orderExtendDataDO.getOrderId());
            context.setRemark(remark);
            context.setOperatorName("ERP");
            context.setSource(CancelOrderContext.CancelOrderEnum.ERP);
            cancelOrder(context);
        } else {
            OrderDistributorDataDO erpDistributorDataDO =
                orderDistributorDataMapper.listByOrderErpNoAndErpFlag(orderErpNo, Constant.ERP_FLAG_1).get(0);
            if (erpDistributorDataDO.getOrderStatus().equals(OrderStatus.CANCELLED.getValue())) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_CANCEL_ERROR, MessageUtils.get(CommonErrorCode.B_ORDER_CANCEL_ERROR));
            }
            if (!orderStatus.equals(erpDistributorDataDO.getOrderStatus())) {
                erpDistributorDataDO.setOrderStatus(orderStatus);
                erpDistributorDataDO.setUpdateTime(new Date(System.currentTimeMillis()));
                // 订单确认，需发送分销商层级数据(可能会生成上级分销商层订单数据)
                if (erpDistributorDataDO.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
                    OrderTreeNodeDataDTO dataDTO = new OrderTreeNodeDataDTO();
                    dataDTO.setCounterpartyType(Constant.COUNTERPARTY_TYPE_1);
                    dataDTO.setOrderId(erpDistributorDataDO.getOrderId());
                    dataDTO.setDistributorId(erpDistributorDataDO.getDistributorId());
                    sendService.orderTreeNodeData(dataDTO);
                }
                orderDistributorDataMapper.updateByPrimaryKey(erpDistributorDataDO);
            }
        }
        messageSendService.oredrLogPackage(orderExtendDataDO.getOrderId(), "ERP订单状态变更", "变更成功",
            JSONObject.toJSONString(orderExtendDataDO), "erp");
    }

    /**
     * 根据订单id取消端订单(B2B2C系统前端，包括前后台)
     *
     * @param orderId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderByOrderId(Integer orderId, String remark, String operatorId, String operatorName) {
        CancelOrderContext context = new CancelOrderContext();
        context.setOrderId(orderId);
        context.setRemark(remark);
        context.setOperatorId(operatorId);
        context.setOperatorName(operatorName);
        context.setSource(CancelOrderContext.CancelOrderEnum.B2B);
        cancelOrder(context);
    }

    /**
     * 定时器取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderCancelByXXLJob() {
        String notDeliveryAutoCloseSwitch = orderRpcExe.getBaseSettingByKey("not_delivery_auto_close_switch", "false");
        if (Boolean.parseBoolean(notDeliveryAutoCloseSwitch)) {
            List<Integer> orderIds = getCancelOrderList_KDFH_ZF_7T_GN();
            if (orderConfig.getNotDeliveryAutoCloseSwitch().equals(Constant.OPEN_YES)) {
                doCanceleOrderByXXLJob(orderIds);
            }
        }
    }

    private void doCanceleOrderByXXLJob(List<Integer> orderIds) {
        Map<Integer, com.bat.dubboapi.order.common.Response> result = new HashMap<>();
        for (Integer orderId : orderIds) {
            CancelOrderContext context = new CancelOrderContext();
            context.setOrderId(orderId);
            context.setRemark("定时器取消订单");
            context.setOperatorId(null);
            context.setOperatorName("TIMER");
            context.setSource(CancelOrderContext.CancelOrderEnum.TIMER);
            try {
                cancelOrder(context);
                result.put(orderId, com.bat.dubboapi.order.common.Response.buildSuccess());
            } catch (OrderException e) {
                log.error("code:{},msg:{}", e.getCode(), e.getMsg());
                result.put(orderId, com.bat.dubboapi.order.common.Response.buildFailure(e.getCode(), e.getMsg()));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (Exception e) {
                log.error("msg:{}", e.getMessage());
                e.printStackTrace();
                result.put(orderId,
                    com.bat.dubboapi.order.common.Response.buildFailure("Exception", e.getMessage()));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        log.info("===============================定时取消订单结果==============================");
        for (Map.Entry<Integer, com.bat.dubboapi.order.common.Response> entry : result.entrySet()) {
            log.info("订单：{}，取消结果：{}", entry.getKey(), JSON.toJSONString(entry.getValue()));
        }
    }

    /**
     * // 款到发货+ 直发订单 + 超过7天未发货 + 国内订单 + 待确认/已确认
     */
    private List<Integer> getCancelOrderList_KDFH_ZF_7T_GN() {
        log.info("查询订单 款到发货+ 直发订单 + 超过7天未发货 + 待确认/已确认");
        List<Integer> result;
        Map<String, Object> map = new HashMap<>();
        setZFParams(map);
        String defaultValue = "7";
        String notDeliveryAutoCloseUnit = orderRpcExe.getBaseSettingByKey("not_delivery_auto_close_unit", defaultValue);
        if (!StringUtils.isNumeric(notDeliveryAutoCloseUnit)) {
            notDeliveryAutoCloseUnit = defaultValue;
        }
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime time = todayStart.minusDays(Long.parseLong(notDeliveryAutoCloseUnit));
        Date endTime = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        map.put("endTime", endTime);
        map.put("orderStatus", Arrays.asList(OrderInfoConstant.ORDER_STATUS_UN_CONFIRM, OrderInfoConstant.ORDER_STATUS_CONFIRMED));
        map.put("payWayNot", PayWay.Period_settlement.getValue());
        map.put("countryId", orderConfig.getCountryChina());
        List<OrderInfoDO> orderInfoDOS = orderInfoDOMapper.listCancelOrderByParams(map);
        result = orderInfoDOS.stream().map(OrderInfoDO::getId).collect(Collectors.toList());
        log.info("查询结果：id集合：{}", result);
        return result;
    }

    /**
     * 直发订单筛选参数
     *
     * 含有在途都不是直发 定制都不是 非B2B直发订单 直运订单不是直发
     * 
     * @param map
     */
    private void setZFParams(Map<String, Object> map) {
        OrderTypeDO diyOrderType = getOrderType(Constant.SPECIAL_FLAG_4);
        OrderTypeDO zyOrderType = getOrderType(Constant.SPECIAL_FLAG_5);
        List<Integer> orderTypeIds = new ArrayList<>();
        if (diyOrderType != null) {
            orderTypeIds.add(diyOrderType.getId());
        }
        if (zyOrderType != null) {
            orderTypeIds.add(zyOrderType.getId());
        }
        if (!CollectionUtils.isEmpty(orderTypeIds)) {
            map.put("orderTypeIdsNot", orderTypeIds);
        }
        map.put("autoDelivery", Constant.AUTO_DELIVERY_1);
        map.put("stockTypeNot",
            Arrays.asList(OrderInfoConstant.ORDER_STOCK_TYPE_ONWAY, OrderInfoConstant.ORDER_STOCK_TYPE_MERGE));
        map.put("deliverStatus", OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED);
    }

    /**
     * 取消订单V2
     */
    public void cancelOrder(CancelOrderContext context) {
        Integer orderId = context.getOrderId();
        CancelOrderContext.CancelOrderEnum source = context.getSource();
        if (orderId == null) {
            throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_NOT_EXIST);
        }
        OrderInfoDO order = orderInfoDOMapper.selectByPrimaryKey(orderId);
        OrderInfoValidator.checkCancelOrder(order);
        OrderExtendDataDO orderExtendDataDO = orderExtendDataMapper.getByOrderId(orderId);
//        OrderInfoValidator.checkOrderExtendDataDO(orderExtendDataDO);
        if (source == CancelOrderContext.CancelOrderEnum.B2B) {
            if (orderExtendDataDO != null && StringUtils.isNotBlank(orderExtendDataDO.getOrderErpNo())) {
                // b2b 不能取消 已经同步给ERP的订单 保持和以前的逻辑一致
                context.setRemark(null);
            }
        }
        cancelOrder(order, orderExtendDataDO, context.getRemark(), context.getOperatorId(), context.getOperatorName());
        if (source != CancelOrderContext.CancelOrderEnum.THIRD) {
            sendService.orderCancelToThirdNew(order.getId());
        }
    }

    private OrderTypeDO getOrderType(Short specialFlag) {
        List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(specialFlag);
        if (!CollectionUtils.isEmpty(diyOrderTypes)) {
            return diyOrderTypes.get(0);
        }
        return null;
    }

    /**
     * （统一入取消订单口）
     *
     * @param order
     * @param remark
     */
    public void cancelOrder(OrderInfoDO order, OrderExtendDataDO orderExtendDataDO, String remark, String operatorId,
        String operatorName) {
        List<OrderGoodsDO> goodsDOS = orderGoodsDOMapper.listByOrderId(order.getId());
        // 退款金额
        List<OrderRefundDTO> refundDTOS = new ArrayList<>();
        // 解锁库存
        List<ItemStockLockDTO> lockDTOS = new ArrayList<>();
        // 优惠券码
        List<String> couponNos = new ArrayList<>();
        // 订单是否已取消
        AtomicBoolean cancelB = new AtomicBoolean(true);
        // 定制订单，且已同步工厂,需通过工厂取消成功后异步处理其他端订单状态
        OrderTypeDO diyOrderType = getOrderType(Constant.SPECIAL_FLAG_4);
        OrderTypeDO mtoOrderType = getOrderType(Constant.SPECIAL_FLAG_2);

        boolean diyOrderFlag = diyOrderType != null && order.getOrderTypeId().equals(diyOrderType.getId());
        if (diyOrderFlag && orderExtendDataDO != null
            && StringUtils.isNotBlank(orderExtendDataDO.getOrderFactoryNo())) {
            LOGGER.info("该订单为定制订单，开始取消工厂订单:{}", orderExtendDataDO.getOrderFactoryNo());
            // 取消工厂订单
            Response<Boolean> factoryResponse =
                orderRpcExe.cancelOrderToFactory(orderExtendDataDO.getFactoryCode(), order.getId(),order.getOrderNo(), remark);
            if (factoryResponse == null) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_CANCEL_FACTORY_NULL,
                    MessageUtils.get(CommonErrorCode.B_ORDER_CANCEL_FACTORY_NULL));
            }
            if (!factoryResponse.getData()) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_CANCEL_FACTORY_ERROR,
                    MessageUtils.get(CommonErrorCode.B_ORDER_CANCEL_FACTORY_ERROR));
            }
            // 取消B2B2C订单
            OrderDistributorDataDO erpDistributorData = cancelB2B2COrder(order, goodsDOS, remark, operatorId,
                operatorName, refundDTOS, lockDTOS, couponNos, diyOrderType, mtoOrderType, cancelB);
            // 现款线上订单，且已付款情况，未同步erp不允许取消
            if (erpDistributorData != null
                && !erpDistributorData.getPayWay().equals(PayWay.Period_settlement.getValue())
                && !erpDistributorData.getPayWay().equals(PayWay.Offline_payment.getValue())
                && erpDistributorData.getPayStatus().equals(PayStatus.PAID.getValue())
                && StringUtils.isBlank(orderExtendDataDO.getOrderErpNo())) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_CASH_CANCEL_ERP_ERROR,
                    MessageUtils.get(CommonErrorCode.B_ORDER_CASH_CANCEL_ERP_ERROR));
            }
            // 发送取消erp订单消息（异步）
            if (erpDistributorData != null && !"ERP".equals(operatorName)
                && StringUtils.isNotBlank(orderExtendDataDO.getOrderErpNo())) {
                LOGGER.info("发送取消erp订单消息:{}", orderExtendDataDO.getOrderErpNo());
                sendService.orderCancelErpNew(erpDistributorData, orderExtendDataDO);
            }
        } else {
            // 取消B2B2C订单
            OrderDistributorDataDO erpDistributorData = cancelB2B2COrder(order, goodsDOS, remark, operatorId,
                operatorName, refundDTOS, lockDTOS, couponNos, diyOrderType, mtoOrderType, cancelB);
            if (erpDistributorData != null) {
                // 取消erp订单（同步）
                if (orderExtendDataDO != null && !"ERP".equals(operatorName)
                    && StringUtils.isNotBlank(orderExtendDataDO.getOrderErpNo())) {
                    LOGGER.info("远程调用erp订单消息:{}", orderExtendDataDO.getOrderErpNo());
                    orderRpcExe.orderCancelErp(erpDistributorData, orderExtendDataDO);
                }
            }
        }
        // 如果订单已取消，无需重复处理后续业务
        if (cancelB.get()) {
            // 发送订单退款消息
            if (!CollectionUtils.isEmpty(refundDTOS)) {
                sendService.orderRefund(refundDTOS);
            }
            // 发送解锁库存消息
            if (!CollectionUtils.isEmpty(lockDTOS)) {
                sendService.orderUnLockStock(lockDTOS, order.getId());
            }
            // 有优惠券情况退回优惠券
            if (!CollectionUtils.isEmpty(couponNos)) {
                LOGGER.info("取消订单优惠卷退回消息发送:{}", couponNos);
                sendService.updateCouponStatusForOrderFail(couponNos, Constant.COUPON_STATUS_2);
            }
            // 兑换卡情况需退回兑换卡
            if (orderConfig.getPlatformCards() != null
                && orderConfig.getPlatformCards().contains(order.getOrderSource())) {
                sendService.orderCancelExchangeCode(order.getId());
            }
            // 处理订单同步操作
            userCustomerOrderCmdExe.dealFlagSendToSangxing(order.getId());
            // 发送商品订单销售量更新消息
            orderGoodsSale(goodsDOS, order.getId(), Constant.CHANGE_TYPE_2, diyOrderFlag);
            // 如果有代金券的 回退代金券
            rollBackRebateVoucher(order.getId());
        }
        LOGGER.info("取消订单处理完毕:{}", order.getId());
        try {
            messageSendService.userOredrLogPackage(order.getId(), operatorId, operatorName, "取消订单", "取消成功！",
                JSONObject.toJSONString(order));
        } catch (Exception e) {
            log.error("记录订单日志出现异常:{}", e);
        }
    }

    private void rollBackRebateVoucher(Integer id) {
        // 回退代金券
        log.info("回退代金券 订单id：{}", id);
        List<OrderDistributorDataDO> orderDistributorDataDOS = orderDistributorDataMapper.listByOrderId(id);
        if (!orderDistributorDataDOS.isEmpty()) {
            OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataDOS.stream()
                .filter(aDo -> aDo.getErpFlag().equals((short)1)).findFirst().orElse(null);
            if (orderDistributorDataDO != null) {
                OrderDistributorCostDO orderDistributorCostDO = orderDistributorCostMapper.getByOrderIdAndDistributorId(
                    orderDistributorDataDO.getOrderId(), orderDistributorDataDO.getDistributorId());
                if (orderDistributorCostDO.getRebateVoucherAmount() != null
                    && orderDistributorCostDO.getRebateVoucherAmount().compareTo(BigDecimal.ZERO) > 0) {
                    // 有抵扣金额
                    List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(id);
                    BigDecimal rollBackAmount = BigDecimal.ZERO;
                    // 未发货标志
                    boolean unDeliverCountFlag = true;
                    // 未发货的需要退返利金额度
                    for (OrderGoodsDO orderGoodsDO : orderGoodsDOS) {
                        Integer unDeliverCount = orderGoodsDO.getUnDeliverCount();
                        Integer deliverCount = orderGoodsDO.getDeliverCount();
                        if (deliverCount > 0) {
                            unDeliverCountFlag = false;
                        }
                        if (unDeliverCount > 0) {
                            OrderGoodsDistributorCostDO orderGoodsDistributorCostDO =
                                orderGoodsDistributorCostMapper.getByOrderGoodsIdAndDistributorId(orderGoodsDO.getId(),
                                    orderDistributorDataDO.getDistributorId());
                            if (orderGoodsDistributorCostDO.getRebateVoucherAmount() != null
                                && orderGoodsDistributorCostDO.getRebateVoucherAmount()
                                    .compareTo(BigDecimal.ZERO) > 0) {
                                rollBackAmount = rollBackAmount.add(orderGoodsDistributorCostDO.getRebateVoucherAmount()
                                    .multiply(BigDecimal.valueOf(unDeliverCount)));
                            }
                        }
                    }
                    // 全部未发货 全额退款
                    if (unDeliverCountFlag) {
                        rollBackAmount = null;
                    }
                    sendService.rollBackRebateVoucher(id, rollBackAmount);
                }
            }
        }
    }

    private void orderGoodsSale(List<OrderGoodsDO> goodsDOS, Integer id, Short changeType2, boolean diyOrderFlag) {
        // 有活动拼团等的情况下 要把活动信息放进去(有可能缺少活动信息 导致销量退不回去)
        if (!diyOrderFlag) {
            for (OrderGoodsDO goodsDO : goodsDOS) {
                OrderGoodsDistributorCostDO orderGoodsDistributorCost = goodsDO.getOrderGoodsDistributorCost();
                if (orderGoodsDistributorCost == null || (orderGoodsDistributorCost.getGoodsPromotionId() == null
                    && orderGoodsDistributorCost.getOrderPromotionId() == null
                    && orderGoodsDistributorCost.getSpellGroupId() == null)) {
                    OrderGoodsDistributorCostDO orderGoodsDistributorCostDO = orderGoodsDistributorCostQryExe
                        .listOrderGoodsDistributorCostByOrderGoodsId(goodsDO.getId()).get(0);
                    orderGoodsDistributorCost = new OrderGoodsDistributorCostDO();
                    orderGoodsDistributorCost.setGoodsPromotionId(orderGoodsDistributorCostDO.getGoodsPromotionId());
                    orderGoodsDistributorCost.setOrderPromotionId(orderGoodsDistributorCostDO.getOrderPromotionId());
                    orderGoodsDistributorCost.setSpellGroupId(orderGoodsDistributorCostDO.getSpellGroupId());
                    goodsDO.setOrderGoodsDistributorCost(orderGoodsDistributorCost);
                }
            }
        }
        sendService.orderGoodsSale(goodsDOS, id, changeType2);
    }

    /**
     * 取消B2B2C订单
     * 
     * @param order
     * @param remark
     */
    public OrderDistributorDataDO cancelB2B2COrder(OrderInfoDO order, List<OrderGoodsDO> goodsDOS, String remark,
        String operatorId, String operatorName, List<OrderRefundDTO> refundDTOS, List<ItemStockLockDTO> lockDTOS,
        List<String> couponNos, OrderTypeDO diyOrderType, OrderTypeDO mtoOrderType, AtomicBoolean cancelB) {
        Date time = new Date(System.currentTimeMillis());
        // C端客户层和分销层数据（需从最底层订单开始取消）
        OrderCustomerDataDO customerDataDO = orderCustomerDataMapper.getByOrderId(order.getId());
        List<OrderDistributorDataDO> distributorDataDOS = orderDistributorDataMapper.listByOrderId(order.getId());
        OrderCustomerCostDO orderCustomerCostDO = null;
        DistributorRpcDTO erpDistributor = null;
        OrderDistributorDataDO erpDistributorData = null;
        List<OrderGoodsCustomerCostDO> goodsCustomerCostDOS = new ArrayList<>();
        if (customerDataDO != null) {
            // 订单已被取消，直接返回
            if (customerDataDO.getOrderStatus().equals(OrderStatus.CANCELLED.getValue())) {
                cancelB.set(false);
                return null;
            }
            orderCustomerCostDO = orderCustomerCostMapper.getByOrderIdAndCustomerId(customerDataDO.getOrderId(),
                customerDataDO.getCustomerId());
            goodsCustomerCostDOS = orderGoodsCustomerCostMapper.listByOrderIdAndCustomerId(customerDataDO.getOrderId(),
                customerDataDO.getCustomerId());
        }
        List<OrderDistributorCostDO> distributorCostDOS = orderDistributorCostMapper.listByOrderId(order.getId());
        List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS =
            orderGoodsDistributorCostMapper.listByOrderId(order.getId());
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            // 订单已被取消，直接返回
            if (distributorDataDOS.get(0).getOrderStatus().equals(OrderStatus.CANCELLED.getValue())) {
                cancelB.set(false);
                return null;
            }
            Optional<OrderDistributorDataDO> optional = distributorDataDOS.stream()
                .filter(distributorDataDO -> distributorDataDO.getErpFlag().equals(Constant.ERP_FLAG_1)).findFirst();
            if (optional.isPresent()) {
                erpDistributorData = optional.get();
                erpDistributor = orderRpcExe.getDistributor(erpDistributorData.getDistributorId());
            }
        }
        // 扣减佣金（暂时不存在佣金，发货时才产生佣金）
        // List<OrderDistributorCommissionDO> commissionDOS = new ArrayList<>();
        // 计算退款（暂时不存在佣金，发货时才产生佣金）
        Map<Integer, OrderGoodsDO> orderGoodsDOMap =
            goodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
        OrderCostConvertor.toOrderCancelRefundDTO(orderGoodsDOMap, customerDataDO, orderCustomerCostDO,
            goodsCustomerCostDOS, distributorDataDOS, distributorCostDOS, goodsDistributorCostDOS, refundDTOS, remark,
            operatorId, operatorName, erpDistributor, couponNos);
        // 处理优惠券问题
        goodsCustomerCostDOS.forEach(orderGoodsCustomerCostDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(orderGoodsCustomerCostDO.getOrderGoodsId());
            LOGGER.info("未发货数量:{},已发货数量:{},优惠卷信息:{}", orderGoodsDO.getUnDeliverCount(), orderGoodsDO.getDeliverCount(),
                orderGoodsCustomerCostDO.getCouponNo());
            if (orderGoodsDO.getUnDeliverCount() != null && orderGoodsDO.getUnDeliverCount() > 0) {
                if (StringUtils.isNotBlank(orderGoodsCustomerCostDO.getCouponNo())
                    && (orderGoodsDO.getDeliverCount() == null || orderGoodsDO.getDeliverCount() == 0)) {
                    LOGGER.info("添加优惠卷到列表:{}", orderGoodsCustomerCostDO.getCouponNo());
                    couponNos.add(orderGoodsCustomerCostDO.getCouponNo());
                }
            }
        });
        // 非定制订单和预售订单需解锁库存
        if ((diyOrderType == null || !order.getOrderTypeId().equals(diyOrderType.getId()))
            && (mtoOrderType == null || !order.getOrderTypeId().equals(mtoOrderType.getId()))) {
            List<ItemStockLockDTO> releaseOrderGoodsStock =
                orderGoodsStockCmdExe.getReleaseOrderGoodsStock(order.getId());
            if (!CollectionUtils.isEmpty(releaseOrderGoodsStock)) {
                lockDTOS.addAll(releaseOrderGoodsStock);
            }
        }
        // 更新订单状态
        OrderDataConvertor.toOrderDataCancel(customerDataDO, distributorDataDOS, time, remark);
        if (customerDataDO != null) {
            orderCustomerDataMapper.updateByPrimaryKey(customerDataDO);
        }
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            orderDistributorDataMapper.updateList(distributorDataDOS);
        }
        if (erpDistributorData != null) {
            erpDistributorData.setCancelRemark(remark);
        }
        // 发送佣金消息（暂时不存在佣金，发货时才产生佣金）
        // if (!CollectionUtils.isEmpty(commissionDOS)) {
        // orderDistributorCommissionMapper.insertList(commissionDOS);
        // sendService.orderCommission(commissionDOS);
        // }
        return erpDistributorData;
    }
}
