package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserDistributorMsgCenterLogDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("小程序跳转链接")
    private String miniLink;

    @ApiModelProperty("PC端跳转链接")
    private String pcLink;

    @ApiModelProperty("是否已经阅读 0否 1是")
    private Short readFlag;

    @ApiModelProperty("消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息")
    private Short msgType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("小程序推送消息体")
    private String miniBody;

}
