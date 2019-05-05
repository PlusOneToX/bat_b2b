package com.bat.promotion.api.groupseckill.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 19:46
 */
@Data
@ApiModel(description = "拼团秒杀活动列表查询")
public class GroupSeckillListQry {
    @ApiModelProperty(value = "搜索内容类型，1 拼团秒杀活动名称 3 货品编号 4 分销商", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "搜索内容，根据搜索内容类型", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", required = false, example = "0")
    private Short applyStatus;
    @ApiModelProperty(value = "拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束", required = false, example = "1")
    private Short groupSeckillStatus;
    @ApiModelProperty(value = "拼团秒杀：1拼团 2秒杀", required = false, example = "1")
    private Short groupSeckillType;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
