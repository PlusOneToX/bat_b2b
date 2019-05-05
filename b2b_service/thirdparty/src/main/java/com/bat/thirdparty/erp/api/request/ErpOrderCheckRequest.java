package com.bat.thirdparty.erp.api.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by apple on 2019/7/10.
 */
@Data
@ApiModel(description = "ERP订单变更信息")
public class ErpOrderCheckRequest {

    @ApiModelProperty(value = "erp订单编号", required = true, example = "SO80462336544")
    @NotBlank(message = "P_THIRDPARTY_ORDER_NO_NULL")
    private String orderNo;
    @ApiModelProperty(value = "订单状态 1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭)", required = true, example = "1")
    @NotBlank(message = "P_THIRDPARTY_ORDER_STATUS_NULL")
    private Short orderStatus;
}
