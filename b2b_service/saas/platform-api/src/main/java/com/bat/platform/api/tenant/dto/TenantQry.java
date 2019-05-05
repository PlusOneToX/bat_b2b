package com.bat.platform.api.tenant.dto;

import com.bat.platform.api.base.PageQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "租户查询")
public class TenantQry extends PageQry {

    @ApiModelProperty(value = "搜索类型：1 公司名", required = false, example = "123343")
    private Short contentType;

    @ApiModelProperty(value = "搜索内容，公司名", required = false, example = "123343")
    private String content;
}