package com.bat.platform.dao.tenant;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantOssDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformTenantOssMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantOssDO record);

    int insertSelective(PlatformTenantOssDO record);

    PlatformTenantOssDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantOssDO record);

    int updateByPrimaryKey(PlatformTenantOssDO record);

    PlatformTenantOssDO findByTenantIdAndOssType(@Param("tenantId")Integer tenantId, @Param("ossType")Short ossType);

    List<PlatformTenantOssDO> listByTenantId(@Param("tenantId")Integer tenantId);
}