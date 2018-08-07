package com.bat.system.api.globalsetting.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 22:16
 */
@Data
@ApiModel(value = "BaseSettingDTO")
public class BaseSettingDTO {
    @ApiModelProperty(value = "key")
    private String key;

    @ApiModelProperty(value = "value")
    private String value;
}