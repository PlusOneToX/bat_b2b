package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "联系人信息")
public class UserLanguageCmd extends BaseId {

    @NotBlank(message = "P_DISTRIBUTOR_USER_LANGUAGE_NULL")
    @ApiModelProperty(value = "平台显示语言", required = true, example = "en")
    private String language;
    @NotBlank(message = "P_DISTRIBUTOR_USER_CURRENCY_NULL")
    @ApiModelProperty(value = "平台显示币种", required = true, example = "USD")
    private String currencyType;
}
