package com.bat.order.service.cost;

import java.util.Date;

import com.bat.order.service.cost.executor.OrderCustomerCostCmdExe;
import com.bat.order.service.cost.executor.OrderCustomerCostQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.cost.OrderCustomerCostServiceI;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;

@Service
public class OrderCustomerCostServiceImpl implements OrderCustomerCostServiceI {

    @Autowired
    private OrderCustomerCostCmdExe orderCustomerCostCmdExe;

    @Autowired
    private OrderCustomerCostQryExe orderCustomerCostQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrderCustomerCostDO orderCustomerCostDO) {
        orderCustomerCostDO.setCreateTime(new Date());
        orderCustomerCostDO.setUpdateTime(new Date());
        orderCustomerCostCmdExe.create(orderCustomerCostDO);
    }
}
