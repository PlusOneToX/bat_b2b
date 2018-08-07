package com.bat.system.dao.globalsetting;

import com.bat.system.dao.globalsetting.dataobject.ShopSettingDO;

public interface ShopSettingMapper {
    int deleteByPrimaryKey(String key);

    int insert(ShopSettingDO record);

    int insertSelective(ShopSettingDO record);

    ShopSettingDO selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(ShopSettingDO record);

    int updateByPrimaryKey(ShopSettingDO record);
}