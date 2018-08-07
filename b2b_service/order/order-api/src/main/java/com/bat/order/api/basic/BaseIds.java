package com.bat.order.api.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "BaseIds", description = "ids列表")
public class BaseIds {

    @NotNull(message = "P_ORDER_IDS_NULL")
    @ApiModelProperty(value = "ids列表", required = true)
    private List<Integer> ids;
}
