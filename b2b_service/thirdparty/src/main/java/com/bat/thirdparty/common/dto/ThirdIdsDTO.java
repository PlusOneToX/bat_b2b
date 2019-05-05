package com.bat.thirdparty.common.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdIdsDTO {

    @ApiModelProperty(value = "ids")
    @NotNull(message = "COMMON_ID_NULL")
    private List<Integer> ids;
}
