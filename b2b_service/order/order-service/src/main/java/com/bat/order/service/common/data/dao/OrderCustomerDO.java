package com.bat.order.service.common.data.dao;

import java.util.List;

import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/19 20:03
 */
@Data
public class OrderCustomerDO {
    /**
     * 订单基本信息
     */
    private OrderInfoDO order;
    /**
     * 订单明细
     */
    private List<OrderGoodsDO> orderGoodss;
    /**
     * 订单费用（C端客户）
     */
    private OrderCustomerCostDO orderCost;
    /**
     * 订单收货信息
     */
    private OrderDeliveryDO delivery;
    /**
     * 订单发票信息
     */
    private OrderInvoiceDO invoice;
    /**
     * 订单分销商信息
     */
    private OrderCustomerDataDO orderCustomerData;

}
