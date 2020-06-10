package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExchangeSpecialDetailDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "分销商明细")
    private List<ExchangeSpecialDistributorCO> exchangeSpecialDistributorCOS;

}
