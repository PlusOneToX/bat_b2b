package com.bat.order.service.cost.executor;

import static com.bat.order.mq.dto.OrderPayDTO.ALIPAY_VOUCHER_TYPE_ALIPAY_BIZ_VOUCHER;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.data.executor.OrderCustomerDataCmdExe;
import com.bat.order.service.order.executor.customer.UserCustomerOrderCmdExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.mq.dto.OrderPayDTO;

@Component
public class OrderCustomerCostCmdExe {

    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;

    @Resource
    private OrderCustomerDataCmdExe customerDataCmdExe;

    @Resource
    private UserCustomerOrderCmdExe userCustomerOrderCmdExe;

    @Resource
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostDOMapper;

    public void create(OrderCustomerCostDO orderCustomerCostDO) {
        orderCustomerCostMapper.insert(orderCustomerCostDO);
    }

    /**
     * C端订单支付（支持合并支付）
     * 
     * @param cmd
     */
    public void orderPay(OrderPayDTO cmd) {
        validParams(cmd);
        List<OrderCustomerCostDO> customerCostDOS =
            orderCustomerCostMapper.listByOrderIdsAndCustomerId(cmd.getOrderIds(), cmd.getCustomerId());
        OrderCostConvertor.orderPayCustomer(customerCostDOS, cmd);
        orderCustomerCostMapper.updateList(customerCostDOS);
        if (cmd.getAlipayVoucherBizFlag()) {
            List<OrderGoodsCustomerCostDO> orderGoodsCustomerCosts =
                orderGoodsCustomerCostDOMapper.listByOrderIdsAndCustomerId(cmd.getOrderIds(), cmd.getCustomerId());
            List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOS =
                OrderCostConvertor.orderPayGoodsCustomer(customerCostDOS, orderGoodsCustomerCosts, cmd);
            if (!CollectionUtils.isEmpty(orderGoodsCustomerCostDOS)) {
                orderGoodsCustomerCostDOMapper.batchUpdate(orderGoodsCustomerCostDOS);
            }
        }
        customerDataCmdExe.orderPay(cmd, customerCostDOS);
        if (cmd.getOrderIds() != null && cmd.getOrderIds().size() > 0) {
            for (Integer orderId : cmd.getOrderIds()) {
                // 如果是则需要同步过去
                userCustomerOrderCmdExe.dealFlagSendToSangxing(orderId);
            }
        }
    }

    /**
     * 数据计算 以及标志位 判断
     * 
     * @param cmd
     */
    private void validParams(OrderPayDTO cmd) {
        BigDecimal paidAmount = cmd.getPaidAmount();
        BigDecimal diffAmount = null;
        if (paidAmount != null && cmd.getReceiptAmount() != null && paidAmount.compareTo(cmd.getReceiptAmount()) > 0) {
            diffAmount = paidAmount.subtract(cmd.getReceiptAmount());
        }
        cmd.setDiffAmount(diffAmount);
        cmd.setAlipayVoucherBizFlag(
            diffAmount != null && ALIPAY_VOUCHER_TYPE_ALIPAY_BIZ_VOUCHER.equals(cmd.getAlipayVoucherType()));
    }

    /**
     * 更新退款金额
     *
     * @param orderId
     * @param refundedAmount
     */
    public void updateOrderRefundedAmount(Integer orderId, Integer distributorId, BigDecimal refundedAmount) {
        orderCustomerCostMapper.updateOrderRefundedAmount(orderId, distributorId, refundedAmount);
    }
}
