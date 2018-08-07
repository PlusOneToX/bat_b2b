package com.bat.order.api.data;

import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;

import java.util.List;

public interface OrderDistributorDataServiceI {
    /**
     * 根据订单id查询
     * @param orderId
     * @return
     */
    List<OrderDistributorDataDO> listByOrderId(Integer orderId);



    /**
     * 条件查询订单归属分销商数据
     * @param orderId 订单id
     * @param distributorId 分销商id
     * @param erpFlag 是否同步到erp 1.是 0.否
     * @return
     */
    List<OrderDistributorDataDO> listByCondition(Integer orderId,Integer distributorId,Short erpFlag);

    /**
     * 修改
     * @param orderDistributorDataDO
     */
    void update(OrderDistributorDataDO orderDistributorDataDO);

    /**
     * 根据订单id查询第一个节点的归属分销商数据
     * @param orderId
     * @return
     */
    OrderDistributorDataDO getFirstTreeNode(Integer orderId);
}
