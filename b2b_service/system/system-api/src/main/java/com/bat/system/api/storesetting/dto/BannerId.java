package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 18:44
 */
@Data
@ApiModel(value = "BannerId", description = "推广id")
public class BannerId {
    @NotNull(message = "P_BANNER_ID_NULL")
    @ApiModelProperty(value = "推广ID", required = true, example = "1")
    private Integer id;

}
