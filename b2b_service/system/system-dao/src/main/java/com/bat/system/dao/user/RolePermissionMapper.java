package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.RolePermissionDO;
import org.apache.ibatis.annotations.Param;

public interface RolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermissionDO record);

    int insertSelective(RolePermissionDO record);

    RolePermissionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePermissionDO record);

    int updateByPrimaryKey(RolePermissionDO record);

    void deleteByRoleId(Integer id);

    List<RolePermissionDO> listByRoleId(Integer id);

    List<RolePermissionDO> listUnionByRoleId(Integer id);

    List<RolePermissionDO> listByRoleIds(@Param("roleIds") List<Integer> roleIds);
}