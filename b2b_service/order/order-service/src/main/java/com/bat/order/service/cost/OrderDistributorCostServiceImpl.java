package com.bat.order.service.cost;

import java.util.List;

import com.bat.order.service.cost.executor.OrderDistributorCostCmdExe;
import com.bat.order.service.cost.executor.OrderDistributorCostQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bat.order.api.cost.OrderDistributorCostServiceI;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;

@Service
public class OrderDistributorCostServiceImpl implements OrderDistributorCostServiceI {

    @Autowired
    private OrderDistributorCostCmdExe orderDistributorCostCmdExe;

    @Autowired
    private OrderDistributorCostQryExe orderDistributorCostQryExe;

    @Override
    public void create(OrderDistributorCostDO orderDistributorCostDO) {
        orderDistributorCostCmdExe.create(orderDistributorCostDO);
    }

    /**
     * 跟据订单id和分销商Id查询订单归属分销商费用
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    @Override
    public OrderDistributorCostDO getByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return orderDistributorCostQryExe.getByOrderIdAndDistributorId(orderId, distributorId);
    }

    @Override
    public List<OrderDistributorCostDO> getDirectPayInfoByOrderId(Integer orderId) {
        return orderDistributorCostQryExe.getDirectPayInfoByOrderId(orderId);
    }

}
