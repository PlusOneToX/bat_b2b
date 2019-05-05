package com.bat.promotion.api.user.dto.distributor;

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
public class UserGroupSeckillListQry {
    @ApiModelProperty(value = "搜索内容，拼团秒杀名称/货品编码/商品编码", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "拼团秒杀：0 全部 1拼团 2秒杀(不填也为全部)", required = false, example = "1")
    private Short groupSeckillType;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
