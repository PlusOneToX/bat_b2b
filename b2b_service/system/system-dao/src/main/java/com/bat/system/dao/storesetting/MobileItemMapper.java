package com.bat.system.dao.storesetting;

import java.util.List;

import com.bat.system.dao.storesetting.dataobject.MobileItemDO;

public interface MobileItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MobileItemDO record);

    int insertSelective(MobileItemDO record);

    MobileItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MobileItemDO record);

    int updateByPrimaryKey(MobileItemDO record);

    int deleteByMobileId(Integer id);

    List<MobileItemDO> listByModuleId(Integer id);
}