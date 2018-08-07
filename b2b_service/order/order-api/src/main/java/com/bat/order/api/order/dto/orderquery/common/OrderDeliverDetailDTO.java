package com.bat.order.api.order.dto.orderquery.common;

import java.util.List;

import com.bat.order.api.deliver.dto.data.OrderDeliverBillDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliveryTraceDTO;
import com.bat.order.api.cost.dto.data.OrderGoodsDistributorCostDTO;

import lombok.Data;

/**
 * @author: lim
 * @description: 发货单 扩展信息
 * @date: 2018/7/9 14:19
 */
@Data
public class OrderDeliverDetailDTO {
    /**
     * 发货单
     */
    private OrderDeliverBillDTO orderDeliverBill;
    /**
     * 物流追踪
     */
    private List<OrderDeliveryTraceDTO> orderDeliveryTrace;

    /**
     * 商品
     */
    private List<OrderDeliverBillDetail> orderDeliverBillDetails;

    @Data
    public static class OrderDeliverBillDetail {

        /**
         * 商品信息
         */
        private OrderGoodsDTO orderGoods;

        /**
         * 发货单商品信息
         */
        private OrderDeliverBillDetailDTO orderDeliverBillDetail;

        /**
         * 商品费用明细
         */
        private OrderGoodsDistributorCostDTO orderGoodsDistributorCost;

    }
}
