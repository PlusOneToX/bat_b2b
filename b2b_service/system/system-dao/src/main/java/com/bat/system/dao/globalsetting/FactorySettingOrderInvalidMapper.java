package com.bat.system.dao.globalsetting;

import java.util.List;

import com.bat.system.dao.globalsetting.dataobject.FactorySettingOrderInvalidDO;

public interface FactorySettingOrderInvalidMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FactorySettingOrderInvalidDO record);

    int insertSelective(FactorySettingOrderInvalidDO record);

    FactorySettingOrderInvalidDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FactorySettingOrderInvalidDO record);

    int updateByPrimaryKey(FactorySettingOrderInvalidDO record);

    List<FactorySettingOrderInvalidDO> listAll();

    FactorySettingOrderInvalidDO getByOrderSource(int orderSource);

    void deleteAll();
}