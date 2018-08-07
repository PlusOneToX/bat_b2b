package com.bat.distributor.api.user.dto.user.data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级特殊公式信息")
public class UserNextScalePriceSpecialDTO {
    @ApiModelProperty(value = "id", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "分销商价格等级id", example = "78445")
    private Integer nextScalePriceId;
    @ApiModelProperty(value = "算数运算符：1 乘 2 加 3 除 4 减", example = "1")
    private Short arithmeticType;
    @ApiModelProperty(value = "参加运算的数值", example = "1.9")
    private BigDecimal arithmeticNum;
    @ApiModelProperty(value = "品牌品类列表", example = "1.9")
    private List<UserNextScalePriceSpecialBrandCategoryDTO> brandCategorys;
}