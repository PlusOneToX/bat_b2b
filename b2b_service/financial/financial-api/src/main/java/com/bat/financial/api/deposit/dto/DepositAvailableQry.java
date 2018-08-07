package com.bat.financial.api.deposit.dto;

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
@ApiModel(value = "DepositAvailableQry", description = "账户余额查询")
public class DepositAvailableQry extends BaseSearchQry {

    public DepositAvailableQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        super.attributeMapper.put((short)2, "setDistributorAncestorName");
        super.attributeMapper.put((short)3, "setErpDistributorId");
    }

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "上级分销商名称", example = "1")
    private String distributorAncestorName;

    @ApiModelProperty(value = "ERP内码", example = "1")
    private String erpDistributorId;

}
