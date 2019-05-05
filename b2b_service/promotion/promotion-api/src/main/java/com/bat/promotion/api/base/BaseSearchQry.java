package com.bat.promotion.api.base;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/8 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseSearchQry extends BasePage {

    @ApiModelProperty(value = "查询类型", example = "1")
    protected Short contentType;

    @ApiModelProperty(value = "查询内容", example = "1")
    protected String content;
    /**
     * contentType attributeName
     */
    protected Map<Short, String> attributeMapper = new HashMap<>(16);

}
