package com.bat.platform.dao.tenant;

import java.util.List;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformTenantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantDO record);

    int insertSelective(PlatformTenantDO record);

    PlatformTenantDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantDO record);

    int updateByPrimaryKey(PlatformTenantDO record);

    List<PlatformTenantDO> listCOByCondition(@Param("contentType") Short contentType, @Param("content") String content);

    PlatformTenantDO selectByTenantNo(@Param("tenantNo")String tenantNo);
}