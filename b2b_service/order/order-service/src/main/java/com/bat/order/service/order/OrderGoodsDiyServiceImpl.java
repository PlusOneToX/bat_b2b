package com.bat.order.service.order;

import java.util.Date;
import java.util.List;

import com.bat.order.service.order.executor.OrderGoodsDiyCmdExe;
import com.bat.order.service.order.executor.OrderGoodsDiyQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.order.OrderGoodsDiyServiceI;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;

@Service
public class OrderGoodsDiyServiceImpl implements OrderGoodsDiyServiceI {

    @Autowired
    private OrderGoodsDiyCmdExe orderGoodsDiyCmdExe;

    @Autowired
    private OrderGoodsDiyQryExe orderGoodsDiyQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createList(List<OrderGoodsDiyDO> diyDOList) {
        diyDOList.stream().forEach(orderGoodsDiyDO -> {
            orderGoodsDiyDO.setCreateTime(new Date());
            orderGoodsDiyDO.setUpdateTime(new Date());
            orderGoodsDiyCmdExe.create(orderGoodsDiyDO);
        });
    }

    /**
     * 根据订单id查询定制详情列表
     * 
     * @param orderId
     * @return
     */
    @Override
    public List<OrderGoodsDiyDO> listByOrderId(Integer orderId) {
        return orderGoodsDiyQryExe.listByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateList(List<OrderGoodsDiyDO> updateList) {
        if (updateList == null || updateList.size() == 0) {
            return;
        }
        orderGoodsDiyCmdExe.updateList(updateList);
    }

    /**
     * 根据工厂编码查询未同步工厂的订单id
     * 
     * @param manufactors
     * @return
     */
    @Override
    public List<Integer> listUnSyncFactory(String manufactors) {
        return orderGoodsDiyQryExe.listUnSyncFactory(manufactors);
    }

}
