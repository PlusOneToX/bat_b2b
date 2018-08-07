package com.bat.order.service.cost;

import java.util.List;

import com.bat.order.service.cost.executor.OrderGoodsCustomerCostCmdExe;
import com.bat.order.service.cost.executor.OrderGoodsCustomerCostQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.cost.OrderGoodsCustomerCostServiceI;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;

@Service
public class OrderGoodsCustomerCostServiceImpl implements OrderGoodsCustomerCostServiceI {

    @Autowired
    private OrderGoodsCustomerCostCmdExe orderGoodsCustomerCostCmdExe;

    @Autowired
    private OrderGoodsCustomerCostQryExe orderGoodsCustomerCostQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createList(List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList) {
        orderGoodsCustomerCostCmdExe.createList(orderGoodsCustomerCostDOList);
    }
}
