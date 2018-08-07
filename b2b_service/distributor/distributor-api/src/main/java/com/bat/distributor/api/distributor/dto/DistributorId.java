package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistributorId", description = "分销商id")
public class DistributorId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "12424")
    private Integer id;
}
