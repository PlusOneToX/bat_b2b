package com.bat.platform.api.tenant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
@ApiModel(description = "短信配置添加或修改")
public class TenantSmsCmd {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "短信平台类型：1 阿里云")
    private Short smsType;

    @ApiModelProperty(value = "短信平台签名")
    private String sign;

    @ApiModelProperty(value = "短信平台key")
    private String accessKeyId;

    @ApiModelProperty(value = "短信平台secret")
    private String accessKeySecret;

    @ApiModelProperty(value = "验证码长度")
    private Integer verifyCodeLength;

    @ApiModelProperty(value = "验证码有效时间")
    private Integer codeVerifyTime;

    @ApiModelProperty(value = "前端验证码倒计时")
    private Integer verifyCodeCountDown;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "短信模板id")
    private List<TenantSmsTemplate> tenantSmsTemplates;

    @Data
    public static class TenantSmsTemplate {

        @ApiModelProperty(value = "id")
        private Integer id;

        @ApiModelProperty(value = "平台租户短信配置id")
        private Integer platformTenantSmsId;

        @ApiModelProperty(value = "短信模板类型：1 注册申请 2 B端验证码修改密码 3 C端修改手机号 4 C端客户验证码登录 5 C端验证码修改密码 6 分账业务员绑定 7 B端客户验证码登录")
        private Short templateType;

        @ApiModelProperty(value = "短信平台模板码")
        private String templateCode;

    }

}
