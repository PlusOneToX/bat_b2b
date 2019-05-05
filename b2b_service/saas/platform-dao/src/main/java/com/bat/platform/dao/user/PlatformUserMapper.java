package com.bat.platform.dao.user;

import java.util.List;

import com.bat.platform.dao.user.dataobject.PlatformUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformUserDO record);

    int insertSelective(PlatformUserDO record);

    PlatformUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformUserDO record);

    int updateByPrimaryKey(PlatformUserDO record);

    PlatformUserDO selectByUserName(@Param("userName") String userName);

    List<PlatformUserDO> listCOByCondition(@Param("openFlag") Short openFlag, @Param("contentType") Short contentType,
        @Param("content") String content);
}