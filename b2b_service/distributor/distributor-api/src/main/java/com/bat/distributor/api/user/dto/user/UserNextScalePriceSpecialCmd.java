package com.bat.distributor.api.user.dto.user;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级特殊公式信息")
public class UserNextScalePriceSpecialCmd {
    @ApiModelProperty(value = "分销商价格等级特殊公式id(更新时必填)", required = false, example = "78445")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_NEXT_SCALE_PRICE_ID_NULL")
    @ApiModelProperty(value = "分销商价格等级id", required = true, example = "78445")
    private Integer nextScalePriceId;
    @ApiModelProperty(value = "算数运算符：1 乘 2 加 3 除 4 减", required = false, example = "1")
    private Short arithmeticType;
    @ApiModelProperty(value = "参加运算的数值", required = false, example = "1.9")
    private BigDecimal arithmeticNum;
    @NotNull(message = "P_DISTRIBUTOR_NEXT_SCALE_PRICE_SPECIAL_NULL")
    @ApiModelProperty(value = "品牌品类列表", required = true)
    private List<UserNextScalePriceSpecialBrandCategoryCmd> brandCategorys;
}