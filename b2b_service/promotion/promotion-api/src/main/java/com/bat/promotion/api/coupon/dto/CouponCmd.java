package com.bat.promotion.api.coupon.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "优惠券信息")
public class CouponCmd {
    @ApiModelProperty(value = "优惠券id(编辑时有值)", required = false, example = "123456")
    private Integer id;
    @NotBlank(message = "P_PROMOTION_COUPON_NAME_NULL")
    @ApiModelProperty(value = "优惠券名称", required = true, example = "优惠券名称")
    private String name;
    @ApiModelProperty(value = "优惠券描述", required = false, example = "促销活动描述")
    private String couponDesc;
    @ApiModelProperty(value = "优惠券使用说明", required = false, example = "优惠券使用说明")
    private String couponExplain;
    @ApiModelProperty(value = "优惠券作废说明", required = false, example = "优惠券作废说明")
    private String invalidExplain;
    @NotNull(message = "P_PROMOTION_COUPON_GENERATE_COUNT_NULL")
    @ApiModelProperty(value = "优惠券总数量(不填或填0不限制)", required = true, example = "10")
    private Integer generateCount;
    @NotNull(message = "P_PROMOTION_COUPON_LIMIT_COUNT_NULL")
    @ApiModelProperty(value = "每个客户限领数量(不填或填0不限制)", required = true, example = "10")
    private Integer limitCount;
    @NotNull(message = "P_PROMOTION_START_TIME_NULL")
    @ApiModelProperty(value = "开始时间", required = true, example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @NotNull(message = "P_PROMOTION_END_TIME_NULL")
    @ApiModelProperty(value = "结束时间", required = true, example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", required = false, example = "1")
    private Short applyStatus;
    @NotNull(message = "P_PROMOTION_COUPON_RECEIVED_TYPE_NULL")
    @ApiModelProperty(value = "领券方式，1 自主领取, 2 人工发放, 3 自动发放", required = true, example = "1")
    private Short receivedType;
    @NotNull(message = "P_PROMOTION_COUPON_TYPE_NULL")
    @ApiModelProperty(value = "优惠类型：1 普通,4 新人, 2 Note 20专题, 3 S20 FE专题 ...  ", required = true, example = "1")
    private Short couponType;
    @ApiModelProperty(value = "优惠券编码(专题为必填)", required = false, example = "1")
    private String couponCode;
    @NotNull(message = "P_PROMOTION_COUPON_METHOD_NULL")
    @ApiModelProperty(value = "优惠形式，1满减  2满折 3兑换", required = true, example = "1")
    private Short couponMethod;
    @NotNull(message = "P_PROMOTION_COUPON_ORDER_MONEY_NULL")
    @ApiModelProperty(value = "订单满金额", required = true, example = "100.00")
    private BigDecimal orderMoney;
    @ApiModelProperty(value = "减免金额(优惠形式为1时必填)", required = false, example = "12.00")
    private BigDecimal reduction;
    @ApiModelProperty(value = "折扣(优惠形式为2时必填)", required = false, example = "3.8")
    private BigDecimal discount;
    @NotNull(message = "P_PROMOTION_COUPON_DELIVERY_FEE_FLAG_NULL")
    @ApiModelProperty(value = "是否收取快递费 0否 1是", required = true, example = "1")
    private Short deliveryFeeFlag;
    @ApiModelProperty(value = "快递费(是否收取快递费为1时必填)", required = false, example = "3.8")
    private BigDecimal deliveryFee;
    @NotNull(message = "P_PROMOTION_COUPON_MODEL_SCOPE_NULL")
    @ApiModelProperty(value = "适用型号，1全部型号可用 2指定型号可用", required = true, example = "1")
    private Short modelScope;
    @NotNull(message = "P_PROMOTION_COUPON_MATERIAL_SCOPE_NULL")
    @ApiModelProperty(value = "适用材质，1全部材质可用 2指定材质可用 ", required = true, example = "1")
    private Short materialScope;
    @NotNull(message = "P_PROMOTION_COUPON_SCOPE_NULL")
    @ApiModelProperty(value = "适用范围，1全部可用 3指定分销商可用", required = true, example = "1")
    private Short couponScope;
    @Valid
    @ApiModelProperty(value = "分销商适用列表(适用范围为2是必填)", required = false)
    private List<CouponDistributorScopeCmd> distributors;
    @Valid

    @ApiModelProperty(value = "材质适用列表(适用材质为2是必填)", required = false)
    private List<CouponMaterialScopeCmd> materials;
    @Valid
    @ApiModelProperty(value = "型号适用列表(适用型号为2是必填)", required = false)
    private List<CouponModelScopeCmd> models;

}
