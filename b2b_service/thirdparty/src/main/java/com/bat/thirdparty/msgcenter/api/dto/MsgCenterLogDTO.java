package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MsgCenterLogDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("推送开关 0关 1开；如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）")
    private Short pushSwitch;

    @ApiModelProperty("结果是否已经推送 0否 1是")
    private Short pushFlag;

    @ApiModelProperty("推送终端 1短信 2微信B2B小程序 3微信定制小程序4.抖音定制小程序")
    private Short pushTerminal;

    @ApiModelProperty("消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息")
    private Short msgType;

    @ApiModelProperty("接收的用户名称")
    private String toUsername;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("小程序推送消息体")
    private String miniBody;

    @ApiModelProperty("推送失败原因")
    private String sendFailError;

}
