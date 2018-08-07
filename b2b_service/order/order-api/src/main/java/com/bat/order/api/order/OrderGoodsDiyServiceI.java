package com.bat.order.api.order;

import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;

import java.util.List;

public interface OrderGoodsDiyServiceI {
    /**
     * 新增订单定制详情记录
     * @param diyDOList
     */
    void createList(List<OrderGoodsDiyDO> diyDOList);

    /**
     * 根据订单id查询定制详情列表
     * @param orderId
     * @return
     */
    List<OrderGoodsDiyDO> listByOrderId(Integer orderId);

    /**
     * 批量修改
     * @param updateList
     */
    void updateList(List<OrderGoodsDiyDO> updateList);

    /**
     * 根据工厂编码查询未同步工厂的订单id
     * @param manufactors
     * @return
     */
    List<Integer> listUnSyncFactory(String manufactors);
}
