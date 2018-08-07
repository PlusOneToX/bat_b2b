package com.bat.distributor.api.user.dto.user;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级信息")
public class UserNextScalePriceCmd {
    @ApiModelProperty(value = "分销商价格等级id(更新时必填)", required = false, example = "78445")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "78445")
    private Integer distributorId;
    @NotBlank(message = "P_DISTRIBUTOR_NEXT_SCALE_PRICE_NULL")
    @ApiModelProperty(value = "分销商价格等级名称", required = true, example = "一级价")
    private String name;
    @ApiModelProperty(value = "算数运算符：1 乘 2 加 3 除 4 减", required = false, example = "1")
    private Short arithmeticType;
    @ApiModelProperty(value = "参加运算的数值(算数运算符不为空时必填)", required = false, example = "1.9")
    private BigDecimal arithmeticNum;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用,0停用", required = true, example = "1")
    private Short openFlag;
    @NotNull(message = "P_DISTRIBUTOR_SPECIAL_FLAG_NULL")
    @ApiModelProperty(value = "是否启用特殊公式, 1是,0否", required = true, example = "1")
    private Short specialFlag;
}