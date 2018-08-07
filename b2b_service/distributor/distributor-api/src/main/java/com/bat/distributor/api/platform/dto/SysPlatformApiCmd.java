package com.bat.distributor.api.platform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商系统平台api信息")
public class SysPlatformApiCmd {
    @NotNull(message = "P_DISTRIBUTOR_PLATFORM_API_TYPE_NULL")
    @ApiModelProperty(value = "事务类型: 1为物流信息推送，2为订单信息推送，3 定制信息推送，4 获取定制价格 若后期有扩展，会进行追加", required = true, example = "1")
    private Short apiType;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_METHOD_NULL")
    @ApiModelProperty(value = "请求方式: GET,PUT,POST,DELETE", required = true, example = "1")
    private String method;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_URI_NULL")
    @ApiModelProperty(value = "接口uri", required = true, example = "1")
    private String uri;
    @NotNull(message = "P_DISTRIBUTOR_DEVELOP_SOURCE_NULL")
    @ApiModelProperty(
        value = "",
        required = true, example = "bat")
    private Short developSource;
}