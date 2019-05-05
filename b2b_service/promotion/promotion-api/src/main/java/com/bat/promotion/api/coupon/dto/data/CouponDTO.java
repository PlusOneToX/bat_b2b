package com.bat.promotion.api.coupon.dto.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class CouponDTO {
    @ApiModelProperty(value = "优惠券id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "优惠券名称", example = "优惠券名称")
    private String name;
    @ApiModelProperty(value = "优惠券描述", example = "促销活动描述")
    private String couponDesc;
    @ApiModelProperty(value = "优惠券使用说明", example = "优惠券使用说明")
    private String couponExplain;
    @ApiModelProperty(value = "优惠券作废说明", example = "优惠券作废说明")
    private String invalidExplain;
    @ApiModelProperty(value = "优惠券总数量", example = "10")
    private Integer generateCount;
    @ApiModelProperty(value = "已发放和领取数量", example = "10")
    private Integer generatedCount;
    @ApiModelProperty(value = "发放已使用数量", example = "10")
    private Integer usedCount;
    @ApiModelProperty(value = "每个客户限领数量", example = "10")
    private Integer limitCount;
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "优惠券状态： 0 未发布,1 未开始, 2 进行中, 3 已过期,4 提前结束 5已作废", example = "1")
    private Short couponStatus;
    @ApiModelProperty(value = "领券方式，1 自主领取, 2 人工发放, 3 自动发放", example = "1")
    private Short receivedType;
    @ApiModelProperty(value = "优惠类型：1 普通,4 新人, 2 Note 20专题, 3 S20 FE专题 ...  ", example = "1")
    private Short couponType;
    @ApiModelProperty(value = "优惠券编码", example = "1")
    private String couponCode;
    @ApiModelProperty(value = "优惠形式，1满减  2满折 3兑换", example = "1")
    private Short couponMethod;
    @ApiModelProperty(value = "订单满金额", example = "100.00")
    private BigDecimal orderMoney;
    @ApiModelProperty(value = "减免金额", example = "12.00")
    private BigDecimal reduction;
    @ApiModelProperty(value = "折扣", example = "3.8")
    private BigDecimal discount;
    @ApiModelProperty(value = "是否收取快递费 0否 1是", example = "1")
    private Short deliveryFeeFlag;
    @ApiModelProperty(value = "快递费", example = "3.8")
    private BigDecimal deliveryFee;
    @ApiModelProperty(value = "适用型号，1全部型号可用 2指定型号可用", example = "1")
    private Short modelScope;
    @ApiModelProperty(value = "适用材质，1全部材质可用 2指定材质可用 ", example = "1")
    private Short materialScope;
    @ApiModelProperty(value = "适用范围，1全部可用 2指定分销商可用", example = "1")
    private Short couponScope;

    @ApiModelProperty(value = "分销商适用列表")
    private List<CouponDistributorScopeDTO> distributors;
    @ApiModelProperty(value = "材质适用列表")
    private List<CouponMaterialScopeDTO> materials;
    @ApiModelProperty(value = "型号适用列表")
    private List<CouponModelScopeDTO> models;

}
