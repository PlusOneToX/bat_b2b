package com.bat.order.service.deliver.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.deliver.OrderDeliverBillDetailDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;

@Component
public class OrderDeliverBillDetailCmdExe {

    @Autowired
    private OrderDeliverBillDetailDOMapper orderDeliverBillDetailDOMapper;

    public void create(OrderDeliverBillDetailDO billDetailDO) {
        orderDeliverBillDetailDOMapper.insert(billDetailDO);
    }

    public void deleteByOrderDeliverBillIds(List<Integer> orderDeliverBillIdList) {
        orderDeliverBillDetailDOMapper.deleteByOrderDeliverBillIds(orderDeliverBillIdList);
    }

    public void update(OrderDeliverBillDetailDO orderDeliverBillDetailDO) {
        orderDeliverBillDetailDOMapper.updateByPrimaryKey(orderDeliverBillDetailDO);
    }
}
