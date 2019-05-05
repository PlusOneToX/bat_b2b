package com.bat.promotion.api.user.dto.customer.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "优惠券列表信息")
public class UserCustomerCouponDTO {
    @ApiModelProperty(value = "券码ID", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "优惠券Id", example = "123456")
    private Integer couponId;
    @ApiModelProperty(value = "优惠券券码", example = "GKXHCXRWIMD8")
    private String couponNo;
    @ApiModelProperty(value = "优惠券名称", example = "123456")
    private String couponName;
    @ApiModelProperty(value = "优惠券描述", example = "123456")
    private String couponDesc;
    @ApiModelProperty(value = "优惠券使用说明", example = "123456")
    private String couponExplain;
    @ApiModelProperty(value = "优惠券作废说明", example = "123456")
    private String invalidExplain;
    @ApiModelProperty(value = "优惠券状态： 0 未发布,1 未开始, 2 进行中, 3 已过期,4 提前结束 5已作废 6 已使用", example = "2")
    private Short couponStatus;
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "优惠形式，1满减  2满折 3兑换", example = "1")
    private Short couponMethod;
    @ApiModelProperty(value = "订单满金额", example = "10.9")
    private BigDecimal orderMoney;
    @ApiModelProperty(value = "减免金额", example = "10.9")
    private BigDecimal reduction;
    @ApiModelProperty(value = "折扣", example = "10.9")
    private BigDecimal discount;
    @ApiModelProperty(value = "优惠类型：1 普通,4 新人, 2 Note 20专题, 3 S20 FE专题 ...  ", example = "1")
    private Short couponType;
    @ApiModelProperty(value = "是否收取快递费 0否 1是", example = "0")
    private Short deliveryFeeFlag;
    @ApiModelProperty(value = "快递费", example = "8.00")
    private BigDecimal deliveryFee;
    @ApiModelProperty(value = "适用型号，1全部型号可用 2指定型号可用", example = "1")
    private Short modelScope;
    @ApiModelProperty(value = "适用材质，1全部材质可用 2指定材质可用", example = "1")
    private Short materialScope;
    @ApiModelProperty(value = "是否已领取: 1是 0否", example = "1")
    private Short receivedFlag;
    @ApiModelProperty(value = "商品是否能用, 1 能用 0 不能用", example = "1")
    private Short goodsEnable;
    @ApiModelProperty(value = "金额是否到达要求，1 达到，0 未达到(针对根据商品列表获取优惠券列表使用,注意：此条件只是代表购买条件已达到优惠券条件，需结合优惠券状态一起判断（因为优惠券有可能已过期）)",
        example = "1")
    private Short amountEnable;

    @ApiModelProperty(value = "材质ids(适用材质为2时有值)")
    private List<Integer> materialIds;
    @ApiModelProperty(value = "型号ids(适用型号为2时有值)")
    private List<Integer> modelIds;
    @ApiModelProperty(value = "领取时间（已领取才有）", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}