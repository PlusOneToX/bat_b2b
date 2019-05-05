package com.bat.platform.dao.tenant;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantErpDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformTenantErpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantErpDO record);

    int insertSelective(PlatformTenantErpDO record);

    PlatformTenantErpDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantErpDO record);

    int updateByPrimaryKey(PlatformTenantErpDO record);

    PlatformTenantErpDO selectByTenantId(@Param("tenantId")Integer tenantId);

    PlatformTenantErpDO selectByTenantIdAndErpType(@Param("tenantId")Integer tenantId,@Param("erpType") Short erpType);
}