package com.bat.thirdparty.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "BasePageParamQry",description = "分页基本参数、分页大小和当前页")
public class BasePageParamQry {


    @NotNull(message = "COMMON_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量",required = true,example = "10")
    @Min(value = 1L,message = "COMMON_NUMBER_ILLEGAL")
    private Integer size=10;

    @NotNull(message = "COMMON_PAGE_PAGE_NULL")
    @ApiModelProperty(value = "页码",required = true,example = "1")
    @Min(value = 1L,message = "COMMON_NUMBER_ILLEGAL")
    private Integer page=1;





}
