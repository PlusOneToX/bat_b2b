package com.bat.order.service.common.data.dao;

import java.util.List;

import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/19 20:03
 */
@Data
public class OrderDistributorDO {
    /**
     * 订单基本信息
     */
    private OrderInfoDO order;
    /**
     * 订单明细
     */
    private List<OrderGoodsDO> orderGoodss;
    /**
     * 订单费用（分销商归属）
     */
    private OrderDistributorCostDO orderCost;
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
    private OrderDistributorDataDO orderDistributorData;
    /**
     * 订单扩展数据
     */
    private OrderExtendDataDO extendData;
    /**
     * 定制订单类型
     */
    private OrderTypeDO diyOrderTypeDO;

}
