package com.bat.promotion.api.coupon.dto;

import com.bat.promotion.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 19:46
 */
@Data
@ApiModel(description = "优惠券列表查询")
public class CouponCustomerListQry extends BasePage {
    @ApiModelProperty(value = "优惠id", required = false, example = "1")
    private Integer couponId;
    @ApiModelProperty(value = "分销平台编码", required = false, example = "123343")
    private String platform;
    @ApiModelProperty(value = "搜索内容类型，1 客户名称 2 优惠券码", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "客户名称", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "优惠券状态： 1 未开始, 2 进行中, 3 已过期,5 已作废 6 已使用", required = false, example = "1")
    private Short couponStatus;
}
