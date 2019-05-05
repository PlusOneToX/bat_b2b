package com.bat.platform.dao.tenant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformTenantServiceDBMapper {

    void createTenantDB(@Param("tenantDB") String tenantDB);

    void createTenantDBTable(@Param("createTableSql") String createTableSql);

    void deteleTenantDB(@Param("tenantDB") String tenantDB);

}