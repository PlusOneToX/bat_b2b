package com.bat.distributor.api.base;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseIds", description = "ids列表")
public class BaseIds {

    @NotNull(message = "P_DISTRIBUTOR_IDS_NULL")
    @ApiModelProperty(value = "ids列表", required = true)
    private List<Integer> ids;
}
