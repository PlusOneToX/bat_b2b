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
@ApiModel(value = "BaseSettingLoginSettingDTO")
public class BaseSettingLoginSettingDTO {
    @ApiModelProperty(value = "是否开启登录 0否 1是")
    private String loginOpenFlag;
    @ApiModelProperty(value = "登录提示 关闭登录时必填")
    private String loginTips;
    @ApiModelProperty(value = "登录英文提示 关闭登录时必填")
    private String loginTipsEn;
}