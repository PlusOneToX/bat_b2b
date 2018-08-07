package com.bat.distributor.api.user.dto.user.data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级信息")
public class UserNextScalePriceDTO {
    @ApiModelProperty(value = "分销商价格等级id", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "分销商id", example = "78445")
    private Integer distributorId;
    @ApiModelProperty(value = "分销商价格等级名称", example = "一级价")
    private String name;
    @ApiModelProperty(value = "算数运算符：1 乘 2 加 3 除 4 减", example = "1")
    private Short arithmeticType;
    @ApiModelProperty(value = "参加运算的数值", example = "1.9")
    private BigDecimal arithmeticNum;
    @ApiModelProperty(value = "是否启用特殊公式, 1是,0否", example = "0")
    private Short specialFlag;
    @ApiModelProperty(value = "状态, 1启用,0停用", example = "1")
    private Short openFlag;
    @ApiModelProperty(value = "特殊公式")
    private List<UserNextScalePriceSpecialDTO> scalePriceSpecials;
}