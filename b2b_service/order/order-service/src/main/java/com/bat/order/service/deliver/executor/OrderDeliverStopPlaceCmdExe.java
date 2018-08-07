package com.bat.order.service.deliver.executor;

import com.bat.order.dao.deliver.OrderDeliverStopPlaceMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverStopPlaceDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderDeliverStopPlaceCmdExe {

    @Autowired
    private OrderDeliverStopPlaceMapper orderDeliverStopPlaceMapper;

    public void create(OrderDeliverStopPlaceDO orderDeliverStopPlaceDO) {
        orderDeliverStopPlaceMapper.insert(orderDeliverStopPlaceDO);
    }

    public void updateByPrimaryKeySelective(OrderDeliverStopPlaceDO orderDeliverStopPlaceDO) {
        orderDeliverStopPlaceMapper.updateByPrimaryKeySelective(orderDeliverStopPlaceDO);
    }

    public void delete(Integer id) {
        orderDeliverStopPlaceMapper.deleteByPrimaryKey(id);
    }
}
