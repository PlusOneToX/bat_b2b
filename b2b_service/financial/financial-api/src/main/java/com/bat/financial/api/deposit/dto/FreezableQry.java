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
@ApiModel(value = "FreezableQry", description = "冻结列表")
public class FreezableQry extends BaseSearchQry {

    public FreezableQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
    }

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "业务类型 1,提现冻结 2,其他冻结", example = "1")
    private Short businessType;

    @ApiModelProperty(value = "1.分销商名称", example = "1")
    private String distributorName;

}
