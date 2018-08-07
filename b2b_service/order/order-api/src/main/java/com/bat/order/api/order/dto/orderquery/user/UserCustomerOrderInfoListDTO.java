package com.bat.order.api.order.dto.orderquery.user;

import java.util.List;

import com.bat.order.api.deliver.dto.data.OrderDeliveryDTO;
import com.bat.order.api.order.dto.orderquery.common.*;
import com.bat.order.api.order.dto.orderquery.common.*;

import lombok.Data;

/**
 * @author: lim
 * @description: 柔性订单列表数据
 * @date: 2018/6/21 16:25
 */
@Data
public class UserCustomerOrderInfoListDTO {
    /**
     * 订单基本信息
     */
    private OrderInfoDTO orderInfo;
    /**
     * 订单柔性定制费用信息
     */
    private OrderCustomerCostDTO orderCustomerCost;
    /**
     * 订单柔性定制数据信息
     */
    private OrderCustomerDataDTO orderCustomerData;
    /**
     * 收货信息
     */
    private OrderDeliveryDTO orderDelivery;
    /**
     * 发货单多个
     */
    private List<OrderDeliverDetailDTO> orderDeliverDetail;
    /**
     * 商品明细
     */
    private List<OrderInfoDetailGoodsDTO> orderInfoDetailGoods;
}
