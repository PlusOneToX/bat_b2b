package com.bat.system.service.globalsetting.executor;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.api.globalsetting.dto.data.BaseSettingLoginSettingDTO;
import com.bat.system.dao.globalsetting.BaseSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.BaseSettingDO;
import org.springframework.stereotype.Component;

import com.bat.system.service.globalsetting.convertor.BaseSettingConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class BaseSettingQryExc {
    @Resource
    private BaseSettingMapper baseSettingMapper;

    public BaseSettingDTO getBaseSettingByKey(String key) {
        BaseSettingDO baseSettingDO = baseSettingMapper.selectByPrimaryKey(key);
        return baseSettingDO == null ? null : BaseSettingConvertor.toBaseSettingDTO(baseSettingDO);
    }

    public BaseSettingLoginSettingDTO getBaseSettingLoginSetting() {
        BaseSettingLoginSettingDTO dto = new BaseSettingLoginSettingDTO();
        BaseSettingDO login_open_flag = baseSettingMapper.selectByPrimaryKey("login_open_flag");
        if(login_open_flag != null){
            dto.setLoginOpenFlag(baseSettingMapper.selectByPrimaryKey("login_open_flag").getValue());
            if (baseSettingMapper.selectByPrimaryKey("login_tips") != null) {
                dto.setLoginTips(baseSettingMapper.selectByPrimaryKey("login_tips").getValue());
            }
            if (baseSettingMapper.selectByPrimaryKey("login_tips_en") != null) {
                dto.setLoginTipsEn(baseSettingMapper.selectByPrimaryKey("login_tips_en").getValue());
            }
        }
        return dto;
    }
}
