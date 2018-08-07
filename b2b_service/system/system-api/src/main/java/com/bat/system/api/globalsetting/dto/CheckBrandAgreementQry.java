package com.bat.system.api.globalsetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 13:47
 */
@Data
public class CheckBrandAgreementQry {

    @NotNull(message = "P_AGREEMENT_SETTING_AGREEMENT_AREA_NULL")
    @ApiModelProperty(value = "协议发布区域", required = true, example = "0")
    private Short agreementArea;
}
