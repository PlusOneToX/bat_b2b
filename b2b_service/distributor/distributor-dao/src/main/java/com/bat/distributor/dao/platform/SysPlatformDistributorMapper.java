package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.DistributorPlatformApiRpcDO;
import com.bat.distributor.dao.platform.dataobject.SysPlatformDistributorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPlatformDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPlatformDistributorDO record);

    SysPlatformDistributorDO selectByPrimaryKey(Integer id);

    List<SysPlatformDistributorDO> selectAll();

    int updateByPrimaryKey(SysPlatformDistributorDO record);

    void deleteBySysPlatformId(Integer sysPlatformId);

    void insertList(List<SysPlatformDistributorDO> records);

    List<SysPlatformDistributorDO> listBySysPlatformId(Integer sysPlatformId);

    DistributorPlatformApiRpcDO getByDistributorIdAndApiTypeAndPlatform(@Param("distributorId") Integer distributorId,
                                                                        @Param("apiType") Short apiType, @Param("platform") String platform);
}