package com.bat.distributor.api.user.dto.user.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "下级分销商详情")
public class UserNextDTO {
    private Integer id;
    @ApiModelProperty(value = "用户名(登录名)", example = "bat")
    private String name;
    @ApiModelProperty(value = "公司名", example = "bat")
    private String companyName;
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败", required = false, example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "申请时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    @ApiModelProperty(value = "申请审核时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "冻结时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;

    @ApiModelProperty(value = "商户联系人", example = "bat")
    private String userName;
    @ApiModelProperty(value = "联系人手机号", example = "5277587")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", example = "5277587")
    private String email;

    @ApiModelProperty(value = "分销商价格等级id", example = "bat")
    private Integer scalePriceId;
    @ApiModelProperty(value = "分销商价格等级名称", example = "一级价")
    private String scalePriceName;

}
