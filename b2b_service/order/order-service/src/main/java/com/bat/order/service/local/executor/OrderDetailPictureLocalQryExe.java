package com.bat.order.service.local.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.local.OrderDetailPictureLocalDOMapper;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;

@Component
public class OrderDetailPictureLocalQryExe {

    @Autowired
    private OrderDetailPictureLocalDOMapper orderDetailPictureLocalDOMapper;

    public OrderDetailPictureLocalDO getByOrderGoodsDiyId(Integer orderGoodsDiyId) {
        return orderDetailPictureLocalDOMapper.getByOrderGoodsDiyId(orderGoodsDiyId);
    }

    public List<OrderDetailPictureLocalDO> listByOrderId(Integer orderId) {
        return orderDetailPictureLocalDOMapper.listByOrderId(orderId);
    }
}
