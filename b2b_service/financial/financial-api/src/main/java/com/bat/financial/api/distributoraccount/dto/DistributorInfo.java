package com.bat.financial.api.distributoraccount.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/9 9:46
 */
@Data
public class DistributorInfo {
    @NotNull(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "")
    private String distributorName;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "分销商公司名称", required = true, example = "")
    private String distributorCompanyName;
}
