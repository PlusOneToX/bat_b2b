package com.bat.platform.dao.tenant;

import org.apache.ibatis.annotations.Mapper;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformTenantSmsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantSmsDO record);

    int insertSelective(PlatformTenantSmsDO record);

    PlatformTenantSmsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantSmsDO record);

    int updateByPrimaryKey(PlatformTenantSmsDO record);

    PlatformTenantSmsDO selectByTenantIdAndSmsType(@Param("tenantId")Integer tenantId, @Param("smsType")Short smsType);

    List<PlatformTenantSmsDO> listByTenantId(@Param("tenantId")Integer tenantId);
}