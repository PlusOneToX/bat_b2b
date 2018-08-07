package com.bat.distributor.api.group.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GroupId", description = "分销商分组id")
public class GroupId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商分组id", required = true, example = "12424")
    private Integer id;
}
