package com.bat.order.service.deliver.convertor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.delivery.dto.OrderDeliverSyncErpParam;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryCmd;
import com.bat.dubboapi.order.delivery.dto.OrderLogisticsSyncParam;
import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyOutboundSyncErpCmd;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyPurchaseOrderDTO;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.order.api.order.OrderGoodsDiyServiceI;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import com.bat.order.service.common.constant.OrderInfoConstant;

@Component
public class OrderDeliveryConvertor {

    @Autowired
    private OrderGoodsDiyServiceI orderGoodsDiyServiceI;

    /**
     * 状态矫正(一个订单多次发货)
     * 
     * @param orderInfoDO
     * @param documentStatus
     */
    public static void deliverStatusCorrect(OrderInfoDO orderInfoDO, Short documentStatus, boolean noDeal) {
        // 已审核情况下不处理
        if (OrderDeliverConstant.ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_AUDIT.equals(documentStatus)) {
            return;
        }
        // 如果不处理；则直接返回(非多次发货)
        if (noDeal) {
            return;
        }
        // 非已审核情况下；不能设置为发货状态
        if (OrderInfoConstant.ORDER_DELIVER_STATUS_SOME_SHIPPED.equals(orderInfoDO.getDeliverStatus())
            || OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED.equals(orderInfoDO.getDeliverStatus())) {
            orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED);
        }
    }

    /**
     * 发货状态处理
     * 
     * @param orderInfoDO
     * @param documentStatus
     */
    public static void dealwithDeliverStatus(OrderInfoDO orderInfoDO, Short documentStatus,
        List<OrderDeliverBillDO> orderDeliverBillDOList, List<OrderGoodsDO> goods, String expressNo,
        String deliverErpNo) {
        if (documentStatus.equals(OrderDeliverConstant.ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_SUBMIT)) {
            // 提交操做不做处理
            return;
        }
        if (documentStatus.equals(OrderDeliverConstant.ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_CREATE)
            && orderInfoDO.getDeliverTime() == null) {
            // 创建状态、设置出库时间
            orderInfoDO.setDeliverTime(new Date());
        }
        // 出库单状态是否都是已审核
        AtomicReference<Boolean> allDeliver = new AtomicReference<>(true);
        // 判断之前的发货单是否都有物流单号
        AtomicReference<Boolean> allExistExpressNoFlag = new AtomicReference<>(true);
        // 判断之前的发货单是否含有物流单号
        AtomicReference<Boolean> existExpressNoFlag = new AtomicReference<>(false);
        // 该订单的发货单是否含有已审核的出库单
        AtomicReference<Boolean> auditFlag = new AtomicReference<>(false);
        // 获取本次出库单
        OrderDeliverBillDO orderDeliver = orderDeliverBillDOList.stream()
            .filter(orderDeliverBillDO -> orderDeliverBillDO.getDeliverErpNo().equals(deliverErpNo)).findFirst().get();
        // 移除本次的出库单号
        if (orderDeliver != null) {
            orderDeliverBillDOList.remove(orderDeliver);
        }
        // 取消或删除出库单情况
        if ((documentStatus.equals(OrderDeliverConstant.ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_CANCEL)
            || documentStatus.equals(OrderDeliverConstant.ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_DELETE))) {
            // 出库单都是已审核切都已有物流单号情况订单状态为部分发货,如果有物流单号空情况订单状态为出库中
            if (!CollectionUtils.isEmpty(orderDeliverBillDOList)) {
                orderInfoDO.setDeliverTime(null);
                orderDeliverBillDOList.forEach(orderDeliverBillDO -> {
                    if (orderDeliverBillDO.getDeliverStatus()
                        .equals(OrderDeliverConstant.ORDER_DELIVER_STATUS_UN_CONFIRMED)) {
                        allDeliver.set(false);
                    }
                    if (StringUtils.isBlank(orderDeliverBillDO.getLogisticsNo())) {
                        allExistExpressNoFlag.set(false);
                    }
                    if (StringUtils.isNotBlank(orderDeliverBillDO.getLogisticsNo())) {
                        existExpressNoFlag.set(true);
                    }
                    if (orderDeliverBillDO.getDeliverStatus()
                        .equals(OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED)) {
                        auditFlag.set(true);
                    }
                    if (orderInfoDO.getDeliverTime() == null) {
                        orderInfoDO.setDeliverTime(orderDeliverBillDO.getCreateTime());
                    } else if (orderInfoDO.getDeliverTime().getTime() < orderDeliverBillDO.getCreateTime().getTime()) {
                        orderInfoDO.setDeliverTime(orderDeliverBillDO.getCreateTime());
                    }
                });
                if (allExistExpressNoFlag.get()) {
                    orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SOME_SHIPPED);
                } else {
                    orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_OUTBOUNDING);
                }
            } else {
                orderInfoDO.setDeliverTime(null);
            }
        } else {
            // 非取消或删除出库单情况，如果发货单都有物流单号情况为已发货或部分发货，如果发货单有非物流单号情况为出库中
            orderDeliverBillDOList.forEach(orderDeliverBillDO -> {
                if (StringUtils.isBlank(orderDeliverBillDO.getLogisticsNo())) {
                    allExistExpressNoFlag.set(false);
                }
            });
            if (allExistExpressNoFlag.get() && StringUtils.isBlank(expressNo)
                && StringUtils.isBlank(orderDeliver.getLogisticsNo())) {
                allExistExpressNoFlag.set(false);
            }
            // 判断发货单是否都有物流单号,否情况为出库中
            if (!allExistExpressNoFlag.get()) {
                orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_OUTBOUNDING);
            } else {
                // 判断是否都已经发货
                AtomicReference<Boolean> deliverFlag = new AtomicReference<>(true);
                goods.forEach(orderGoodsDO -> {
                    if (orderGoodsDO.getUnDeliverCount() > 0) {
                        deliverFlag.set(false);
                    }
                });
                if (deliverFlag.get()) {
                    orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED);
                } else {
                    orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SOME_SHIPPED);
                }
            }
        }
    }

    /**
     * 组装同步第三方物流信息参数
     * 
     * @param orderDeliverBillDO
     * @return
     */
    public static OrderLogisticsSyncParam toOrderLogisticsSyncParam(OrderDeliverBillDO orderDeliverBillDO) {
        OrderLogisticsSyncParam param = new OrderLogisticsSyncParam();
        param.setOrderId(orderDeliverBillDO.getOrderId());
        param.setDistributionId(orderDeliverBillDO.getDistributionId());
        param.setDistributionName(orderDeliverBillDO.getDistributionName());
        param.setExpressCode(orderDeliverBillDO.getDistributionCode());
        param.setExpressNo(orderDeliverBillDO.getLogisticsNo());
        param.setExpressTime(orderDeliverBillDO.getDeliverTime().getTime());
        return param;
    }

    public static OrderDeliverSyncErpParam toOrderDeliverSyncErpParam(OrderDeliverBillDO orderDeliverBillDO) {
        OrderDeliverSyncErpParam erpParam = new OrderDeliverSyncErpParam();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        erpParam.setTime(simpleDateFormat.format(orderDeliverBillDO.getCreateTime()));
        erpParam.setDistributionName(orderDeliverBillDO.getDistributionName());
        erpParam.setLogisticNo(orderDeliverBillDO.getLogisticsNo());
        erpParam.setDeliverTime(simpleDateFormat.format(orderDeliverBillDO.getDeliverTime()));
        if (StringUtils.isNotBlank(orderDeliverBillDO.getDeliverErpNo())) {
            // 出库单已同步、不需要再次同步
            erpParam.setSyncOutbountFlag(false);
        }
        if (StringUtils.isNotBlank(orderDeliverBillDO.getDeliverStkNo())) {
            // 采购单已同步、不需要再次同步
            erpParam.setSyncPurchaseFlag(false);
        }
        return erpParam;
    }

    /**
     * 组装同步第三方物流信息
     * 
     * @param distributorId
     * @param orderDeliveryCmd
     * @param orderNo
     * @return
     */
    public static OrderLogistics toOrderLogistics(Integer distributorId, OrderDeliveryCmd orderDeliveryCmd,
        String orderNo) {
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setDistributorId(distributorId);
        orderLogistics.setExpressCode(orderDeliveryCmd.getExpressCode());
        orderLogistics.setExpressName(orderDeliveryCmd.getExpressName());
        orderLogistics.setExpressNo(orderDeliveryCmd.getExpressNo());
        orderLogistics.setExpressTime(orderDeliveryCmd.getExpressTime());
        // 三方单号（传过去的是B2B的单号）
        orderLogistics.setOtherOrderNo(orderNo);
        return orderLogistics;
    }

    /**
     *
     * @param orderErpNo
     * @param billId
     *            orderDeliverBillDO主键id
     * @param manufactors
     * @param orderGoodsDetailDTOS
     * @param time
     * @return
     */
    public DiyPurchaseOrderDTO getDiyPurchaseOrderDTO(String orderErpNo, Integer billId, String manufactors,
        List<OrderGoodsDetailDTO> orderGoodsDetailDTOS, String time) {
        DiyPurchaseOrderDTO diyPurchaseOrderDTO = new DiyPurchaseOrderDTO();
        diyPurchaseOrderDTO.setOrderErpNo(orderErpNo);
        diyPurchaseOrderDTO.setDetailDTOList(orderGoodsDetailDTOS);
        diyPurchaseOrderDTO.setFactoryNo(manufactors);
        diyPurchaseOrderDTO.setTime(time);
        diyPurchaseOrderDTO.setId(billId);
        return diyPurchaseOrderDTO;
    }

    /**
     * 组装柔性同步ERP出库单参数
     * 
     * @param orderErpNo
     * @param orderDeliverBillDO
     * @param manufactors
     *            工厂编码
     * @param orderGoodsDetailDTOS
     * @param time
     * @param distributionAmount
     * @return
     */
    public DiyOutboundSyncErpCmd getSyncErpOutboundParam(String orderErpNo, OrderDeliverBillDO orderDeliverBillDO,
        String manufactors, List<OrderGoodsDetailDTO> orderGoodsDetailDTOS, String time,
        BigDecimal distributionAmount) {
        // 下推销售出库单
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String deliverTime = simpleDateFormat.format(orderDeliverBillDO.getDeliverTime());

        DiyOutboundSyncErpCmd diyOutboundSyncErpCmd = new DiyOutboundSyncErpCmd();
        diyOutboundSyncErpCmd.setOrderErpNo(orderErpNo);
        diyOutboundSyncErpCmd.setDetailDTOList(orderGoodsDetailDTOS);
        diyOutboundSyncErpCmd.setFactoryNo(manufactors);
        diyOutboundSyncErpCmd.setTime(time);
        diyOutboundSyncErpCmd.setId(orderDeliverBillDO.getId());
        diyOutboundSyncErpCmd.setDeliverTime(deliverTime);
        diyOutboundSyncErpCmd.setDistributionAmount(distributionAmount);
        diyOutboundSyncErpCmd.setLogisticsNo(orderDeliverBillDO.getLogisticsNo());
        diyOutboundSyncErpCmd.setDistributionName(orderDeliverBillDO.getDistributionName());
        return diyOutboundSyncErpCmd;
    }

}
