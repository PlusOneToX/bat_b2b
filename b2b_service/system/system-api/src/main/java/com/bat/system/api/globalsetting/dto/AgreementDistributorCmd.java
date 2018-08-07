package com.bat.system.api.globalsetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 11:56
 */
@Data
@ApiModel(value = "AgreementDistributorCreateCmd", description = "协议分销商新增")
public class AgreementDistributorCmd {
    // @NotNull(message = "P_AGREEMENT_ID_NULL")
    @ApiModelProperty(value = "协议id", required = true, example = "1")
    private Integer agreementId;

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;
}
