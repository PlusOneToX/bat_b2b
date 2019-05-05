package com.bat.dubboapi.thirdparty.oss.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 21:02
 */
@Data
public class AssumeRoleRpcDTO implements Serializable {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
    private String bucketName;
    private String pathName;
    private String region;
    private String endpoint;
    private String hostname;
}
