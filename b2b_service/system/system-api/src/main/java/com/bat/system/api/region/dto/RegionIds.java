package com.bat.system.api.region.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "RegionId", description = "区域id")
public class RegionIds {
    @NotNull(message = "P_REGION_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private List<Integer> ids;
}
