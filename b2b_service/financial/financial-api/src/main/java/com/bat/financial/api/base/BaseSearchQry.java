package com.bat.financial.api.base;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/8 17:00
 */
@Data
public class BaseSearchQry {
    @NotNull(message = "P_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    protected Integer size;

    @NotNull(message = "P_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    protected Integer page;

    @ApiModelProperty(value = "查询类型", example = "1")
    protected Short contentType;

    @ApiModelProperty(value = "查询内容", example = "1")
    protected String content;
    /**
     * contentType attributeName
     */
    protected Map<Short, String> attributeMapper = new HashMap<>(16);

}
