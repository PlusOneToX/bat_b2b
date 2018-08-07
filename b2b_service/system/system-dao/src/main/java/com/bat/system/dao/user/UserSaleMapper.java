package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.UserSaleDO;

public interface UserSaleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSaleDO record);

    int insertSelective(UserSaleDO record);

    UserSaleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSaleDO record);

    int updateByPrimaryKey(UserSaleDO record);

    void deleteByUserId(Integer id);

    List<UserSaleDO> listByUserId(Integer id);
}