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
@ApiModel(description = "优惠券型号")
public class CouponModelScopeCmd {
    @NotNull(message = "P_PROMOTION_MODEL_ID_NULL")
    @ApiModelProperty(value = "型号id", example = "123456")
    private Integer modelId;
    @NotBlank(message = "P_PROMOTION_MODEL_NAME_NULL")
    @ApiModelProperty(value = "型号名称", example = "型号名称")
    private String modelName;
}
