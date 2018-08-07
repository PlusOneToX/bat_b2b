package com.bat.system.dao.organization;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.organization.dataobject.OrganizationDO;
import org.apache.ibatis.annotations.Param;

public interface OrganizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrganizationDO record);

    int insertSelective(OrganizationDO record);

    OrganizationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrganizationDO record);

    int updateByPrimaryKey(OrganizationDO record);

    OrganizationDO getByErpOrganizationId(String erpOrganizationId);

    List<OrganizationDO> listByParams(@Param("params") Map<String,Object> map);

    List<OrganizationDO> listByName(String name);

    OrganizationDO getByIdAndErpOrganizationId(Integer id, String erpOrganizationId);

    List<OrganizationDO> listByOrganizationIds(@Param("organizationIds") List<Integer> organizationIds);
}