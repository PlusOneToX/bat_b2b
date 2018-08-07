package com.bat.system.api.globalsetting.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 11:22
 */
@Data
@ApiModel(value = "AgreementSettingDTO")
public class AgreementSettingDTO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "协议名称")
    private String name;

    @ApiModelProperty(value = "协议类型 1品牌协议 2用户协议")
    private Short type;

    @ApiModelProperty(value = "协议状态 0禁用 1启用")
    private Short status;

    @ApiModelProperty(value = "所属品牌")
    private String brandId;

    @ApiModelProperty(value = "协议发布区域 0国内 1国外")
    private Short agreementArea;

    @ApiModelProperty(value = "协议内容")
    private String content;
}
