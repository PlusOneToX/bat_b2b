package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ExchangeShareCmd {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "活动所属平台 1兑换商城 2定制商城")
    private Short activityPlatform;

    @ApiModelProperty(value = "减免优惠活动名称")
    private String preferName;

    @ApiModelProperty(value = "活动位置 1确认订单页 2订单详情页")
    private Short seat;

    @ApiModelProperty(value = "转发按钮文案")
    private String forwardButtonText;

    @ApiModelProperty(value = "是否减免优惠 0否 1是")
    private Short reduceFlag;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "专题活动id")
    private Integer exchangeSpecialId;

    @ApiModelProperty(value = "分销商可视类型 1全部 2指定")
    private Short distributorVisualType;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态 0禁用 1启用")
    private Short status;

    @ApiModelProperty(value = "分销商ids")
    private List<Integer> distributorIds;

}
