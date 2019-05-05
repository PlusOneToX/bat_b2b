package com.bat.platform.api.tenant.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "短信配置查询")
public class TenantSmsQry {

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    private Integer tenantId;

    @NotNull(message = "P_PLATFORM_TYPE_NULL")
    private Short smsType;

}
