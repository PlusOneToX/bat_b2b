package com.bat.order.api.order.dto.orderquery.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 移动端 订单列表
 * @date: 2018/6/21 16:25
 */
@Data
public class UserMBOrderInfoCountQry {

    @ApiModelProperty(value = "查看常量确定")
    private Short searchType;

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "分销商id", example = "1")
    private Integer distributorId;

}
