package com.bat.financial.api.deposit.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:45
 */
@Data
@ApiModel(value = "DepositDetailQry", description = "预存款明细明细账查询")
public class DepositDetailAdminQry extends BaseSearchQry {

    public DepositDetailAdminQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        super.attributeMapper.put((short)2, "setAmount");
    }

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "分销商Id", example = "1")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "业务id", example = "1")
    private String businessId;

    @ApiModelProperty(value = "变化类型", example = "1")
    private Short changeType;

    @ApiModelProperty(value = "支付方式", example = "1")
    private Short payWay;

    @ApiModelProperty(value = "金额", example = "1")
    private BigDecimal amount;

    @ApiModelProperty(value = "业务类型 支持逗号拼接", example = "1")
    private String businessType;

    @ApiModelProperty(value = "分销客户预存款账户id", example = "1")
    private Integer depositDistributorId;

}
