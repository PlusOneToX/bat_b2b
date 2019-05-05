package com.bat.promotion.api.promotion.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(value = "PromotionDistributorScopeDTO", description = "促销活动可视范围分销商")
public class PromotionDistributorScopeDTO {
    @ApiModelProperty(value = "分销商id", example = "123456")
    private Integer distributorId;
    @ApiModelProperty(value = "分销商用户名（登录名）", example = "促销活动名称")
    private String name;
    @ApiModelProperty(value = "分销商公司名", example = "促销活动英文名称")
    private String companyName;
}
