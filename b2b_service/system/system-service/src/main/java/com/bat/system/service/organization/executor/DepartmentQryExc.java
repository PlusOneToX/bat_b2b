package com.bat.system.service.organization.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.organization.dto.DepartmentQry;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
import com.bat.system.dao.organization.DepartmentMapper;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import com.bat.system.service.organization.convertor.DepartmentConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class DepartmentQryExc {
    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    public PageInfo<DepartmentDTO> listDepartment(DepartmentQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DepartmentDO> departmentDOList =
            departmentMapper.listByNameAndSaleType(qry.getDepartmentName(), qry.getSaleType());
        PageInfo pageInfo = new PageInfo(departmentDOList);
        List<DepartmentDTO> toDepartmentDTOList = DepartmentConvertor.toDepartmentDTOList(pageInfo.getList());
        List<DepartmentDTO> collect = toDepartmentDTOList.stream().map(departmentDTO -> {
            OrganizationDO organizationDO = organizationMapper.selectByPrimaryKey(departmentDTO.getOrganizationId());
            if (organizationDO != null) {
                departmentDTO.setOrganizationName(organizationDO.getName());
                return departmentDTO;
            } else {
                departmentDTO.setOrganizationName("组织已被删除");
            }
            return departmentDTO;
        }).collect(Collectors.toList());
        pageInfo.setList(collect);
        return pageInfo;
    }

    public DepartmentDTO getDepartmentById(Integer id) {
        DepartmentDO departmentDO = departmentMapper.selectByPrimaryKey(id);
        return departmentDO == null ? null : DepartmentConvertor.toDepartmentDTO(departmentDO);
    }

    public PageInfo<DepartmentDTO> listDepartmentByOrganization(DepartmentQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DepartmentDO> departmentDOList = departmentMapper.listByOrganizationId(qry.getOrganizationId());
        PageInfo pageInfo = new PageInfo(departmentDOList);
        List<DepartmentDTO> toDepartmentDTOList = DepartmentConvertor.toDepartmentDTOList(pageInfo.getList());
        List<DepartmentDTO> collect = toDepartmentDTOList.stream().map(departmentDTO -> {
            OrganizationDO organizationDO = organizationMapper.selectByPrimaryKey(departmentDTO.getOrganizationId());
            departmentDTO.setOrganizationName(organizationDO.getName());
            return departmentDTO;
        }).collect(Collectors.toList());
        pageInfo.setList(collect);
        return pageInfo;
    }

    public List<DepartmentDO> listByCondition(Short status) {
        return departmentMapper.listByCondition(status);
    }

    public List<DepartmentDTO> getDepartmentByIds(List<Integer> ids) {
        List<DepartmentDO> departmentDOS = departmentMapper.selectByPrimaryKeys(ids);
        return DepartmentConvertor.toDepartmentDTOList(departmentDOS);
    }
}
