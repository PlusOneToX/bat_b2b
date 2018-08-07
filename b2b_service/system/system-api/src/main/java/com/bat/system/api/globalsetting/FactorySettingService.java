package com.bat.system.api.globalsetting;

import java.util.List;

import com.bat.system.api.globalsetting.dto.FactorySettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.FactorySettingDelayPushesDTO;
import com.bat.system.api.globalsetting.dto.data.FactorySettingOrderInvalidDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 15:55
 */
public interface FactorySettingService {
    /**
     * 获取工厂延迟推送设置
     * 
     * @return
     */
    List<FactorySettingDelayPushesDTO> listFactorySettingDelayPushes();

    /**
     * 通过定时器更新工厂延迟推送设置
     * 
     * @return
     */
    boolean updateDelayPushesPushTimeByTask();

    /**
     * 获取工厂设置订单失效设置列表
     * 
     * @return
     */
    List<FactorySettingOrderInvalidDTO> listFactorySettingOrderInvalid();

    /**
     * 通过订单来源获取工厂设置订单失效列表
     * 
     * @param orderSource
     * @return
     */
    FactorySettingOrderInvalidDTO getFactorySettingOrderInvalidByOrderSource(int orderSource);

    /**
     * 更新工厂设置
     * 
     * @param cmd
     */
    void updateFactorySetting(FactorySettingUpdateCmd cmd);
}
