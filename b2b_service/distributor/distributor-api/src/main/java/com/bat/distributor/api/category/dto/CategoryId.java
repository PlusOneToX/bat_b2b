package com.bat.distributor.api.category.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CategoryId", description = "分销商类别id")
public class CategoryId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商类别id", required = true, example = "12424")
    private Integer id;
}
