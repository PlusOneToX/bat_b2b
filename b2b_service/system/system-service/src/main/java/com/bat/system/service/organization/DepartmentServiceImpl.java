package com.bat.system.service.organization;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.organization.DepartmentService;
import com.bat.system.api.organization.dto.DepartmentCreateCmd;
import com.bat.system.api.organization.dto.DepartmentQry;
import com.bat.system.api.organization.dto.DepartmentUpdateCmd;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
import com.bat.system.service.organization.executor.DepartmentCmdExc;
import com.bat.system.service.organization.executor.DepartmentQryExc;
import com.bat.system.service.organization.executor.DepartmentRpcQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentQryExc departmentQryExc;

    @Resource
    private DepartmentCmdExc departmentCmdExc;

    @Resource
    private DepartmentRpcQryExc departmentRpcQryExc;

    @Override
    public boolean createDepartment(DepartmentCreateCmd cmd) {
        return departmentCmdExc.createDepartment(cmd);
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer id) {
        return departmentQryExc.getDepartmentById(id);
    }

    @Override
    public PageInfo<DepartmentDTO> listDepartment(DepartmentQry qry) {
        return departmentQryExc.listDepartment(qry);
    }

    @Override
    public PageInfo<DepartmentDTO> listDepartmentByOrganization(DepartmentQry qry) {
        return departmentQryExc.listDepartmentByOrganization(qry);
    }

    @Override
    public boolean deleteDepartmentById(Integer id) {
        return departmentCmdExc.deleteDepartmentById(id);
    }

    @Override
    public boolean updateDepartment(DepartmentUpdateCmd cmd) {
        return departmentCmdExc.updateDepartment(cmd);
    }

    @Override
    public void syncDepartment() {
        List<DepartmentErpRpcDTO> departmentErpRpcDTOS = departmentRpcQryExc.listDepartment();
        departmentCmdExc.syncDepartment(departmentErpRpcDTOS);
    }
}
