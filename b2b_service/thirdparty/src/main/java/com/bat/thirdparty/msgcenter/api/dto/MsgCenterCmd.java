package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MsgCenterCmd {

    @ApiModelProperty("主键")
    private Integer id;

    @NotNull(message = "P_THIRDPARTY_CHANNEL_NULL")
    @ApiModelProperty("消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

    @NotBlank(message = "P_THIRDPARTY_TITLE_NULL")
    @ApiModelProperty("消息标题")
    private String title;

    @NotBlank(message = "P_THIRDPARTY_MINI_LINK_NULL")
    @ApiModelProperty("小程序跳转链接")
    private String miniLink;

    @NotBlank(message = "P_THIRDPARTY_PC_LINK")
    @ApiModelProperty("PC端跳转链接")
    private String pcLink;

    @NotNull(message = "P_THIRDPARTY_PUSH_SWITCH")
    @ApiModelProperty("推送开关 0关 1开；如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）")
    private Short pushSwitch;

    @NotNull(message = "P_THIRDPARTY_DISTRIBUTOR_SCOPE")
    @ApiModelProperty("分销商推送范围1全部 2分销商等级 3.指定销售部门 4.指定业务员 5.指定分销商分组 6.指定分销商")
    private Short distributorScope;

    @NotBlank(message = "P_THIRDPARTY_CONTENT")
    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty(value = "可视，分销商价格等级id列表(分销商可视范围为2时必填)", required = false)
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "可视，销售部门id列表(分销商可视范围为3时必填)", required = false)
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "可视，业务员id列表(分销商可视范围为4时必填)", required = false)
    private List<Integer> adminIds;
    @ApiModelProperty(value = "可视，分销商id列表,可视范围为6时需传值", required = false)
    private List<Integer> distributorIds;
    @ApiModelProperty(value = "分销商分组id列表，可视范围为5时需传值", required = false)
    private List<Integer> distributorGroupIds;

}
