package com.bat.order.api.order.dto.orderquery.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: lim
 * @description: 移动端 订单列表
 * @date: 2018/6/21 16:25
 */
@Data
@AllArgsConstructor
public class UserMBOrderInfoCountDTO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "数量", example = "1")
    private Integer count;

}
