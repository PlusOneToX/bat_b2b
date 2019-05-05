package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class MsgCenterWechatTemplateCmd {

    @NotNull(message = "P_THIRDPARTY_CHANNEL_NULL")
    @ApiModelProperty("消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

    @NotNull(message = "P_THIRDPARTY_TYPE_NULL")
    @ApiModelProperty("通知类型:1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知")
    private Short type;

    @NotBlank(message = "P_THIRDPARTY_TEMPLATE_ID_NULL")
    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("备注")
    private String remark;

}