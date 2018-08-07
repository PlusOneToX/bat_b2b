package com.bat.system.dao.globalsetting;

import java.util.List;

import com.bat.system.dao.globalsetting.dataobject.FactorySettingDelayPushDO;

public interface FactorySettingDelayPushMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FactorySettingDelayPushDO record);

    int insertSelective(FactorySettingDelayPushDO record);

    FactorySettingDelayPushDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FactorySettingDelayPushDO record);

    int updateByPrimaryKey(FactorySettingDelayPushDO record);

    List<FactorySettingDelayPushDO> listAll();

    void deleteAll();
}