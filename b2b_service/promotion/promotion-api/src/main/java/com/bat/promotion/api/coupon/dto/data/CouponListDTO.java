package com.bat.promotion.api.coupon.dto.data;

import java.util.Date;

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
public class CouponListDTO {
    @ApiModelProperty(value = "优惠券id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "优惠券名称", example = "优惠券名称")
    private String name;
    @ApiModelProperty(value = "优惠券描述", example = "促销活动描述")
    private String couponDesc;
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
}
