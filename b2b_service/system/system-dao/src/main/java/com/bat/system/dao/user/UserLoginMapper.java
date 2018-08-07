package com.bat.system.dao.user;

import com.bat.system.dao.user.dataobject.UserLoginDO;

public interface UserLoginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoginDO record);

    int insertSelective(UserLoginDO record);

    UserLoginDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLoginDO record);

    int updateByPrimaryKey(UserLoginDO record);
}