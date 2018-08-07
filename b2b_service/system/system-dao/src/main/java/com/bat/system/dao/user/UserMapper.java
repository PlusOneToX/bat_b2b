package com.bat.system.dao.user;

import com.bat.system.dao.user.dataobject.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    UserDO getByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    List<UserDO> listAll(Map map);

    UserDO getByErpUserNo(String erpUserNo);

    UserDO getByName(String userName);

    UserDO getByIdAndErpUserNo(Integer id, String erpUserNo);

    UserDO getByNotEqIdAndErpUserNo(Integer id, String erpUserNo);

    List<UserDO> listByContent(@Param("status") Short status, @Param("saleFlag") Short saleFlag,
                               @Param("contentType") Short contentType, @Param("content") String content);

    List<UserDO> listUserByDepartmentIds(String ids);

    List<UserDO> selectByParams(@Param("params") Map<String, Object> map);

    List<UserDO> listByCondition(@Param("status") Short status);
}