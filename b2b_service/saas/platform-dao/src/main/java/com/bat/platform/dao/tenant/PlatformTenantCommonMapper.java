package com.bat.platform.dao.tenant;

import java.util.List;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantCommonDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformTenantCommonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantCommonDO record);

    int insertSelective(PlatformTenantCommonDO record);

    PlatformTenantCommonDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantCommonDO record);

    int updateByPrimaryKey(PlatformTenantCommonDO record);

    PlatformTenantCommonDO selectByTenantId(@Param("tenantId") Integer tenantId);

    PlatformTenantCommonDO selectByTenantNo(@Param("tenantNo") String tenantNo);

    List<PlatformTenantCommonDO> selectByWxProgramAppId(@Param("wxProgramAppId") String wxProgramAppId);

}