package com.bat.order.service.deliver.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.deliver.OrderDeliverBillDetailDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;

@Component
public class OrderDeliverBillDetailQryExe {

    @Autowired
    private OrderDeliverBillDetailDOMapper orderDeliverBillDetailDOMapper;

    public List<OrderDeliverBillDetailDO> listByOrderDeliverBillId(Integer orderDeliverBillId) {
        return orderDeliverBillDetailDOMapper.listByOrderDeliverBillId(orderDeliverBillId);
    }

    public List<OrderDeliverBillDetailDO> listByOrderDeliverBillIdList(List<Integer> orderDeliverBillIdList) {
        return orderDeliverBillDetailDOMapper.listByOrderDeliverBillIdList(orderDeliverBillIdList);
    }

    public List<OrderDeliverBillDetailDO> listByOrderGoodsId(Integer orderGoodsId) {
        return orderDeliverBillDetailDOMapper.listByOrderGoodsId(orderGoodsId);
    }

    public List<OrderDeliverBillDetailDO> listMorethenCreateTime() {
        return orderDeliverBillDetailDOMapper.listMorethenCreateTime();
    }

    public List<OrderDeliverBillDetailDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderDeliverBillDetailDOMapper.listByOrderIdList(orderIdList);
    }
}
