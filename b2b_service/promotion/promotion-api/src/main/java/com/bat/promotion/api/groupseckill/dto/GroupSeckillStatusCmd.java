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
@ApiModel(value = "GroupSeckillStatusCmd", description = "拼团秒杀状态更新")
public class GroupSeckillStatusCmd extends BaseId {
    @NotNull(message = "P_PROMOTION_STATUS_NULL")
    @ApiModelProperty(value = "状态：1、进行中 2、已暂停 4 提前结束", required = true, example = "1")
    private Short groupSeckillStatus;
}
