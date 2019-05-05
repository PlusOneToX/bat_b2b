package com.bat.platform.dao.tenant;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;

@Mapper
public interface PlatformTenantDBMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantDBDO record);

    void insertList(List<PlatformTenantDBDO> records);

    int insertSelective(PlatformTenantDBDO record);

    PlatformTenantDBDO selectByPrimaryKey(Integer id);

    List<PlatformTenantDBDO> selectByTenantNo(@Param("tenantNo") String tenantNo);

    List<PlatformTenantDBDO> selectByDbName(@Param("dbName") String dbName);

    int updateByPrimaryKeySelective(PlatformTenantDBDO record);

    int updateByPrimaryKey(PlatformTenantDBDO record);

    void updateList(List<PlatformTenantDBDO> records);

    List<PlatformTenantDBDO> listTenantDBByModelType(@Param("modelType") Short modelType);

    PlatformTenantDBDO selectByTenantIdAndModelType(@Param("tenantId") Integer tenantId,
        @Param("modelType") Short modelType);

    List<PlatformTenantDBDO> listByTenantId(@Param("tenantId") Integer tenantId);
}