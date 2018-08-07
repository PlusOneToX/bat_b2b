package com.bat.system.dao.globalsetting;

import com.bat.system.dao.globalsetting.dataobject.BaseSettingDO;

public interface BaseSettingMapper {
    int deleteByPrimaryKey(String key);

    int insert(BaseSettingDO record);

    int insertSelective(BaseSettingDO record);

    BaseSettingDO selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(BaseSettingDO record);

    int updateByPrimaryKey(BaseSettingDO record);
}