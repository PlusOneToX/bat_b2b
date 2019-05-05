package com.bat.promotion.api.coupon.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 19:46
 */
@Data
@ApiModel(description = "优惠券列表查询")
public class CouponListQry {
    @ApiModelProperty(value = "搜索内容类型，1 优惠券名称 2 材料名称 3 型号名称 4 分销商", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "搜索内容，根据搜索内容类型", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", required = false, example = "0")
    private Short applyStatus;
    @ApiModelProperty(value = "优惠券状态： 0 未发布,1 未开始, 2 进行中, 3 已过期,4 提前结束 5已作废", required = false, example = "1")
    private Short couponStatus;
    @ApiModelProperty(value = "领券方式，1 自主领取, 2 人工发放, 3 自动发放", required = false, example = "1")
    private Short receivedType;
    @ApiModelProperty(value = "优惠形式，1满减  2满折 3兑换", required = false, example = "1")
    private Short couponMethod;
    @ApiModelProperty(value = "优惠类型：1 普通,4 新人, 2 Note 20专题, 3 S20 FE专题 ...  ", required = false, example = "1")
    private Short couponType;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
