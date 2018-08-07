package com.bat.distributor.service.role;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.api.role.RoleServiceI;
import com.bat.distributor.api.role.dto.RoleCmd;
import com.bat.distributor.api.role.dto.RoleId;
import com.bat.distributor.api.role.dto.RoleListQry;
import com.bat.distributor.api.role.dto.RoleOpenCmd;
import com.bat.distributor.api.role.dto.data.RoleDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.service.role.executor.RoleCmdExe;
import com.bat.distributor.service.role.executor.RoleQryExe;

@Service
public class RoleServiceImpl implements RoleServiceI {

    @Resource
    private RoleCmdExe cmdExe;

    @Resource
    private RoleQryExe qryExe;

    @Override
    public void createRole(RoleCmd cmd) {
        cmdExe.createRole(cmd);
    }

    @Override
    public void updateRole(RoleCmd cmd) {
        cmdExe.updateRole(cmd);
    }

    @Override
    public void openRole(RoleOpenCmd cmd) {
        cmdExe.openRole(cmd);
    }

    @Override
    public PageInfo<GroupDTO> listRole(RoleListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public void deleteRole(RoleId cmd) {
        cmdExe.deleteRole(cmd);
    }

    @Override
    public RoleDTO getRole(RoleId qry) {
        return qryExe.execute(qry);
    }

}