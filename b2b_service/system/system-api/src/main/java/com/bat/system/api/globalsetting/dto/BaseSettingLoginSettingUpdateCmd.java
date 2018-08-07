package com.bat.system.api.globalsetting.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 22:16
 */
@Data
public class BaseSettingLoginSettingUpdateCmd {

    @NotBlank(message = "P_BASE_SETTING_LOGIN_SETTING_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启登录", required = true, example = "1")
    private String loginOpenFlag;
    private String loginTips;
    private String loginTipsEn;
}