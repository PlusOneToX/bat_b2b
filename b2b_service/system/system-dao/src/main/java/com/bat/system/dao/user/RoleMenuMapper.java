package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.RoleMenuDO;
import org.apache.ibatis.annotations.Param;

public interface RoleMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleMenuDO record);

    int insertSelective(RoleMenuDO record);

    RoleMenuDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenuDO record);

    int updateByPrimaryKey(RoleMenuDO record);

    void deleteByRoleId(Integer id);

    List<RoleMenuDO> listByRoleId(Integer id);

    List<RoleMenuDO> listUnionByRoleId(Integer id);

    List<RoleMenuDO> listByRoleIds(@Param("roleIds") List<Integer> roleIds);
}