package com.bat.platform.api.tenant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "文件存储配置查询")
public class TenantOssQry {

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    private Integer tenantId;

    @NotNull(message = "P_PLATFORM_TYPE_NULL")
    private Short ossType;
}