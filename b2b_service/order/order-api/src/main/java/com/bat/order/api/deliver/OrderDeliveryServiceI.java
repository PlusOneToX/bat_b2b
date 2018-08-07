package com.bat.order.api.deliver;

import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;

public interface OrderDeliveryServiceI {
    /**
     * 新增发货单
     * @param orderDeliveryDO
     */
    void create(OrderDeliveryDO orderDeliveryDO);

    /**
     * 根据订单id查询订单送货信息
     * @param orderId
     * @return
     */
    OrderDeliveryDO getByOrderId(Integer orderId);

    void update(OrderDeliveryDO orderDeliveryDO);


}
