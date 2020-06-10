package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExchangeCodeTransferRecordDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "兑换活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "兑换码id")
    private Integer exchangeCodeId;

    @ApiModelProperty(value = "转赠发起人ID")
    private Integer fromUserId;

    @ApiModelProperty(value = "转赠发起人名称")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否已收取 0否 1是")
    private Short receiveFlag;

    @ApiModelProperty(value = "收卡人")
    private Integer toUserId;

    @ApiModelProperty(value = "收卡时间")
    private Date receiveTime;

    @ApiModelProperty(value = "转赠文案")
    private String transferText;

    @ApiModelProperty(value = "转赠封面图")
    private String transferImg;

    @ApiModelProperty(value = "收取页面文案")
    private String receiveText;

    @ApiModelProperty(value = "收取卡片页面配图")
    private String receiveImg;

    @ApiModelProperty(value = "转赠发起人昵称")
    private String nickName;

    @ApiModelProperty(value = "转赠发起人头像")
    private String headImg;

}
