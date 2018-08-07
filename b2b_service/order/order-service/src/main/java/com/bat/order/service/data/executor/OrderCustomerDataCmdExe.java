package com.bat.order.service.data.executor;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.data.convertor.OrderDataConvertor;
import com.bat.order.service.third.financial.FinancialCmdExeRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.order.dto.OrderPayStatusCmd;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.mq.dto.OrderPayDTO;
import com.bat.order.service.cost.executor.OrderCustomerCostCmdExe;
import com.bat.order.service.cost.executor.OrderCustomerCostQryExe;

@Component
public class OrderCustomerDataCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCustomerDataCmdExe.class);

    @Resource
    private OrderCustomerDataMapper orderCustomerDataDOMapper;
    @Resource
    private OrderCustomerCostCmdExe orderCustomerCostCmdExe;

    @Resource
    private OrderCustomerCostQryExe orderCustomerCostQryExe;

    @Resource
    private FinancialCmdExeRpc financialCmdExeRpc;

    /**
     * 订单付款，更新C段客户订单付款状态
     * 
     * @param cmd
     * @param customerCostDOS
     */
    public void orderPay(OrderPayDTO cmd, List<OrderCustomerCostDO> customerCostDOS) {
        List<OrderCustomerDataDO> customerDataDOS =
            orderCustomerDataDOMapper.listByOrderIdsAndCustomerId(cmd.getOrderIds(), cmd.getCustomerId());
        OrderDataConvertor.orderPayCustomer(cmd.getPadTime(), customerCostDOS, customerDataDOS);
        orderCustomerDataDOMapper.updateList(customerDataDOS);

    }

    /**
     * 更新订单分销层数据
     * 
     * @param customerDataDO
     */
    public void updateOrderCustomerData(OrderCustomerDataDO customerDataDO) {
        orderCustomerDataDOMapper.updateByPrimaryKey(customerDataDO);
    }

    /**
     * 更新订单付款状态
     * 
     * @param cmd
     */
    public void orderPayStatus(OrderPayStatusCmd cmd) {
        OrderCustomerCostDO orderCustomerCost =
            orderCustomerCostQryExe.getOrderCustomerCost(cmd.getOrderId(), cmd.getCustomerId());
        BigDecimal amount = orderCustomerCost.getPayAmount().subtract(cmd.getRefundedAmount());
        if (orderCustomerCost.getRefundedAmount() != null) {
            amount = amount.subtract(orderCustomerCost.getRefundedAmount());
        }
        if (amount.compareTo(new BigDecimal(0)) > 0) {
            orderCustomerDataDOMapper.updateOrderPayStatus(cmd.getOrderId(), cmd.getCustomerId(),
                PayStatus.PARTIAL_REFUND.getValue());
        } else {
            orderCustomerDataDOMapper.updateOrderPayStatus(cmd.getOrderId(), cmd.getCustomerId(), cmd.getPayStatus());
        }

        if (cmd.getRefundedAmount() != null) {
            orderCustomerCostCmdExe.updateOrderRefundedAmount(cmd.getOrderId(), cmd.getCustomerId(),
                cmd.getRefundedAmount());
        }
    }

}
