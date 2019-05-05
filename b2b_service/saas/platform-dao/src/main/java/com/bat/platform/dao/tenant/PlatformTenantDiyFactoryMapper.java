package com.bat.platform.dao.tenant;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantDiyFactoryDO;

@Mapper
public interface PlatformTenantDiyFactoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantDiyFactoryDO record);

    int insertSelective(PlatformTenantDiyFactoryDO record);

    PlatformTenantDiyFactoryDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantDiyFactoryDO record);

    int updateByPrimaryKeyWithBLOBs(PlatformTenantDiyFactoryDO record);

    int updateByPrimaryKey(PlatformTenantDiyFactoryDO record);

    List<PlatformTenantDiyFactoryDO> listByTenantId(@Param("tenantId") Integer tenantId);

    PlatformTenantDiyFactoryDO selectByTenantIdAndFactoryNo(@Param("tenantId") Integer tenantId,
        @Param("factoryNo") String factoryNo);

    List<String> factoryNoList();
}