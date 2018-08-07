package com.bat.order.api.order.dto.orderquery.user;

import java.math.BigDecimal;
import java.util.List;

import com.bat.order.api.order.dto.orderquery.common.OrderDistributorDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 前台订单结果信息 没有单独的Qry类
 * @date: 2018/7/2 20:16
 */
@Data
public class UserPCDistributorOrderResultDTO {
    @ApiModelProperty("收货人")
    private String userName;
    @ApiModelProperty("总价")
    private BigDecimal amount;
    private List<UserDistributorOrderItemResultDTO> item;

    /**
     * @author: lim
     * @description: 前台订单结果行项目数据
     * @date: 2018/7/2 20:16
     */
    @Data
    public static class UserDistributorOrderItemResultDTO {
        @ApiModelProperty("类型 1 在途 2在库 3 定制")
        private Short type;
        private OrderInfoDTO orderInfo;
        private OrderTypeDTO orderType;
        private List<OrderInfoDetailGoodsDTO> orderInfoDetailGoods;
        private OrderDistributorDTO orderDistributor;
    }
}
