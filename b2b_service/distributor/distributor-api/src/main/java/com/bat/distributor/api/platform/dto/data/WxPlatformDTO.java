package com.bat.distributor.api.platform.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商微信平台信息")
public class WxPlatformDTO {
    @ApiModelProperty(value = "分销商微信平台id", example = "1111")
    private Integer id;
    @ApiModelProperty(value = "分销商平台编码", example = "bat")
    private String platform;
    @ApiModelProperty(value = "平台类型：1 公众号 2 小程序", example = "1")
    private Short type;
    @ApiModelProperty(value = "分销商微信平台名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "微信平台appid", example = "bat")
    private String appId;
    @ApiModelProperty(value = "微信平台密钥", example = "bat")
    private String appSecret;
    @ApiModelProperty(value = "创建时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "分销商列表")
    private List<PlatformDistributorDTO> distributors;

}