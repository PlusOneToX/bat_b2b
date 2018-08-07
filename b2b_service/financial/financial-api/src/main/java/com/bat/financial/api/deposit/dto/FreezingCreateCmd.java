package com.bat.financial.api.deposit.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 11:57
 */
@Data
@ApiModel(value = "DepositDistributorFreezingCmd", description = "添加冻结")
public class FreezingCreateCmd {

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "1")
    private String distributorName;

    @ApiModelProperty(value = "冻结金额", example = "1")
    private BigDecimal freezingAmount;

    // @ApiModelProperty(value = "解冻金额", example = "1")
    // private BigDecimal thawAmount;

    @ApiModelProperty(value = "业务类型 1,提现冻结 2,其他冻结", example = "2")
    private Short businessType = 2;

    @ApiModelProperty(value = "业务单号 (业务类型为 2时，为空 ) ", example = "")
    private Integer businessId = null;

    @ApiModelProperty(value = "备注", example = "1")
    private String remark;

}
