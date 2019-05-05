package com.bat.promotion.api.promotion.dto;

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
public class PromotionListQry {
    @ApiModelProperty(value = "搜索内容类型，1 促销活动名称 2 商品编号 3 货品编号 4 分销商 5 规则标签 6 条件标签", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "搜索内容，根据搜索内容类型", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", required = false, example = "0")
    private Short applyStatus;
    @ApiModelProperty(value = "促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束", required = false, example = "1")
    private Short promoStatus;
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动", required = false, example = "1")
    private Short promoType;
    @ApiModelProperty(value = "活动来源，1 后台新增, 2 批量导入", required = false, example = "1")
    private Short promoSource;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
