package com.bat.distributor.dao.role;

import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.role.dataobject.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDO record);

    RoleDO selectByPrimaryKey(Integer id);

    List<RoleDO> selectAll();

    int updateByPrimaryKey(RoleDO record);

    Integer getRoleContactsCount(@Param("roleId") Integer roleId);

    void openRole(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<RoleDO> listRole(Map map);

}