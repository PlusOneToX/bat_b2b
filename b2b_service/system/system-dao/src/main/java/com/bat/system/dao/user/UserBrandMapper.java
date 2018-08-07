package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.UserBrandDO;

public interface UserBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBrandDO record);

    int insertSelective(UserBrandDO record);

    UserBrandDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBrandDO record);

    int updateByPrimaryKey(UserBrandDO record);

    void deleteByUserId(Integer id);

    List<UserBrandDO> listByUserId(Integer id);
}