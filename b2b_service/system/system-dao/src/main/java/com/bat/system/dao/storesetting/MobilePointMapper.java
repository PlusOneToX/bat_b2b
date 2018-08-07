package com.bat.system.dao.storesetting;


import com.bat.system.dao.storesetting.dataobject.MobilePointDO;

import java.util.List;

public interface MobilePointMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByMobileChildId(Integer mobileChildId);

    int insert(MobilePointDO record);

    int insertSelective(MobilePointDO record);

    MobilePointDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MobilePointDO record);

    int updateByPrimaryKey(MobilePointDO record);

    void insertList(List<MobilePointDO> mobilePointDOS);

    List<MobilePointDO> listByMobileChildId(Integer mobileChildId);
}