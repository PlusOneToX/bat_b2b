package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.UserRoleDO;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleDO record);

    int insertSelective(UserRoleDO record);

    UserRoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRoleDO record);

    int updateByPrimaryKey(UserRoleDO record);

    void deleteByUserId(Integer id);

    List<UserRoleDO> listByUserId(Integer id);

}