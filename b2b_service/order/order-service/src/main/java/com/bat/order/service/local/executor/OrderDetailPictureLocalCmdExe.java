package com.bat.order.service.local.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.local.OrderDetailPictureLocalDOMapper;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;

@Component
public class OrderDetailPictureLocalCmdExe {

    @Autowired
    private OrderDetailPictureLocalDOMapper orderDetailPictureLocalDOMapper;

    public void create(OrderDetailPictureLocalDO orderDetailPictureLocalDO) {
        orderDetailPictureLocalDOMapper.insert(orderDetailPictureLocalDO);
    }

    public void update(OrderDetailPictureLocalDO orderDetailPictureLocalDO) {
        orderDetailPictureLocalDOMapper.updateByPrimaryKey(orderDetailPictureLocalDO);
    }

    public void batchCreate(List<OrderDetailPictureLocalDO> createList) {
        orderDetailPictureLocalDOMapper.createList(createList);
    }

    public void batchUpdate(List<OrderDetailPictureLocalDO> updateLocalDOList) {
        orderDetailPictureLocalDOMapper.batchUpdate(updateLocalDOList);
    }
}
