package com.bat.order.service.deliver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bat.order.api.deliver.OrderDeliverBillDetailServiceI;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.service.deliver.executor.OrderDeliverBillDetailCmdExe;
import com.bat.order.service.deliver.executor.OrderDeliverBillDetailQryExe;

@Service
public class OrderDeliverBillDetailServiceImpl implements OrderDeliverBillDetailServiceI {

    @Autowired
    private OrderDeliverBillDetailQryExe orderDeliverBillDetailQryExe;

    @Autowired
    private OrderDeliverBillDetailCmdExe orderDeliverBillDetailCmdExe;

    /**
     * 根据订单出库流水id查询明细列表
     * 
     * @param orderDeliverBillId
     * @return
     */
    @Override
    public List<OrderDeliverBillDetailDO> listByOrderDeliverBillId(Integer orderDeliverBillId) {
        return orderDeliverBillDetailQryExe.listByOrderDeliverBillId(orderDeliverBillId);
    }

    /**
     * 根据订单出库流水id列表删除明细
     * 
     * @param orderDeliverBillIdList
     */
    @Override
    public void deleteByOrderDeliverBillIds(List<Integer> orderDeliverBillIdList) {
        orderDeliverBillDetailCmdExe.deleteByOrderDeliverBillIds(orderDeliverBillIdList);
    }

    @Override
    public List<OrderDeliverBillDetailDO> listByOrderDeliverBillIdList(List<Integer> orderDeliverBillIdList) {
        return orderDeliverBillDetailQryExe.listByOrderDeliverBillIdList(orderDeliverBillIdList);
    }

    @Override
    public List<OrderDeliverBillDetailDO> listByOrderGoodsId(Integer orderGoodsId) {
        return orderDeliverBillDetailQryExe.listByOrderGoodsId(orderGoodsId);
    }

    @Override
    public void update(OrderDeliverBillDetailDO orderDeliverBillDetailDO) {
        orderDeliverBillDetailCmdExe.update(orderDeliverBillDetailDO);
    }

    @Override
    public List<OrderDeliverBillDetailDO> listByOrderIdList(List<Integer> orderIds) {
        return orderDeliverBillDetailQryExe.listByOrderIdList(orderIds);
    }
}
