package com.bat.order.api.order.dto.common;

import com.bat.order.dao.cost.dataobject.*;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoParamDTO {

    /**
     *
     */
    private Integer orderId;

    /**
     * order_goods列表
     */
    private List<OrderGoodsDO> orderGoodsDOList;

    /**
     * order_goods_diy列表
     */
    private List<OrderGoodsDiyDO> diyDOList;

    /**
     *订单明细费用客户归属列表
     */
    private List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList;


    /**
     * 订单明细费用归属分销商列表
     */
    private List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList;

    /**
     * 订单费用归属客户
     */
    private OrderCustomerCostDO orderCustomerCostDO;

    /**
     * 订单费用归属分销商
     */
    private OrderDistributorCostDO orderDistributorCostDO;

    /**
     * 订单发票对象
     */
    private OrderInvoiceDO orderInvoiceDO;
}
