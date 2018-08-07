package com.bat.system.api.globalsetting.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 10:41
 */
@Data
@ApiModel(value = "BaseSettingKeyUpdateCmd", description = "基本设置查询")
public class BaseSettingKeyUpdateCmd {

    @NotBlank(message = "P_BASE_SETTING_KEY_NULL")
    @ApiModelProperty(value = "key", required = true, example = "1")
    private String key;

    // @NotBlank(message = "P_BASE_SETTING_VALUE_NULL")
    @ApiModelProperty(value = "value", required = true, example = "1")
    private String value;
}
