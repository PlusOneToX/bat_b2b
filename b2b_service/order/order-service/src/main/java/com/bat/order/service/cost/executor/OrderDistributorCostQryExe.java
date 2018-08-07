package com.bat.order.service.cost.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;

@Component
public class OrderDistributorCostQryExe {
    @Autowired
    private OrderDistributorCostMapper orderDistributorCostDOMapper;

    /**
     * 根据订单id和分销商id获取订单分销层费用
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    public OrderDistributorCostDO getByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return orderDistributorCostDOMapper.getByOrderIdAndDistributorId(orderId, distributorId);
    }

    public List<OrderDistributorCostDO> getDirectPayInfoByOrderId(Integer orderId) {
        return orderDistributorCostDOMapper.getDirectPayInfoByOrderId(orderId);
    }
}
