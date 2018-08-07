package com.bat.system.service.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.BaseSettingService;
import com.bat.system.api.globalsetting.dto.BaseSettingKeyUpdateCmd;
import com.bat.system.api.globalsetting.dto.BaseSettingLoginSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.api.globalsetting.dto.data.BaseSettingLoginSettingDTO;
import com.bat.system.service.globalsetting.executor.BaseSettingCmdExc;
import com.bat.system.service.globalsetting.executor.BaseSettingQryExc;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 10:36
 */
@Service
@Slf4j
public class BaseSettingServiceImpl implements BaseSettingService {

    @Resource
    private BaseSettingQryExc baseSettingQryExc;

    @Resource
    private BaseSettingCmdExc baseSettingCmdExc;

    @Override
    public BaseSettingDTO getBaseSettingByKey(String key) {
        return baseSettingQryExc.getBaseSettingByKey(key);
    }

    @Override
    public BaseSettingLoginSettingDTO getBaseSettingLoginSetting() {
        return baseSettingQryExc.getBaseSettingLoginSetting();
    }

    @Override
    public boolean updateBaseSettingByKey(BaseSettingKeyUpdateCmd cmd) {
        return baseSettingCmdExc.updateBaseSettingByKey(cmd);
    }

    @Override
    public boolean updateBaseSettingLoginSetting(BaseSettingLoginSettingUpdateCmd cmd) {
        return baseSettingCmdExc.updateBaseSettingLoginSetting(cmd);
    }
}
