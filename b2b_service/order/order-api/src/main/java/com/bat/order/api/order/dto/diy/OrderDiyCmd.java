package com.bat.order.api.order.dto.diy;


import com.bat.order.api.order.dto.common.OrderCommonCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderDiyCmd extends OrderCommonCmd {

    @ApiModelProperty(value = "定制订单明细")
    @NotEmpty(message = "ORDER_DETAIL_LIST_EMPTY")
    @Valid
    private List<OrderDetailDiyDTO> diyDetailList;
}
