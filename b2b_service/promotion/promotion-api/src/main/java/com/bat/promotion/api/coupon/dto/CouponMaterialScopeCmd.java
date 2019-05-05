package com.bat.promotion.api.coupon.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "优惠券材质")
public class CouponMaterialScopeCmd {
    @NotNull(message = "P_PROMOTION_MATERIAL_NULL")
    @ApiModelProperty(value = "材质id", example = "123456")
    private Integer materialId;
    @NotBlank(message = "P_PROMOTION_MATERIAL_NAME_NULL")
    @ApiModelProperty(value = "材质名称", example = "材质名称")
    private String materialName;
}
