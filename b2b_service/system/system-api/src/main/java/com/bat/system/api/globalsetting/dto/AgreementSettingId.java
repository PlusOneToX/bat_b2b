package com.bat.system.api.globalsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 11:22
 */
@Data
@ApiModel(value = "AgreementSettingId", description = "协议设置id")
public class AgreementSettingId {
    // @NotNull(message = "P_AGREEMENT_SETTING_ID_NULL")
    @ApiModelProperty(value = "协议id", required = true, example = "0")
    private Integer id;
}
