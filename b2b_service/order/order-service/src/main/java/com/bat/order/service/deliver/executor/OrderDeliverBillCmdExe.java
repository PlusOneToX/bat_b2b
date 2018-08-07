package com.bat.order.service.deliver.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;

@Component
public class OrderDeliverBillCmdExe {

    @Autowired
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    public void create(OrderDeliverBillDO orderDeliverBillDO) {
        orderDeliverBillDOMapper.insert(orderDeliverBillDO);
    }

    public void update(OrderDeliverBillDO orderDeliverBillDO) {
        orderDeliverBillDOMapper.updateByPrimaryKey(orderDeliverBillDO);
    }

    public void batchDelete(List<Integer> idList) {
        orderDeliverBillDOMapper.batchDelete(idList);
    }

    public void batchUpdate(List<OrderDeliverBillDO> orderDeliverBillDOList) {
        orderDeliverBillDOMapper.batchUpdate(orderDeliverBillDOList);
    }
}
