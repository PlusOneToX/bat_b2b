package com.bat.order.service.deliver.executor;

import com.bat.order.api.basic.BaseSearchQry;
import com.bat.order.dao.deliver.OrderDeliverStopPlaceMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverStopPlaceDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderDeliverStopPlaceQryExe {

    @Autowired
    private OrderDeliverStopPlaceMapper orderDeliverStopPlaceMapper;

    public int findMatch(String placeName) {
        return orderDeliverStopPlaceMapper.findMatch(placeName);
    }

    public List<OrderDeliverStopPlaceDO> selectList(BaseSearchQry qry) {
      return orderDeliverStopPlaceMapper.list(qry.getContent());
    }
}
