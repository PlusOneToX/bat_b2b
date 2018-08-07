package com.bat.system.service.user.executor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.user.dto.RoleCreateCmd;
import com.bat.system.api.user.dto.RoleUpdateCmd;
import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;
import com.bat.system.service.user.convertor.RoleConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class RoleCmdExc {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(RoleCreateCmd cmd) {
        RoleDO roleDO = RoleConvertor.toRoleDO(cmd);
        roleMapper.insert(roleDO);
        // 保存权限信息
        if (!CollectionUtils.isEmpty(cmd.getPermissionIds())) {
            cmd.getPermissionIds().forEach(integer -> {
                RolePermissionDO rolePermissionDO = new RolePermissionDO();
                rolePermissionDO.setRoleId(roleDO.getId());
                rolePermissionDO.setPermissionId(integer);
                rolePermissionMapper.insert(rolePermissionDO);
            });
        } else if (!CollectionUtils.isEmpty(cmd.getPermissions())) {
            cmd.getPermissions().forEach(stringStringMap -> {
                String module = stringStringMap.get("module");
                List<PermissionDO> dos = permissionMapper.listByModule(module);
                if (CollectionUtils.isNotEmpty(dos)) {
                    dos.forEach(permissionDO -> {
                        RolePermissionDO rolePermissionDO = new RolePermissionDO();
                        rolePermissionDO.setRoleId(roleDO.getId());
                        rolePermissionDO.setPermissionId(permissionDO.getId());
                        rolePermissionMapper.insert(rolePermissionDO);
                    });
                }
            });
        }
        if (!CollectionUtils.isEmpty(cmd.getMenusIds())) {
            cmd.getMenusIds().forEach(integer -> {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(roleDO.getId());
                roleMenuDO.setMenuId(integer);
                roleMenuMapper.insert(roleMenuDO);
            });
        } else if (!CollectionUtils.isEmpty(cmd.getMenus())) {
            roleMenuMapper.deleteByRoleId(roleDO.getId());
            cmd.getMenus().forEach(stringStringMap -> {
                String module = stringStringMap.get("module");
                MenuDO menuDO = menuMapper.getByMenuEn(module);
                if (menuDO != null) {
                    RoleMenuDO roleMenuDO = new RoleMenuDO();
                    roleMenuDO.setRoleId(roleDO.getId());
                    roleMenuDO.setMenuId(menuDO.getId());
                    roleMenuMapper.insert(roleMenuDO);
                }
            });
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleById(Integer id) {
        if (roleMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_ROLE_ID_NOT_EXISTS);
        }
        roleMapper.deleteByPrimaryKey(id);
        rolePermissionMapper.deleteByRoleId(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleUpdateCmd cmd) {
        if (roleMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_ROLE_ID_NOT_EXISTS);
        }
        // 更新角色信息
        roleMapper.updateByPrimaryKeySelective(RoleConvertor.toRoleDO(cmd));
        // 先删除旧的关联关系
        rolePermissionMapper.deleteByRoleId(cmd.getId());
        roleMenuMapper.deleteByRoleId(cmd.getId());
        // 添加新的权限信息
        if (!CollectionUtils.isEmpty(cmd.getPermissionIds())) {
            cmd.getPermissionIds().forEach(integer -> {
                RolePermissionDO rolePermissionDO = new RolePermissionDO();
                rolePermissionDO.setRoleId(cmd.getId());
                rolePermissionDO.setPermissionId(integer);
                rolePermissionMapper.insert(rolePermissionDO);
            });
        } else if (!CollectionUtils.isEmpty(cmd.getPermissions())) {
            cmd.getPermissions().forEach(stringStringMap -> {
                String module = stringStringMap.get("module");
                List<PermissionDO> dos = permissionMapper.listByModule(module);
                dos.forEach(permissionDO -> {
                    RolePermissionDO rolePermissionDO = new RolePermissionDO();
                    rolePermissionDO.setRoleId(cmd.getId());
                    rolePermissionDO.setPermissionId(permissionDO.getId());
                    rolePermissionMapper.insert(rolePermissionDO);
                });
            });
        }
        if (!CollectionUtils.isEmpty(cmd.getMenusIds())) {
            cmd.getMenusIds().forEach(integer -> {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(cmd.getId());
                roleMenuDO.setMenuId(integer);
                roleMenuMapper.insert(roleMenuDO);
            });
        } else if (!CollectionUtils.isEmpty(cmd.getMenus())) {
            for (Map<String, String> menu : cmd.getMenus()) {
                String module = menu.get("module");
                MenuDO menuDO = menuMapper.getByMenuEn(module);
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(cmd.getId());
                roleMenuDO.setMenuId(menuDO.getId());
                roleMenuMapper.insert(roleMenuDO);
            }
        }
        return true;
    }

}
