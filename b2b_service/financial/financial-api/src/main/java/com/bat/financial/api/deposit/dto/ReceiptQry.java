package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 19:45
 */
@Data
@ApiModel(value = "ReceiptQry", description = "收款列表")
public class ReceiptQry extends BaseSearchQry {
    public ReceiptQry() {
        // super.attributeMapper.put((short)1, "setDistributorName");
        // super.attributeMapper.put((short)2, "setAmount");
    }

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "支付方式", example = "1")
    private Short payWay;

    @ApiModelProperty(value = "业务类型 1充值 8 分销佣金（订单收款）", example = "1")
    private String businessType;
}
