package com.bat.system.api.oss.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/11 21:02
 */
@Data
@ApiModel(value = "AssumeRoleDTO")
public class AssumeRoleDTO {
    @ApiModelProperty(value = "accessKeyId")
    private String accessKeyId;
    @ApiModelProperty(value = "accessKeySecret")
    private String accessKeySecret;
    @ApiModelProperty(value = "securityToken")
    private String securityToken;
    @ApiModelProperty(value = "expiration")
    private String expiration;
    @ApiModelProperty(value = "bucketName")
    private String bucketName;
    @ApiModelProperty(value = "pathName")
    private String pathName;
    @ApiModelProperty(value = "region")
    private String region;
    @ApiModelProperty(value = "endpoint")
    private String endpoint;
    @ApiModelProperty(value = "hostname")
    private String hostname;
}
