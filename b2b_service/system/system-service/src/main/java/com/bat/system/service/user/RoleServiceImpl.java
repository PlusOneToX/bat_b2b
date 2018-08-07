package com.bat.system.service.user;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.user.RoleService;
import com.bat.system.api.user.dto.RoleCreateCmd;
import com.bat.system.api.user.dto.RoleQry;
import com.bat.system.api.user.dto.RoleUpdateCmd;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.api.user.dto.data.UserInfoDTO;
import com.bat.system.service.user.executor.RoleQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.system.service.user.executor.RoleCmdExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleQryExc roleQryExc;

    @Resource
    private RoleCmdExc roleCmdExc;

    @Resource
    private PermissionServiceImpl permissionService;

    @Override
    public boolean createRole(RoleCreateCmd cmd) {
        boolean role = roleCmdExc.createRole(cmd);
        permissionService.syncCache();
        return role;
    }

    @Override
    public RoleDTO getRoleById(Integer id) {
        return roleQryExc.getRoleById(id);
    }

    @Override
    public PageInfo<RoleDTO> listRole(RoleQry qry) {
        return roleQryExc.listRole(qry);
    }

    @Override
    public boolean deleteRoleById(Integer id) {
        boolean b = roleCmdExc.deleteRoleById(id);
        permissionService.syncCache();
        return b;
    }

    @Override
    public boolean updateRole(RoleUpdateCmd cmd) {
        boolean b = roleCmdExc.updateRole(cmd);
        permissionService.syncCache();
        return b;
    }

    @Override
    public UserInfoDTO getRoleByIds(List<Integer> ids) {
        return roleQryExc.getRoleByIds(ids);
    }
}