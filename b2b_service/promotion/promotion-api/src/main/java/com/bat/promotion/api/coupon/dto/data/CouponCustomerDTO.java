package com.bat.promotion.api.coupon.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "客户优惠券信息")
public class CouponCustomerDTO {
    @ApiModelProperty(value = "客户优惠券id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "优惠券id", example = "123456")
    private Integer couponId;
    @ApiModelProperty(value = "优惠券码", example = "123456")
    private String couponNo;
    @ApiModelProperty(value = "优惠券作废说明", example = "123456")
    private String invalidExplain;
    @ApiModelProperty(value = "状态，1 未开始,2 进行中, 3 已过期,5 已作废 6已使用", example = "1")
    private Short couponStatus;
    @ApiModelProperty(value = "客户id", example = "1")
    private Integer customerId;
    @ApiModelProperty(value = "客户名称", example = "1")
    private String customerName;
    @ApiModelProperty(value = "第三方用户系统编号,渠道ID", example = "123456")
    private String openId;
    @ApiModelProperty(value = "分销平台编码", example = "123456")
    private String platform;
    @ApiModelProperty(value = "归属分销商id", example = "123456")
    private Integer distributorId;
    @ApiModelProperty(value = "领取时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}