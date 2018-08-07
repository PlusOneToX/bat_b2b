package com.bat.order.api.order.dto.orderquery.user;

import com.bat.order.api.deliver.dto.data.OrderDeliveryDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.api.cost.dto.data.OrderDistributorCostDTO;
import com.bat.order.api.data.dto.data.OrderDistributorDataDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: lim
 * @description: 移动端 订单列表
 * @date: 2018/6/21 16:25
 */
@Data
public class UserMBDistributorOrderInfoListDTO {

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货（已发货） 5已关闭 6已完成 7 待付款 8 已拒绝 9出库中", example = "1")
    private Short frontOrderStatus;

    /**
     * 订单基本信息
     */
    private OrderInfoDTO orderInfo;
    /**
     * 订单费用信息
     */
    private OrderDistributorCostDTO orderDistributorCost;
    /**
     * 订单数据信息
     */
    private OrderDistributorDataDTO orderDistributorData;
    /**
     * 发货信息
     */
    private OrderDeliveryDTO orderDelivery;
    /**
     * 商品明细
     */
    private List<OrderInfoDetailGoodsDTO> orderInfoDetailGoods;

}
