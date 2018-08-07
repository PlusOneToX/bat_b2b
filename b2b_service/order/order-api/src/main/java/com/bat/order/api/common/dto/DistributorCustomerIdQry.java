package com.bat.order.api.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributorCustomerIdQry {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;
}
