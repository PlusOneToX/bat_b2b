package com.bat.promotion.api.promotion.dto;

import javax.validation.constraints.NotNull;

import com.bat.promotion.api.base.BaseIds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(value = "PromotionSubmitsCmd", description = "批量提交促销活动")
public class PromotionSubmitsCmd extends BaseIds {
    @NotNull(message = "P_PROMOTION_SUBMIT_NULL")
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败 ", required = true, example = "1")
    private Short applyStatus;
}
