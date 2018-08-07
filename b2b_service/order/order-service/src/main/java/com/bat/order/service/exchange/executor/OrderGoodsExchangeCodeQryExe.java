package com.bat.order.service.exchange.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.order.OrderGoodsExchangeCodeMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeListDO;

@Component
public class OrderGoodsExchangeCodeQryExe {

    @Autowired
    private OrderGoodsExchangeCodeMapper orderGoodsExchangeCodeMapper;

    public List<OrderGoodsExchangeCodeDO> listByOrderId(Integer orderId) {
        return orderGoodsExchangeCodeMapper.listByOrderId(orderId);
    }

    /**
     * 条件查询分页列表参数
     * 
     * @param startTime
     * @param endTime
     * @param orderStatus
     * @param content
     * @return
     */
    public List<OrderGoodsExchangeCodeListDO> listDOByCondition(Date startTime, Date endTime, Short orderStatus,
        String content) {

        return orderGoodsExchangeCodeMapper.listDOByCondition(startTime, endTime, orderStatus, content);
    }
}
