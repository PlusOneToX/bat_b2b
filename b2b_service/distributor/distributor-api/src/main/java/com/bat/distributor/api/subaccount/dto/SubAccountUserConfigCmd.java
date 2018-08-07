package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubAccountUserConfigCmd {

/*    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;*/

    @ApiModelProperty(value = "分账金额类型 1、按照实付金额 2、按照利润金额")
    @NotNull(message = "D_SUB_ACCOUNT_AMOUNT_TYPE_NULL")
    private Short AmountType;

    @ApiModelProperty(value = "分账配置名称不能为空")
    @NotBlank(message = "D_SUB_ACCOUNT_CONFIG_NAME_NULL")
    private String name;

    @Valid
    @ApiModelProperty(value = "分账分销商等级比例列表")
    @NotEmpty(message = "D_SUB_ACCOUNT_LEVEL_RATIO_LIST_NULL")
    private List<SubAccountLevelRatioDTO> levelRatioList;

}
