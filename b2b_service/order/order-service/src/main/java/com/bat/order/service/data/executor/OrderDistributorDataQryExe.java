package com.bat.order.service.data.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;

@Component
public class OrderDistributorDataQryExe {

    @Autowired
    private OrderDistributorDataMapper orderDistributorDataMapper;

    public List<OrderDistributorDataDO> listByOrderId(Integer orderId) {
        return orderDistributorDataMapper.listByOrderId(orderId);
    }

    /**
     * 根据ERP订单号查询归属分销商数据
     * 
     * @param erpFlag
     *            是否同步到ERP 1、是 0、否
     * @param orderErpNo
     * @return
     */
    public List<OrderDistributorDataDO> getByOrderErpNoAndErpFlag(String orderErpNo, Short erpFlag) {
        return orderDistributorDataMapper.listByOrderErpNoAndErpFlag(orderErpNo, erpFlag);
    }

    /**
     * 根据订单id和分销商id获取分销层级数据
     *
     * @param orderId
     * @param distributorId
     * @return
     */
    public OrderDistributorDataDO getOrderDistributorData(Integer orderId, Integer distributorId) {
        return orderDistributorDataMapper.getByOrderIdAndDistributorId(orderId, distributorId);
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
    public List<OrderDistributorDataDO> listByCondition(Integer orderId, Integer distributorId, Short erpFlag) {
        return orderDistributorDataMapper.listByCondition(orderId, distributorId, erpFlag);
    }

    public OrderDistributorDataDO getFirstTreeNode(Integer orderId) {
        return orderDistributorDataMapper.getFirstTreeNode(orderId);
    }

    /**
     * 获取未同步erp订单id列表
     * 
     * @return
     */
    public List<Integer> getNotErpOrderIds(Date startTime) {
        List<Integer> orderIds = orderDistributorDataMapper.getNotErpOrderIds(startTime);
        return orderIds;
    }

}
