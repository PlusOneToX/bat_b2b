package com.bat.system.service.organization.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.organization.dto.DepartmentCreateCmd;
import com.bat.system.api.organization.dto.DepartmentUpdateCmd;
import com.bat.system.dao.organization.DepartmentMapper;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import com.bat.system.service.organization.convertor.DepartmentConvertor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
@Slf4j
public class DepartmentCmdExc {

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    public boolean createDepartment(DepartmentCreateCmd cmd) {
        DepartmentDO departmentDO =
            departmentMapper.getByErpDepartmentIdAndOrganizationId(cmd.getErpDepartmentId(), cmd.getOrganizationId());
        if (departmentDO != null) {
            throw SystemException.buildException(ErrorCode.B_ERP_DEPARTMENT_ID_EXISTS);
        }
        return departmentMapper.insert(DepartmentConvertor.toDepartmentDO(cmd)) == 1;
    }

    public boolean deleteDepartmentById(Integer id) {
        DepartmentDO departmentDO = departmentMapper.selectByPrimaryKey(id);
        if (departmentDO == null) {
            throw SystemException.buildException(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS);
        }
        return departmentMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateDepartment(DepartmentUpdateCmd cmd) {
        if (departmentMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS);
        }
        try {
            departmentMapper.updateByPrimaryKeySelective(DepartmentConvertor.toDepartmentDO(cmd));
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_org_id_erp_dep_id")) {
                throw SystemException.buildException(ErrorCode.B_ERP_DEPARTMENT_ID_EXISTS);
            }
        }
        return true;
    }

    public void syncDepartment(List<DepartmentErpRpcDTO> departmentErpRpcDTOS) {
        log.info("补充部门信息");
        for (DepartmentErpRpcDTO departmentErpRpcDTO : departmentErpRpcDTOS) {
            // erp 组织编码
            String orgId = departmentErpRpcDTO.getOrgId();
            OrganizationDO organizationDO = organizationMapper.getByErpOrganizationId(orgId);
            // erp 部门编码
            String erpDepartmentId = departmentErpRpcDTO.getErpDepartmentId();
            DepartmentDO departmentDO =
                departmentMapper.getByErpDepartmentIdAndOrganizationId(erpDepartmentId, organizationDO.getId());
            if (departmentDO != null) {
                // 更新
                departmentDO.setDepartmentName(departmentErpRpcDTO.getDepartmentName());
                departmentDO.setUpdateTime(new Date());
                departmentMapper.updateByPrimaryKeySelective(departmentDO);
            } else {
                // 新增
                DepartmentDO departmentDO1 = new DepartmentDO();
                Date date = new Date();
                departmentDO1.setParentId(0);
                departmentDO1.setOrganizationId(organizationDO.getId());
                departmentDO1.setDepartmentName(departmentErpRpcDTO.getDepartmentName());
                departmentDO1.setSort(0);
                departmentDO1.setErpDepartmentId(departmentErpRpcDTO.getErpDepartmentId());
                departmentDO1.setDescription("ERP 同步新增部门");
                departmentDO1.setSaleType((short)1);
                departmentDO1.setStatus((short)1);
                departmentDO1.setCreateTime(date);
                departmentDO1.setUpdateTime(date);
                departmentMapper.insert(departmentDO1);
            }
        }
        // 为了避免更新关联时 父不存在，所以分两次进行
        log.info("更新父子级联关系");
        for (DepartmentErpRpcDTO departmentErpRpcDTO : departmentErpRpcDTOS) {
            String orgId = departmentErpRpcDTO.getOrgId();
            OrganizationDO organizationDO = organizationMapper.getByErpOrganizationId(orgId);
            // erp 部门编码
            String erpDepartmentId = departmentErpRpcDTO.getErpDepartmentId();
            DepartmentDO departmentDO =
                departmentMapper.getByErpDepartmentIdAndOrganizationId(erpDepartmentId, organizationDO.getId());
            DepartmentDO departmentDO1 = departmentMapper.getByErpDepartmentId(departmentErpRpcDTO.getParentErpId());
            departmentDO.setParentId(departmentDO1 == null ? 0 : departmentDO1.getId());
            departmentMapper.updateByPrimaryKeySelective(departmentDO);
        }
    }
}
