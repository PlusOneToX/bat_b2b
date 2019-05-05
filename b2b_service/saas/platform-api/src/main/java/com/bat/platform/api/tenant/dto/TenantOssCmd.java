package com.bat.platform.api.tenant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "文件存储配置添加或修改")
public class TenantOssCmd {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotNull(message = "P_PLATFORM_TYPE_NULL")
    @ApiModelProperty(value = "文件存储平台类型：1 阿里云")
    private Short ossType;

    @NotBlank(message = "P_PLATFORM_ENDPOINT_NULL")
    @ApiModelProperty(value = "文件服务器oss endpoint")
    private String endpoint;

    @NotBlank(message = "P_PLATFORM_KEY_NULL")
    @ApiModelProperty(value = "文件服务器oss key")
    private String accessKeyId;

    @NotBlank(message = "P_PLATFORM_SECRET_NULL")
    @ApiModelProperty(value = "文件服务器oss secret")
    private String accessKeySecret;

    @NotBlank(message = "P_PLATFORM_BUCKET_NULL")
    @ApiModelProperty(value = "文件服务器oss bucket")
    private String bucket;

    @NotBlank(message = "P_PLATFORM_BASE_HTTP_NULL")
    @ApiModelProperty(value = "文件服务器oss baseHttp")
    private String baseHttp;

    @NotBlank(message = "P_PLATFORM_ROLE_ARN_NULL")
    @ApiModelProperty(value = "文件服务器oss roleArn")
    private String roleArn;

    @NotBlank(message = "P_PLATFORM_REGION_ID_NULL")
    @ApiModelProperty(value = "文件服务器oss regionId sts获取授权时使用")
    private String regionId;

    @NotBlank(message = "P_PLATFORM_REGION_NULL")
    @NotBlank(message = "")
    @ApiModelProperty(value = "文件服务器oss region api时使用")
    private String region;

    @NotBlank(message = "P_PLATFORM_POLICY_NULL")
    @ApiModelProperty(value = "文件服务器oss policy")
    private String policy;

    @ApiModelProperty(value = "分销商申请二维码图片存放路径")
    private String distributorOssFolder;
}