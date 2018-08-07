package com.bat.system.dao.storesetting;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.storesetting.dataobject.MobileDO;

public interface MobileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MobileDO record);

    int insertSelective(MobileDO record);

    MobileDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MobileDO record);

    int updateByPrimaryKey(MobileDO record);

    List<MobileDO> listByModuleType(Short moduleType);

    List<MobileDO> listAll(@Param("status") Short status, @Param("moduleType") Short moduleType);
}