package com.bat.order.service.order.executor.distributor;

import com.alibaba.fastjson.JSON;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.service.common.data.dao.OrderDistributorDO;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.PayStatus;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
class UserDistributorOrderCmdExeTest {

    void calcRebateVoucherAmount() {
    }

    public static void main(String[] args) {
        UserDistributorOrderCmdExe cmdExe = new UserDistributorOrderCmdExe();
        String str = "";
        BigDecimal rebateVoucherAmountSum = new BigDecimal("343.25");
        BigDecimal sumPayAmount = new BigDecimal("343.25");;
        List<OrderDistributorDO> orderDistributorDOS = JSON.parseArray(str,OrderDistributorDO.class);
        for (OrderDistributorDO orderDistributorDO : orderDistributorDOS) {
            OrderDistributorCostDO orderCost = orderDistributorDO.getOrderCost();
            // （旧）订单商品金额 - 促销金额 + 物流费 = 分销商归属应收款总额
            // （新）订单商品金额 - 促销金额 + 物流费 - 抵扣金额 = 分销商归属应收款总额
            // 该订单 需要抵扣的返利金额（多个订单拆分的情况）
            BigDecimal newGoodsRebateAmount = cmdExe.orderRebate(rebateVoucherAmountSum, sumPayAmount, orderCost);
            BigDecimal newGoodsAmountSum = cmdExe.orderDetailRebate(orderDistributorDO, orderCost, newGoodsRebateAmount);
            // ====================================数据矫正======================================
            log.info("====================================数据矫正======================================");
            // 订单（商品-促销）的金额 与 明细商品（单价-促销）*数量 是否相等
            BigDecimal orderGoodsRebateAmount = orderCost.getPayAmount().subtract(orderCost.getDistributionAmount());
            BigDecimal difference = orderGoodsRebateAmount.subtract(newGoodsAmountSum);
            if (difference.compareTo(BigDecimal.ZERO) != 0) {
                log.info("订单商品的金额 与 明细商品单价*数量，orderGoodsRebateAmount：{},newGoodsAmountSum：{}", orderGoodsRebateAmount,
                        newGoodsAmountSum);
//                List<Integer> arr = Arrays.asList(1, 2, 4, 8, null);
//                for (Integer itemCount : arr) {
//                    if (correct(orderDistributorDO, difference, itemCount)) {
//                        break;
//                    }
//                }
            }
//            // 抵扣之后金额为0
//            if (orderCost.getPayAmount().equals(BigDecimal.ZERO)) {
//                OrderDistributorDataDO orderDistributorData = orderDistributorDO.getOrderDistributorData();
//                orderDistributorData.setPayStatus(PayStatus.PAID.getValue());
//                orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
//            }
        }
    }
}