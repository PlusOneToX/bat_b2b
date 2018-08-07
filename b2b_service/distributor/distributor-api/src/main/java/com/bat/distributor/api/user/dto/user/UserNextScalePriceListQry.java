package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserNextScalePriceListQry", description = "分销商价格等级列表查询")
public class UserNextScalePriceListQry {
    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "17946")
    private Integer distributorId;
    @ApiModelProperty(value = "状态, 1启用,0停用", required = false, example = "1")
    private Short openFlag;
}
