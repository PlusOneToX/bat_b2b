package com.bat.order.api.deliver.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/7 9:45
 */
@Data
public class OrderDeliveryTraceDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("发货单id")
    private Integer orderDeliverBillId;
    @ApiModelProperty("节点时间")
    private Date acceptTime;
    @ApiModelProperty("节点轨迹描述")
    private String acceptStation;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("发货状态/新增字段")
    private String logisticsStatus;
}
