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
@ApiModel(description = "优惠券可视范围分销商")
public class CouponDistributorScopeCmd {
    @NotNull(message = "P_PROMOTION_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", example = "123456")
    private Integer distributorId;
    @NotBlank(message = "P_PROMOTION_DISTRIBUTOR_NAME_NULL")
    @ApiModelProperty(value = "分销商用户名（登录名）", example = "促销活动名称")
    private String name;
    @NotBlank(message = "P_PROMOTION_DISTRIBUTOR_COMPANY_NAME_NULL")
    @ApiModelProperty(value = "分销商公司名", example = "促销活动英文名称")
    private String companyName;
}
