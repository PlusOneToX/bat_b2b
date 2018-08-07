package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.RoleDO;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);

    List<RoleDO> listAll();

    List<RoleDO> listRoleByName(String name);

    List<RoleDO> selectByPrimaryKeys(@Param("ids") List<Integer> ids);
}