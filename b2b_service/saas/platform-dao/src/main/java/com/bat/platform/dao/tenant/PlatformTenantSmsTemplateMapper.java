package com.bat.platform.dao.tenant;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformTenantSmsTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTenantSmsTemplateDO record);

    int insertSelective(PlatformTenantSmsTemplateDO record);

    PlatformTenantSmsTemplateDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTenantSmsTemplateDO record);

    int updateByPrimaryKey(PlatformTenantSmsTemplateDO record);

    List<PlatformTenantSmsTemplateDO> listByPlatformTenantSmsId(@Param("platformTenantSmsId")Integer platformTenantSmsId);

    PlatformTenantSmsTemplateDO selectByPlatformTenantSmsIdAndTemplateType(@Param("platformTenantSmsId")Integer platformTenantSmsId, @Param("templateType")Short templateType);

    List<PlatformTenantSmsTemplateDO> listByPlatformTenantSmsIds(@Param("platformTenantSmsIds")List<Integer> platformTenantSmsIds);
}