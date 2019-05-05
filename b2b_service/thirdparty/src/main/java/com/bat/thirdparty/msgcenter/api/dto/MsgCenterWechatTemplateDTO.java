package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class MsgCenterWechatTemplateDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

    @ApiModelProperty("通知类型:1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知")
    private Short type;

    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}