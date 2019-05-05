package com.bat.promotion.api.groupseckill.dto;

import javax.validation.constraints.NotNull;

import com.bat.promotion.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(value = "GroupSeckillSortCmd", description = "拼团秒杀排序更新")
public class GroupSeckillSortCmd extends BaseId {
    @NotNull(message = "P_PROMOTION_SORT_NULL")
    @ApiModelProperty(value = "排序号: 允许为负数", required = true, example = "1")
    private Integer sort;
}
