package com.bat.order.service.data;

import java.util.List;

import com.bat.order.service.data.executor.OrderDistributorDataQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.data.OrderDistributorDataServiceI;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;

@Service
public class OrderDistributorDataServiceImpl implements OrderDistributorDataServiceI {

    @Autowired
    private OrderDistributorDataQryExe orderDistributorDataQryExe;

    @Autowired
    private OrderDistributorDataCmdExe orderDistributorDataCmdExe;

    @Override
    public List<OrderDistributorDataDO> listByOrderId(Integer orderId) {
        return orderDistributorDataQryExe.listByOrderId(orderId);
    }

    /**
     *
     * @param orderId
     *            订单id
     * @param distributorId
     *            分销商id
     * @param erpFlag
     *            是否同步到erp 1.是 0.否
     * @return
     */
    @Override
    public List<OrderDistributorDataDO> listByCondition(Integer orderId, Integer distributorId, Short erpFlag) {
        return orderDistributorDataQryExe.listByCondition(orderId, distributorId, erpFlag);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(OrderDistributorDataDO orderDistributorDataDO) {
        orderDistributorDataCmdExe.updateOrderDistributorData(orderDistributorDataDO);
    }

    @Override
    public OrderDistributorDataDO getFirstTreeNode(Integer orderId) {
        return orderDistributorDataQryExe.getFirstTreeNode(orderId);
    }
}
