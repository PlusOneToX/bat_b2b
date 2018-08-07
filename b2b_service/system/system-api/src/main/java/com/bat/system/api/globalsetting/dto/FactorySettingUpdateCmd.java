package com.bat.system.api.globalsetting.dto;

import java.util.List;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/14 11:39
 */
@Data
public class FactorySettingUpdateCmd {
    private FactorySettingOrderInvalidUpdateCmd invalidDefault;
    private List<FactorySettingDelayPushesUpdateCmd> delayPushes;
    private List<FactorySettingOrderInvalidUpdateCmd> orderInvalid;
}
