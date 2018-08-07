package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserQrCodeQry", description = "分销商id")
public class UserQrCodeQry {
    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "123445")
    private Integer distributorId;
}
