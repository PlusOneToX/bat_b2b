package com.bat.system.service.globalsetting.executor;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.globalsetting.dto.BaseSettingKeyUpdateCmd;
import com.bat.system.api.globalsetting.dto.BaseSettingLoginSettingUpdateCmd;
import com.bat.system.dao.globalsetting.BaseSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.BaseSettingDO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class BaseSettingCmdExc {

    @Resource
    private BaseSettingMapper baseSettingMapper;

    public boolean updateBaseSettingByKey(BaseSettingKeyUpdateCmd cmd) {
        BaseSettingDO aDo = new BaseSettingDO();
        aDo.setKey(cmd.getKey());
        aDo.setValue(cmd.getValue());
        baseSettingMapper.updateByPrimaryKeySelective(aDo);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateBaseSettingLoginSetting(BaseSettingLoginSettingUpdateCmd cmd) {
        if ("0".equals(cmd.getLoginOpenFlag()) && cmd.getLoginTips() == null) {
            throw SystemException.buildException(ErrorCode.B_BASE_SETTING_LOGIN_SETTING_LOGIN_TIPS_NULL);
        }
        if (cmd.getLoginOpenFlag() != null) {
            baseSettingMapper.updateByPrimaryKeySelective(new BaseSettingDO("login_open_flag", cmd.getLoginOpenFlag()));
        }
        if (cmd.getLoginTips() != null) {
            baseSettingMapper.updateByPrimaryKeySelective(new BaseSettingDO("login_tips", cmd.getLoginTips()));
        }
        if (cmd.getLoginTipsEn() != null) {
            baseSettingMapper.updateByPrimaryKeySelective(new BaseSettingDO("login_tips_en", cmd.getLoginTipsEn()));
        }
        return true;
    }
}
