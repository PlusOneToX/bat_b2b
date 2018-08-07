package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 16:20
 */
@Data
@ApiModel(value = "WithdrawalQry", description = "提现列表")
public class WithdrawalQry extends BaseSearchQry {

    public WithdrawalQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        super.attributeMapper.put((short)2, "setId");
    }

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "状态 1,申请中,待确认 2,已确认 3,已拒绝", example = "1")
    private Short applyStatus;

    @ApiModelProperty(value = "提现方式：1.支付宝,2.微信,3.银行", example = "1")
    private Short withdrawAccountType;

    @ApiModelProperty(value = "1.分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "2.提现单编号", example = "1")
    private Integer id;

}