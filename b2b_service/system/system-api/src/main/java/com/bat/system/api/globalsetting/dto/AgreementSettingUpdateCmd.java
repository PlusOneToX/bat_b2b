package com.bat.system.api.globalsetting.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 11:22
 */
@Data
@ApiModel(value = "AgreementSettingUpdateCmd", description = "协议设置更新")
public class AgreementSettingUpdateCmd {
    @NotNull(message = "P_AGREEMENT_SETTING_ID_NULL")
    @ApiModelProperty(value = "协议id", required = true, example = "0")
    private Integer id;

    @NotBlank(message = "P_AGREEMENT_SETTING_NAME_NULL")
    @ApiModelProperty(value = "协议名称", required = true, example = "定制商品购销协议")
    private String name;

    @NotNull(message = "P_AGREEMENT_SETTING_TYPE_NULL")
    @ApiModelProperty(value = "协议类型", required = true, example = "1")
    private Short type;

    @NotNull(message = "P_AGREEMENT_SETTING_STATUS_NULL")
    @ApiModelProperty(value = "协议状态", required = true, example = "1")
    private Short status;

    @ApiModelProperty(value = "协议品牌", example = "0")
    private String brandId;

    @NotNull(message = "P_AGREEMENT_SETTING_AGREEMENT_AREA_NULL")
    @ApiModelProperty(value = "协议发布区域", required = true, example = "0")
    private Short agreementArea;

    @NotBlank(message = "P_AGREEMENT_SETTING_CONTENT_NULL")
    @ApiModelProperty(value = "协议内容", required = true, example = "")
    private String content;
}
