package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExchangeShareCarryOutDTO {

    @ApiModelProperty(value = "转发专题发布id")
    private Integer exchangeSpecialReleaseId;

    @ApiModelProperty(value = "转发id")
    private Integer exchangeShareId;

    @ApiModelProperty(value = "专题id")
    private Integer exchangeSpecialId;

    @ApiModelProperty(value = "专题分销商配置id")
    private Integer exchangeSpecialDistributorId;

    @ApiModelProperty(value = "发卡分销商")
    private Integer distributorId;

    @ApiModelProperty(value = "转发用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "活动所属平台 1兑换商城")
    private Short activityPlatform;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "页面配图")
    private String pageImg;

    @ApiModelProperty(value = "活动页面引导文案")
    private String pageGuideText;

    @ApiModelProperty(value = "活动福利类型;1兑换卡")
    private Short type;

    @ApiModelProperty(value = "兑换活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "转发文案")
    private String forwardText;

    @ApiModelProperty(value = "活动转发封面")
    private String forwardImg;

    @ApiModelProperty(value = "朋友圈分享图")
    private String friendImg;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态 0停用 1启用")
    private Short status;

    @ApiModelProperty(value = "是否领取 0否 1是")
    private Short isReceive=0;

    @ApiModelProperty(value = "二维码地址")
    String qrCodeUrl;
}
