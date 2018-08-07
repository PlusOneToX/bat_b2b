package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.SysPlatformApiDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPlatformApiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPlatformApiDO record);

    SysPlatformApiDO selectByPrimaryKey(Integer id);

    List<SysPlatformApiDO> selectAll();

    int updateByPrimaryKey(SysPlatformApiDO record);

    void deleteBySysPlatformId(Integer sysPlatformId);

    void insertList(List<SysPlatformApiDO> records);

    List<SysPlatformApiDO> listBySysPlatformId(Integer sysPlatformId);

    SysPlatformApiDO getSysPlatformApiBySysPlatformIdAndApiType(@Param("sysPlatformId") Integer sysPlafformId,@Param("apiType") Short apiType);
}