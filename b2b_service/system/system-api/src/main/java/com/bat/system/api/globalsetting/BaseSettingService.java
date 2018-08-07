package com.bat.system.api.globalsetting;

import com.bat.system.api.globalsetting.dto.BaseSettingKeyUpdateCmd;
import com.bat.system.api.globalsetting.dto.BaseSettingLoginSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.api.globalsetting.dto.data.BaseSettingLoginSettingDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 10:33
 */
public interface BaseSettingService {
    /**
     * 通过key获取基本配置
     * 
     * @param key
     * @return
     */
    BaseSettingDTO getBaseSettingByKey(String key);

    /**
     * 获取基本信息 登录设置
     * 
     * @return
     */
    BaseSettingLoginSettingDTO getBaseSettingLoginSetting();

    /**
     * 通过key更新基本设置
     * 
     * @param cmd
     * @return
     */
    boolean updateBaseSettingByKey(BaseSettingKeyUpdateCmd cmd);

    /**
     * 更新基本设置登录设置
     * 
     * @param cmd
     * @return
     */
    boolean updateBaseSettingLoginSetting(BaseSettingLoginSettingUpdateCmd cmd);
}
