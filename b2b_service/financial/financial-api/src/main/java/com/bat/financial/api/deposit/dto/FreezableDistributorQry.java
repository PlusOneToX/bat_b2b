package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:45
 */
@Data
@ApiModel(value = "DepositDetailQry", description = "可冻结分销商")
public class FreezableDistributorQry {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "分销商名称", example = "1")
    private String distributorName;

}
