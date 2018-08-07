package com.bat.order.api.data;

import com.bat.order.dao.data.dataobject.OrderExtendDataDO;

public interface OrderExtendDataServiceI {

    /**
     * 根据订单id查询扩展数据
     * @param orderId
     * @return
     */
    OrderExtendDataDO getByOrderId(Integer orderId);

    /**
     * 根据工厂单号查询订单扩展数据表
     * @param orderFactoryNo
     * @return
     */
    OrderExtendDataDO getByOrderFactoryNo(String orderFactoryNo);

    /**
     * 让ERP单号去查询
     * @param orderErpNo
     * @return
     */
    OrderExtendDataDO getByOrderErpNo(String orderErpNo);

    /**
     * 修改订单扩展数据
     * @param orderExtendDataDO
     */
    void update(OrderExtendDataDO orderExtendDataDO);
}
