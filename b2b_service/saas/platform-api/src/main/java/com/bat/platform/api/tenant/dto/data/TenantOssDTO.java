package com.bat.platform.api.tenant.dto.data;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class TenantOssDTO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "文件存储平台类型：1 阿里云")
    private Short ossType;

    @ApiModelProperty(value = "文件服务器oss endpoint")
    private String endpoint;

    @ApiModelProperty(value = "文件服务器oss key")
    private String accessKeyId;

    @ApiModelProperty(value = "文件服务器oss secret")
    private String accessKeySecret;

    @ApiModelProperty(value = "文件服务器oss bucket")
    private String bucket;

    @ApiModelProperty(value = "文件服务器oss baseHttp")
    private String baseHttp;

    @ApiModelProperty(value = "文件服务器oss roleArn")
    private String roleArn;

    @ApiModelProperty(value = "文件服务器oss regionId sts获取授权时使用")
    private String regionId;

    @ApiModelProperty(value = "文件服务器oss region api时使用")
    private String region;

    @ApiModelProperty(value = "文件服务器oss policy")
    private String policy;

    @ApiModelProperty(value = "分销商申请二维码图片存放路径")
    private String distributorOssFolder;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
