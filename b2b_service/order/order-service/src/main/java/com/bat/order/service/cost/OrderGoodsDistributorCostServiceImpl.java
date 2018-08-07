package com.bat.order.service.cost;

import java.util.List;

import com.bat.order.service.cost.executor.OrderGoodsDistributorCostCmdExe;
import com.bat.order.service.cost.executor.OrderGoodsDistributorCostQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.cost.OrderGoodsDistributorCostServiceI;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

@Service
public class OrderGoodsDistributorCostServiceImpl implements OrderGoodsDistributorCostServiceI {

    @Autowired
    private OrderGoodsDistributorCostCmdExe orderGoodsDistributorCostCmdExe;

    @Autowired
    private OrderGoodsDistributorCostQryExe orderGoodsDistributorCostQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createList(List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList) {
        orderGoodsDistributorCostCmdExe.createList(orderGoodsDistributorCostDOList);
    }

    @Override
    public Boolean checkIsGroup(Integer orderId) {
        Integer spellGroupId = orderGoodsDistributorCostQryExe.checkIsGroup(orderId);
        if (spellGroupId == null || spellGroupId == 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据订单id查询订单明细费用归属分销商的列表
     * 
     * @param orderId
     * @return
     */
    @Override
    public List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return orderGoodsDistributorCostQryExe.listByOrderIdAndDistributorId(orderId, distributorId);
    }
}
