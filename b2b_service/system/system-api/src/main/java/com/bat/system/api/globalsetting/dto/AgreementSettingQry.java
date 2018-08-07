package com.bat.system.api.globalsetting.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 13:47
 */
@Data
public class AgreementSettingQry extends BaseSearchQry {

    // @NotNull(message = "P_AGREEMENT_SETTING_TYPE_NULL")
    @ApiModelProperty(value = "协议类型 1品牌协议 2用户协议", required = true, example = "1")
    private Short type;

    // @NotNull(message = "P_AGREEMENT_SETTING_AGREEMENT_AREA_NULL")
    @ApiModelProperty(value = "协议发布区域 0国内 1国外", required = true, example = "0")
    private Short agreementArea;
}
