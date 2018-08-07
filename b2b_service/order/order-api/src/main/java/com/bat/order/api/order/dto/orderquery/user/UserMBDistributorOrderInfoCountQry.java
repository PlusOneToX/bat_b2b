package com.bat.order.api.order.dto.orderquery.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMBDistributorOrderInfoCountQry {

    @ApiModelProperty(value = "查看常量确定")
    private Short searchType;

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款", example = "1")
    private Short payStatus;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成", example = "1")
    private Short orderStatus;

    @ApiModelProperty(value = "发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)", example = "1")
    private Short deliverStatus;

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "分销商id", example = "1")
    private Integer distributorId;

}
