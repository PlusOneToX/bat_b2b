package com.bat.system.dao.organization;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.organization.dataobject.DepartmentDO;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepartmentDO record);

    int insertSelective(DepartmentDO record);

    DepartmentDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepartmentDO record);

    int updateByPrimaryKey(DepartmentDO record);

    DepartmentDO getByErpDepartmentId(String erpDepartmentId);

    List<DepartmentDO> listByParams(@Param("params") Map<String, Object> map);

    List<DepartmentDO> listByName(String departmentName);

    DepartmentDO getByErpDepartmentIdAndOrganizationId(@Param("erpDepartmentId") String erpDepartmentId,
        @Param("organizationId") Integer organizationId);

    List<DepartmentDO> listByNameAndSaleType(@Param("departmentName") String departmentName,
        @Param("saleType") Short saleType);

    List<DepartmentDO> listByOrganizationId(Integer organizationId);

    List<DepartmentDO> listByCondition(@Param("status") Short status);

    List<DepartmentDO> selectByPrimaryKeys(@Param("ids") List<Integer> ids);
}