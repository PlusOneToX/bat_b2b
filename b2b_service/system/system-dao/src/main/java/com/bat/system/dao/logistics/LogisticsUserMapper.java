package com.bat.system.dao.logistics;

import java.util.List;

import com.bat.system.dao.logistics.dataobject.LogisticsUserDO;

public interface LogisticsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsUserDO record);

    int insertSelective(LogisticsUserDO record);

    LogisticsUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsUserDO record);

    int updateByPrimaryKey(LogisticsUserDO record);

    List<LogisticsUserDO> listByLogisticsId(Integer id);

    void deleteByLogisticsId(Integer id);
}