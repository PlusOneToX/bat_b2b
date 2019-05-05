package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MsgCenterDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("小程序跳转链接")
    private String miniLink;

    @ApiModelProperty("PC端跳转链接")
    private String pcLink;

    @ApiModelProperty("推送开关 0关 1开；如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）")
    private Short pushSwitch;

    @ApiModelProperty("分销商推送范围1全部 2分销商等级 3.指定销售部门 4.指定业务员 5.指定分销商分组 6.指定分销商")
    private Short distributorScope;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty(value = "可视，分销商等级id列表")
    private List<Integer> scalePriceIds;

    @ApiModelProperty(value = "可视，销售部门id列表")
    private List<Integer> departmentIds;

    @ApiModelProperty(value = "可视，业务员id列表")
    private List<Integer> adminIds;

    @ApiModelProperty(value = "可视，分销商id列表")
    private List<Integer> distributorIds;

    @ApiModelProperty(value = "可视，分销商组id列表")
    private List<Integer> distributorGroupIds;

/*    @ApiModelProperty(value = "可视，分销商id列表")
    private List<GoodsDistributorScopeDTO> distributors;
    @ApiModelProperty(value = "可视，分销商分组id列表")
    private List<GoodsDistributorGroupDTO> distributorGroups;*/
}
