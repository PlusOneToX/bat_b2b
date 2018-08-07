package com.bat.system.api.globalsetting.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 19:34
 */
@Data
public class FactorySettingDelayPushesUpdateCmd {

    @ApiModelProperty(value = "id", required = true, example = "0")
    private Integer id;

    @ApiModelProperty(value = "工厂", required = true, example = "0")
    private String factory;

    @ApiModelProperty(value = "推送时间", required = true, example = "0")
    private Date pushTime;

    @ApiModelProperty(value = "相关分销商", required = true, example = "0")
    private String useRange;

    @ApiModelProperty(value = "0全部可用 1指定可用 2指定不可用", required = true, example = "0")
    private Integer type;
}
