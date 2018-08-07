package com.bat.order.api.deliver;

import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;

import java.util.List;

public interface OrderDeliverBillDetailServiceI {

    /**
     * 根据订单出库流水id查询明细列表
     * @param orderDeliverBillId
     * @return
     */
    List<OrderDeliverBillDetailDO> listByOrderDeliverBillId(Integer orderDeliverBillId);

    /**
     * 根据订单出库流水id列表删除明细
     * @param deliverGoodsIds
     */
    void deleteByOrderDeliverBillIds(List<Integer> deliverGoodsIds);

    /**
     * 根据
     * @param orderDeliverBillIdList
     * @return
     */
    List<OrderDeliverBillDetailDO> listByOrderDeliverBillIdList(List<Integer> orderDeliverBillIdList);


    List<OrderDeliverBillDetailDO> listByOrderGoodsId(Integer orderGoodsId);

    void update(OrderDeliverBillDetailDO orderDeliverBillDetailDO);

    /**
     * 根据订单id列表查询订单发货明细列表
     * @param orderIds
     * @return
     */
    List<OrderDeliverBillDetailDO> listByOrderIdList(List<Integer> orderIds);
}
