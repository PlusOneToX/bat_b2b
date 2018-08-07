package com.bat.distributor.api.platform.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商系统平台api信息")
public class SysPlatformApiDTO {
    @ApiModelProperty(value = "事务类型: 1为物流信息推送，2为订单信息推送，若后期有扩展，会进行追加", example = "1")
    private Short apiType;
    @ApiModelProperty(value = "请求方式: GET,PUT,POST,DELETE", example = "GET")
    private String method;
    @ApiModelProperty(value = "接口uri", example = "1")
    private String uri;
    @ApiModelProperty(
        value = "",
        example = "bat")
    private Short developSource;
}