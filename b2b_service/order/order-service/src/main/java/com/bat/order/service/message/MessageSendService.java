package com.bat.order.service.message;

import static com.bat.order.service.common.Constant.COUNTERPARTY_TYPE_1;
import static com.bat.order.service.common.Constant.COUNTERPARTY_TYPE_2;
import static com.bat.order.service.common.constant.OrderInfoConstant.ERP_PURCHASE_1;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.order.mq.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNewOrderMsgDTO;
import com.bat.dubboapi.financial.subaccount.dto.OrderSubAccountCmd;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.ErpPurchaseOrderOutboundCmd;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.message.MessageSendServiceI;
import com.bat.order.dao.cost.dataobject.OrderDistributorCommissionDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.mq.service.SenderService;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.data.dao.OrderDistributorDO;
import com.bat.order.service.common.data.dto.OrderInfoDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 9:07
 */
@Slf4j
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;
    @Resource
    private OrderConfig orderConfig;

    @Resource
    private HttpServletRequest request;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendService.class);

    /**
     * 分销订单线上支付未付款定时关闭队列
     *
     * @param distributorId
     * @param orderId
     */
    public void orderTimerPowerOffD(Integer distributorId, Integer orderId) {
        OrderTimerDTO dto = new OrderTimerDTO();
        dto.setOrderId(orderId);
        dto.setDistributorId(distributorId);
        dto.setCounterpartyType(COUNTERPARTY_TYPE_1);
        service.sendObject(dto, "orderTimerPowerOffD", "orderTimerPowerOffD-" + orderId,
            orderConfig.getOrderTimerPowerOffDLevel());
        // 增加未支付提醒消息
        orderUnpaidMsg(orderId, orderConfig.getOrderTimerWillPowerOffDLevel());
    }

    /**
     * C端客户订单线上支付未付款定时关闭队列
     *
     * @param customerId
     * @param orderId
     */
    public void orderTimerPowerOffC(Integer customerId, Integer orderId) {
        OrderTimerDTO dto = new OrderTimerDTO();
        dto.setOrderId(orderId);
        dto.setCustomerId(customerId);
        dto.setCounterpartyType(COUNTERPARTY_TYPE_2);
        service.sendObject(dto, "orderTimerPowerOffC", "orderTimerPowerOffC-" + orderId,
            orderConfig.getOrderTimerPowerOffCLevel());
        // 增加未支付提醒消息
        orderUnpaidMsg(orderId, orderConfig.getOrderTimerWillPowerOffCLevel());
    }

    /**
     * 订单分销层级数据生成消息
     *
     * @param cmds
     */
    public void orderTreeNodeData(List<OrderTreeNodeDataDTO> cmds) {
        cmds.forEach(cmd -> {
            orderTreeNodeData(cmd);
        });
    }

    /**
     * 订单分销层级数据生成消息
     *
     * @param cmd
     */
    public void orderTreeNodeData(OrderTreeNodeDataDTO cmd) {
        service.sendObject(cmd, "orderTreeNodeData", "orderTreeNodeData-" + cmd.getOrderId(),
            orderConfig.getOrderTreeNodeDataLevel());
    }

    /**
     * 发送生成收款单消息
     *
     * @param cmd
     */
    public void orderVoucher(OrderVoucherDTO cmd) {
        if (cmd != null) {
            String key = "orderVoucher-" + cmd.getOutTradeNo();
            log.info("发送生成收款单消息 cmd:{}, key:{}", JSON.toJSONString(cmd), key);
            service.sendObject(cmd, "orderVoucher", key);
        }
    }

    /**
     * 发送收款单同步erp消息(旧)
     *
     * @param orderId
     */
    public void orderVoucherErpOld(Integer orderId) {
        service.sendObject(orderId, "orderVoucherSyncErp", "orderVoucherSyncErp-" + orderId);
    }

    /**
     * 发送收款单同步erp消息(新)
     *
     * @param orderId
     */
    public void orderVoucherErpNew(Integer orderId) {
        service.sendObjectErp(orderId, "orderVoucherSyncErp", "orderVoucherSyncErp-" + orderId);
    }

    /**
     * 发送订单异步同步erp消息(旧)
     *
     * @param orderId
     */
    public void orderAsynErpOld(Integer orderId) {
        String key = "orderAsynErp-" + orderId;
        LOGGER.info("发送订单异步同步erp消息(旧) orderId:{}, key:{}", orderId, key);
        service.sendObject(orderId, "orderAsynErp", key, orderConfig.getOrderAsynErpDelayLevel());
    }

    /**
     * 发送订单异步同步erp消息(新)
     *
     * @param orderId
     */
    public void orderAsynErpNew(Integer orderId) {
        String key = "orderAsynErp-" + orderId;
        LOGGER.info("发送订单异步同步erp消息(新):{}", orderId);
        service.sendObjectErp(orderId, "orderAsynErp", key, orderConfig.getOrderAsynErpDelayLevel());
    }

    /**
     * 发送订单异步同步工厂消息(旧)
     *
     * @param orderId
     */
    public void orderAsynFactoryOld(Integer orderId, Integer orderAsynFactoryTime) {
        service.sendObject(orderId, "orderAsynFactory", "orderAsynFactory-" + orderId,
            String.valueOf(orderAsynFactoryTime));
    }

    /**
     * 发送订单异步同步工厂消息(新)
     *
     * @param orderId
     */
    public void orderAsynFactoryNew(Integer orderId, Integer orderAsynFactoryTime) {
        String key = "orderAsynFactory-" + orderId;
        log.info("发送订单异步同步工厂消息(新) key:{}", key);
        service.sendObjectFactory(orderId, "orderAsynFactory", key, String.valueOf(orderAsynFactoryTime));
    }

    /**
     * 发送订单佣金消息
     *
     * @param commissionDOS
     */
    public void orderCommission(List<OrderDistributorCommissionDO> commissionDOS) {
        if (!CollectionUtils.isEmpty(commissionDOS)) {
            commissionDOS.forEach(commissionDO -> {
                orderCommission(commissionDOS);
            });
        }
    }

    /**
     * 发送订单佣金消息
     *
     * @param commissionDO
     */
    public void orderCommission(OrderDistributorCommissionDO commissionDO) {
        OrderCommissionDTO cmd = MessageConvertor.toOrderCommissionDTO(commissionDO);
        service.sendObject(cmd, "orderCommission",
            "orderCommission-" + cmd.getOrderId() + "-" + cmd.getDistributorId());
    }

    /**
     * 订单反锁库存消息（锁库之后、处理业务失败、解除库存锁定）
     */
    public void orderUnLockStock(List<ItemStockLockDTO> cmds, Integer orderId) {
        if (!CollectionUtils.isEmpty(cmds)) {
            List<OrderUnStockLockDTO> dtos = MessageConvertor.toOrderUnStockLockDTOList(cmds);
            if (orderId == null) {
                String key = "orderUnLockStock-" + "orderFail";
                service.sendObject(dtos, "orderUnLockStock", key);
            } else {
                String key = "orderUnLockStock-" + orderId;
                service.sendObject(dtos, "orderUnLockStock", key);
            }
        }
    }

    /**
     * 发送优惠券状态变更消息
     *
     * @param couponNos
     * @param couponStatus
     */
    public void updateCouponStatusForOrderFail(List<String> couponNos, Short couponStatus) {
        if (!CollectionUtils.isEmpty(couponNos)) {
            CouponStatusDTO dto = MessageConvertor.toCouponStatusDTO(couponNos, couponStatus);
            service.sendObject(dto, "updateCouponStatus", "updateCouponStatus-" + "orderFail");
        }
    }

    /**
     * 发送优惠券状态变更消息
     *
     * @param couponNos
     * @param couponStatus
     */
    public void updateCouponStatusForOrderCancel(List<String> couponNos, Short couponStatus) {
        if (!CollectionUtils.isEmpty(couponNos)) {
            CouponStatusDTO dto = MessageConvertor.toCouponStatusDTO(couponNos, couponStatus);
            service.sendObject(dto, "updateCouponStatus", "updateCouponStatus-" + "orderCancel");
        }
    }

    /**
     * 发送订单日志消息
     */
    public void orderLog(List<Integer> orderIds, String operateSource, Integer operateId, String operator,
        String operateType, String operateDes, String operateData, Date time) {
        List<OrderLogDTO> dtos = MessageConvertor.toOrderLogDTOList(orderIds, operateSource, operateId, operator,
            operateType, operateDes, operateData, time);
        dtos.forEach(dto -> {
            service.sendObject(dto, "orderLog", "orderLog-" + dto.getOrderId());
        });
    }

    /**
     * 发送订单日志消息
     */
    public void oredrLogPackage(Integer orderId, String operateType, String operateDes, String operateData,
        String specialName) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            if (StringUtils.isNotBlank(specialName)) {
                userName = specialName;
            }
            List<Integer> orderIds = new ArrayList<>();
            orderIds.add(orderId);
            orderLog(orderIds, platform, userId, userName, operateType, operateDes, operateData, new Date());

        } catch (Exception e) {
            log.error("记录订单日志出现异常:{}", e);
        }
    }

    /**
     * 发送订单日志消息
     */
    public void userOredrLogPackage(Integer orderId, String userId, String userName, String operateType,
        String operateDes, String operateData) {
        try {
            String platform = "";
            try {
                platform = request.getHeader("platform");
            } catch (Exception e) {
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            List<Integer> orderIds = new ArrayList<>();
            orderIds.add(orderId);
            if (userId == null) {
                orderLog(orderIds, platform, null, userName, operateType, operateDes, operateData, new Date());
            } else {
                orderLog(orderIds, platform, Integer.valueOf(userId), userName, operateType, operateDes, operateData,
                    new Date());
            }
        } catch (Exception e) {
            log.error("记录订单日志出现异常:{}", e);
        }
    }

    /**
     * 发送发货单日志
     */
    private void orderDeliverBillLog(Integer orderDeliverBillId, Integer orderId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        OrderDeliverBillLogDTO dto = MessageConvertor.toOrderDeliverBillLogDTO(orderDeliverBillId, orderId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);

        service.sendObject(dto, "orderDeliverBillLog", "orderDeliverBillLog-" + dto.getOrderDeliverBillId());
    }

    public void orderDeliverBillLogPackage(Integer orderDeliverBillId, Integer orderId, String operateType,
        String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            orderDeliverBillLog(orderDeliverBillId, orderId, platform, userId, userName, operateType, operateDes,
                operateData, new Date());
        } catch (Exception e) {
            log.error("记录发货单日志出现异常:{}", e);
        }
    }

    /**
     * 发送通用日志消息
     */
    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

    /**
     * 发送订单消息退款消息
     *
     * @param refundDTOS
     */
    public void orderRefund(List<OrderRefundDTO> refundDTOS) {
        if (!CollectionUtils.isEmpty(refundDTOS)) {
            refundDTOS.forEach(refundDTO -> {
                orderRefund(refundDTO);
            });
        }
    }

    /**
     * 发送订单消息退款消息
     *
     * @param refundDTO
     */
    public void orderRefund(OrderRefundDTO refundDTO) {
        service.sendObject(refundDTO, "orderRefund", "orderRefund-" + refundDTO.getOrderId());
    }

    /**
     * 发送取消erp订单消息(旧)
     *
     */
    public void orderCancelErpOld(OrderDistributorDataDO erpDistributorData, OrderExtendDataDO orderExtendDataDO) {
        OrderCancelDTO dto = MessageConvertor.toOrderCancelDTO(erpDistributorData, orderExtendDataDO);
        service.sendObject(dto, "orderCancelErp", "orderCancelErp-" + dto.getOrderId());
    }

    /**
     * 发送取消erp订单消息(新)
     *
     */
    public void orderCancelErpNew(OrderDistributorDataDO erpDistributorData, OrderExtendDataDO orderExtendDataDO) {
        OrderCancelDTO dto = MessageConvertor.toOrderCancelDTO(erpDistributorData, orderExtendDataDO);
        service.sendObjectErp(dto, "orderCancelErp", "orderCancelErp-" + dto.getOrderId());
    }

    /**
     * 发送标签生成消息
     *
     * @param distributorId
     * @param diyDOS
     */
    public void orderGoodsDiyLabel(Integer distributorId, Integer orderId, List<OrderGoodsDiyDO> diyDOS) {
        OrderLableDTO dto = MessageConvertor.toOrderLableDTO(distributorId, orderId, diyDOS);
        String key = "orderGoodsDiyLabel-" + dto.getOrderId();
        log.info("发送标签生成消息 key:{}", key);
        service.sendObject(dto, "orderGoodsDiyLabel", key);
    }

    private String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 同步销售采购单到ERP(柔性,旧)
     *
     * @param erpPurchaseOrderOutboundCmd
     */
    public void syncErpPurchaseAndOutboundOrderOld(ErpPurchaseOrderOutboundCmd erpPurchaseOrderOutboundCmd) {
        log.info("推送采购销售单和采购单到第三方服务、同步ERP、消息参数为{}", JSON.toJSONString(erpPurchaseOrderOutboundCmd));
        service.sendObject(erpPurchaseOrderOutboundCmd, "syncErpPurchaseAndOutboundOrder",
            "syncErpPurchaseAndOutboundOrder-" + erpPurchaseOrderOutboundCmd.getDiyPurchaseOrderDTO().getOrderErpNo());
    }

    /**
     * 同步销售采购单到ERP(柔性,新)
     *
     * @param erpPurchaseOrderOutboundCmd
     */
    public void syncErpPurchaseAndOutboundOrderNew(ErpPurchaseOrderOutboundCmd erpPurchaseOrderOutboundCmd) {
        // 柔性出库单是否同步：1 同步 0 不同步
        if (orderConfig.getErpPurchase().equals(ERP_PURCHASE_1)) {
            log.info("推送采购销售单和采购单到第三方服务、同步ERP、消息参数为{}", JSON.toJSONString(erpPurchaseOrderOutboundCmd));
            service.sendObjectErpPurchase(erpPurchaseOrderOutboundCmd, "syncErpPurchaseAndOutboundOrder",
                "syncErpPurchaseAndOutboundOrder-"
                    + erpPurchaseOrderOutboundCmd.getDiyPurchaseOrderDTO().getOrderErpNo());
        }
    }

    /**
     * 同步出库单到ERP(柔性)已废弃
     *
     * @param diyOutboundSyncErpCmd
     */
    /*public void syncOutStockToERP(DiyOutboundSyncErpCmd diyOutboundSyncErpCmd) {
        log.info("推送出库单（柔性）到第三方服务、同步ERP、消息参数为{}", JSON.toJSONString(diyOutboundSyncErpCmd));
        service.sendObject(diyOutboundSyncErpCmd, "syncOutStockToERP",
            "syncOutStockToERP-" + diyOutboundSyncErpCmd.getOrderErpNo());
    }*/

    /**
     * 订单变更更新层级费用（包括分销和C端订单）
     *
     * @param orderId
     */
    public void orderChangeCost(Integer orderId) {
        service.sendObject(orderId, "orderChangeCost", "orderChangeCost-" + orderId);
    }

    /**
     * 订单取消，退回兑换卡
     *
     * @param orderId
     */
    public void orderCancelExchangeCode(Integer orderId) {
        service.sendObject(orderId, "orderCancelExchangeCode", "orderCancelExchangeCode-" + orderId);
    }

    /**
     * 同步物流信息到第三方(旧)
     *
     * @param id
     *            发货流水id
     */
    public void syncLogisticToThirdOld(Integer id) {
        service.sendObject(id, "orderLogistictToThird", "orderLogistictToThird-" + id);
    }

    /**
     * 同步物流信息到第三方(新)
     *
     * @param id
     *            发货流水id
     */
    public void syncLogisticToThirdNew(Integer id) {
        service.sendObjectCustomer(id, "orderLogistictToThird", "orderLogistictToThird-" + id);
    }

    /**
     * 同步取消订单到第三方(旧)
     *
     * @param orderId
     */
    public void orderCancelToThirdOld(Integer orderId) {
        service.sendObject(orderId, "orderCancelToThird", "orderCancelToThird-" + orderId);
    }

    /**
     * 同步取消订单到第三方(新)
     *
     * @param orderId
     */
    public void orderCancelToThirdNew(Integer orderId) {
        service.sendObjectCustomer(orderId, "orderCancelToThird", "orderCancelToThird-" + orderId);
    }

    /**
     * 商品订单销售数量更新
     */
    public void orderGoodsSale(List<OrderGoodsDO> goodsDOS, Integer orderId, Short changeType) {
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            List<GoodsSaleDTO> goodsSaleDTOS = MessageConvertor.toGoodsSaleDTOList(goodsDOS, changeType);
            log.info("商品订单销售数量更新 {}", JSON.toJSONString(goodsSaleDTOS));
            service.sendObject(goodsSaleDTOS, "orderGoodsSale", "orderGoodsSale-" + orderId);
        }
    }

    /**
     * 商品订单销售数量更新
     */
    public void orderGoodsSale(List<GoodsSaleDTO> saleDTOS, Integer orderId) {
        if (!CollectionUtils.isEmpty(saleDTOS)) {
            log.info("商品订单销售数量更新(ERP订单变更) {}", JSON.toJSONString(saleDTOS));
            service.sendObject(saleDTOS, "orderGoodsSale", "orderGoodsSale-" + orderId);
        }
    }



    /**
     * 订单分账处理
     *
     * @param orderSubAccountCmd
     */
    public void orderSubAccount(OrderSubAccountCmd orderSubAccountCmd) {
        log.info("订单分账、发送消息{}", JSON.toJSONString(orderSubAccountCmd));
        service.sendObject(orderSubAccountCmd, "orderSubAccount", "orderSubAccount-" + orderSubAccountCmd.getOrderId());
    }

    /**
     * 订单审核通过发送消息
     */
    public void orderConfirmMsg(Integer orderId) {
        log.info("订单审核通过发送消息{}", orderId);
        service.sendObjectSubMsg(orderId, "orderConfirmMsg", "orderConfirmMsg-" + orderId);
    }

    /**
     * 订单订单发货通知消息
     */
    public void orderDeliverMsg(Integer orderDeliverBillId) {
        log.info("单订单发货通知消息{}", orderDeliverBillId);
        service.sendObjectSubMsg(orderDeliverBillId, "orderDeliverMsg", "orderDeliverMsg-" + orderDeliverBillId);
    }

    /**
     * 订单未支付提醒
     * 
     * @param orderId
     */
    public void orderUnpaidMsg(Integer orderId, String orderTimerWillPowerOffLevel) {
        log.info("订单未支付提醒发送消息{}", orderId);
        service.sendObjectSubMsg(orderId, "orderUnpaidMsg", "orderUnpaidMsg-" + orderId, orderTimerWillPowerOffLevel);
    }

    /**
     * 新订单提醒
     */
    public void newOrderMsg(Integer orderId, Integer distributorId) {
        DistributorNewOrderMsgDTO dto = new DistributorNewOrderMsgDTO();
        dto.setDistributorId(distributorId);
        dto.setOrderId(orderId);
        log.info("新订单提醒发送消息{}", JSONObject.toJSONString(dto));
        service.sendObjectSubMsg(dto, "newOrderMsg", "newOrderMsg-" + dto.getOrderId());
    }

    /**
     * 抵扣代金券
     *
     * @param orderDistributorDOList
     * @param orderDTO
     */
    public void deductionRebateVoucher(List<OrderDistributorDO> orderDistributorDOList, OrderInfoDTO orderDTO) {
        List<Integer> rebateVoucherIds = orderDTO.getRebateVoucherIds();
        if (!CollectionUtils.isEmpty(rebateVoucherIds)) {
            RebateVoucherDTO dto = MessageConvertor.toRebateVoucherDTO(orderDistributorDOList, rebateVoucherIds);
            if (dto.getVoucherItems().size() > 0) {
                String collect = dto.getVoucherItems().stream().map(RebateVoucherDTO.RebateVoucherItemDTO::getOrderId)
                    .map(String::valueOf).collect(Collectors.joining("-"));
                String key = "deductionRebateVoucher-" + collect;
                log.info("抵扣代金券 key:{} 信息：{}", key, JSON.toJSONString(dto));
                service.sendObject(dto, "deductionRebateVoucher", key);
            }
        }
    }

    /**
     * 回退代金券
     * 
     * @param id
     * @param rollBackAmount
     *            退款金额 如果为NULL 表示订单全额退款
     */
    public void rollBackRebateVoucher(Integer id, BigDecimal rollBackAmount) {
        if (rollBackAmount == null || rollBackAmount.compareTo(BigDecimal.ZERO) > 0) {
            String key = "rollBackRebateVoucher-" + id;
            log.info("回退代金券 key:{}", key);
            RebateVoucherRollBackDTO dto = new RebateVoucherRollBackDTO();
            dto.setOrderId(id);
            dto.setRollBackAmount(rollBackAmount);
            service.sendObject(dto, "rollBackRebateVoucher", key);
        }
    }
}
