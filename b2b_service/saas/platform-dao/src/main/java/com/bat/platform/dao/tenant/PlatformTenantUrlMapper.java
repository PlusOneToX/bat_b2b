package com.bat.platform.dao.tenant;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantUrlDO;

@Mapper
public interface PlatformTenantUrlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantUrlDO record);

    int insertSelective(PlatformTenantUrlDO record);

    PlatformTenantUrlDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantUrlDO record);

    int updateByPrimaryKey(PlatformTenantUrlDO record);

    List<PlatformTenantUrlDO> listByTenantId(@Param("tenantId") Integer tenantId);

    List<PlatformTenantUrlDO> listByhost(@Param("host") String host);

    List<PlatformTenantUrlDO> all();

    List<PlatformTenantUrlDO> selectByTenantIdAndUrlType(@Param("tenantId") Integer tenantId,
        @Param("urlType") Short urlType);

    List<PlatformTenantUrlDO> selectByTenantNoAndUrlType(@Param("tenantNo") String tenantNo,
        @Param("urlType") Short urlType);

    List<PlatformTenantUrlDO> listByHostAndUrlType(@Param("host") String host, @Param("urlType") Short urlType);

}