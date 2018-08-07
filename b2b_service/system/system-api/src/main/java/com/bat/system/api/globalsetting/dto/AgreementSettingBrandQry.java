package com.bat.system.api.globalsetting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/25 15:42
 */
@Data
public class AgreementSettingBrandQry {
    @ApiModelProperty(value = "协议发布区域 0国内 1国外", required = true, example = "0")
    private Short agreementArea;
    @ApiModelProperty(value = "品牌id", required = true, example = "0")
    private Integer brandId;
}
