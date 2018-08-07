package com.bat.system.api.globalsetting.dto.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/14 11:47
 */
@Data
@ApiModel(value = "FactorySettingDTO")
public class FactorySettingDTO {
    @ApiModelProperty(value = "默认失效时间")
    private FactorySettingOrderInvalidDTO invalidDefault;
    @ApiModelProperty(value = "延迟推送")
    private List<FactorySettingDelayPushesDTO> delayPushes;
    @ApiModelProperty(value = "订单失效")
    private List<FactorySettingOrderInvalidDTO> orderInvalid;
}
