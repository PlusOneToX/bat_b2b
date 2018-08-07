package com.bat.system.api.globalsetting.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 15:57
 */
@Data
@ApiModel(value = "FactorySettingDelayPushesDTO")
public class FactorySettingDelayPushesDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "工厂")
    private String factory;

    @ApiModelProperty(value = "推送时间")
    private Date pushTime;

    @ApiModelProperty(value = "相关分销商")
    private String useRange;

    @ApiModelProperty(value = "0全部可用 1指定可用 2指定不可用")
    private Integer type;
}
