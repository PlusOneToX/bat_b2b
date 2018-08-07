package com.bat.order.service.order.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;

@Component
public class OrderGoodsDiyQryExe {

    @Autowired
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    public List<OrderGoodsDiyDO> listByOrderId(Integer orderId) {
        return orderGoodsDiyDOMapper.listByOrderId(orderId);
    }

    public OrderGoodsDiyDO getById(Integer orderGoodsDiyId) {
        return orderGoodsDiyDOMapper.selectByPrimaryKey(orderGoodsDiyId);
    }

    /**
     * 根据工厂编码查询未同步的订单
     *
     * @param manufactors
     * @return
     */
    public List<Integer> listUnSyncFactory(String manufactors) {
        return orderGoodsDiyDOMapper.listUnSyncFactory(manufactors);
    }

    /**
     * 根据时间查询未同步的订单
     *
     * @param startTime
     * @return
     */
    public List<Integer> listUnSyncFactoryByStartTime(Date startTime) {
        return orderGoodsDiyDOMapper.listUnSyncFactoryByStartTime(startTime);
    }

    public List<OrderGoodsDiyDO> findLatelyNullLabel() {
        return orderGoodsDiyDOMapper.findLatelyNullLabel();
    }

    public List<Integer> needToSyncOrders(String manufactor) {
        return orderGoodsDiyDOMapper.needToSyncOrders(manufactor);
    }
}
