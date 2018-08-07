package com.bat.order.service.message;

import static com.bat.order.service.common.Constant.COUNTERPARTY_TYPE_2;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.order.mq.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.order.mq.api.Sink;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.cost.executor.OrderCostCmdExe;
import com.bat.order.service.cost.executor.OrderCustomerCostCmdExe;
import com.bat.order.service.cost.executor.OrderDistributorCostCmdExe;
import com.bat.order.service.order.executor.OrderCmdExe;
import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import com.bat.order.tenant.TenantContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
@Slf4j
public class ReceiveService {

    private static Logger logger = LoggerFactory.getLogger(ReceiveService.class);

    @Resource
    private OrderCustomerCostCmdExe customerCostCmdExe;
    @Resource
    private OrderDistributorCostCmdExe distributorCostCmdExe;
    @Resource
    private MessageSendService sendService;
    @Resource
    private OrderCmdExe orderCmdExe;

    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;
    @Resource
    private OrderCostCmdExe orderCostCmdExe;
    @Resource
    private OrderConfig config;

    /**
     * 订单付款成功消息
     * 
     * @param dto
     */

    @StreamListener(value = Sink.ORDER_FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderPay'")
    public void orderPayInput(@Headers Map headers, @Payload OrderPayDTO dto) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderPayInput(dto);
        TenantContext.removeTenantNo();
    }

    @Transactional(rollbackFor = Exception.class)
    public void orderPayInput(OrderPayDTO dto) {
        if (dto.getCounterpartyType().equals(COUNTERPARTY_TYPE_2)) {
            // C端订单收款成功（支付宝 无资金商户优惠券 目前 分销端没有 只有小程序）
            customerCostCmdExe.orderPay(dto);
        } else {
            // 分销订单收款成功
            distributorCostCmdExe.orderPay(dto);
        }
        // 发送分销层级数据消息
        List<OrderTreeNodeDataDTO> dataDTOS = MessageConvertor.toOrderTreeNodeDataDTO(dto);
        sendService.orderTreeNodeData(dataDTOS);
    }

    /**
     * 订单生成分销层级数据消息
     * 
     * @param dto
     */
    @StreamListener(value = Sink.ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderTreeNodeData'")
    public void orderTreeNodeDataInput(@Headers Map headers, @Payload OrderTreeNodeDataDTO dto) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderCmdExe.createOrderTreeNodeData(dto);
        TenantContext.removeTenantNo();
    }

    /**
     * 分销订单在线支付未支付自动关闭
     *
     * @param orderTimer
     */
    @StreamListener(value = Sink.ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderTimerPowerOffD'")
    public void orderTimerPowerOffDInput(@Headers Map headers, @Payload OrderTimerDTO orderTimer) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderCmdExe.orderTimerPowerOff(orderTimer);
        TenantContext.removeTenantNo();
    }

    /**
     * C端客户订单在线支付未支付自动关闭
     *
     * @param orderTimer
     */
    @StreamListener(value = Sink.ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderTimerPowerOffC'")
    public void orderTimerPowerOffCInput(@Headers Map headers, @Payload OrderTimerDTO orderTimer) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderCmdExe.orderTimerPowerOff(orderTimer);
        TenantContext.removeTenantNo();
    }

    /**
     * 分销商基本信息修改（名称）
     *
     * @param dto
     */
    @StreamListener(value = Sink.ORDER_DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'distributorNameChange'")
    public void distributorNameChange(@Headers Map headers, @Payload DistributorNameChangeDTO dto) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        logger.info("order receive: " + dto);
        TenantContext.removeTenantNo();
    }

    /**
     * 订单商品锁库记录解锁
     *
     * @param orderGoodsStockUnLockList
     */
    @StreamListener(value = Sink.ORDER_WAREHOUSE_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderGoodsStockUnLock'")
    public void orderGoodsStockUnLock(@Headers Map headers,
        @Payload List<OrderGoodsStockSimpleDTO> orderGoodsStockUnLockList) {
        logger.info("order receive: " + orderGoodsStockUnLockList);
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderGoodsStockCmdExe.unLock(orderGoodsStockUnLockList);
        TenantContext.removeTenantNo();
    }

    /**
     * 订单变更更新层级费用数据
     *
     * @param orderId
     */
    @StreamListener(value = Sink.ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderChangeCost'")
    public void orderChangeCost(@Headers Map headers, @Payload Integer orderId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderCostCmdExe.orderChangeCost(orderId);
        TenantContext.removeTenantNo();
    }

    /**
     * 根据ERP订单号和行序号列表解锁B2B订单
     *
     * @param orderErpNoLineDTOS
     */
    @StreamListener(value = Sink.ORDER_WAREHOUSE_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'unLockByOrderErpNoLine'")
    public void unLockByOrderErpNoLine(@Headers Map headers, @Payload List<OrderErpNoLineDTO> orderErpNoLineDTOS) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderGoodsStockCmdExe.unLockOrderErpNoLineDTO(orderErpNoLineDTOS);
        TenantContext.removeTenantNo();
    }

}
