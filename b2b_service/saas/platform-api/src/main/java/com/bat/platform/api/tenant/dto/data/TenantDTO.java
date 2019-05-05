package com.bat.platform.api.tenant.dto.data;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "平台租户信息")
public class TenantDTO {
    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人")
    private Short companyType;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "联系人姓名")
    private String realName;

    @ApiModelProperty(value = "性别 0,未设置, 1,男 2,女")
    private Short sex;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态, 1.启用 0.禁用")
    private Short openFlag;

    @ApiModelProperty(value = "备注(可以填禁用原因)")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}