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
@ApiModel(description = "促销活动列表查询")
public class UserPromotionListQry {
    @ApiModelProperty(value = "搜索内容，活动名称", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "活动类型，0 全部活动 1 普通活动，2 阶梯活动(不填也为全部)", required = false, example = "1")
    private Short promoType;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
