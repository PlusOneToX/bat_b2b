package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 15:22
 */
@Data
@ApiModel(value = "FreezingDeleteCmd", description = "解除冻结")
public class FreezingDeleteCmd {
    @NotNull(message = "P_DEPOSIT_FREEZING_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    Integer id;
}
