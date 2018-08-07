package com.bat.system.dao.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.user.dataobject.PermissionDO;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PermissionDO record);

    int insertSelective(PermissionDO record);

    PermissionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PermissionDO record);

    int updateByPrimaryKey(PermissionDO record);

    List<PermissionDO> listAll();

    PermissionDO getByPathUrlAndMethod(@Param("urlPath") String urlPath, @Param("method") String method);

    List<PermissionDO> listByModule(String module);

    List<PermissionDO> listByPrimaryKeys(@Param("ids") List<Integer> ids);

    List<Map<String, String>> listByPermissionUserMapping();
}