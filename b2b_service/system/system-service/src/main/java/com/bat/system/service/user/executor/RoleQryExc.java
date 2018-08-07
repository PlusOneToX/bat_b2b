package com.bat.system.service.user.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.user.dto.RoleQry;
import com.bat.system.api.user.dto.data.MenuDTO;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.api.user.dto.data.UserInfoDTO;
import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;
import com.bat.system.service.user.convertor.MenuConvertor;
import com.bat.system.service.user.convertor.PermissionConvertor;
import com.bat.system.service.user.convertor.RoleConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class RoleQryExc {
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

    public PageInfo<RoleDTO> listRole(RoleQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<RoleDO> roleDOList = roleMapper.listRoleByName(qry.getName());
        PageInfo pageInfo = new PageInfo(roleDOList);
        List<RoleDTO> toRoleDTOList = RoleConvertor.toRoleDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }

    public RoleDTO getRoleById(Integer id) {
        RoleDO roleDO = roleMapper.selectByPrimaryKey(id);
        if (roleDO != null) {
            RoleDTO roleDTO = RoleConvertor.toRoleDTO(roleDO);
            List<RoleMenuDO> roleMenuDOS = roleMenuMapper.listUnionByRoleId(roleDTO.getId());
            List<MenuDTO> collect = roleMenuDOS.stream()
                .map(roleMenuDO -> MenuConvertor.toMenuDTO(roleMenuDO.getMenu())).collect(Collectors.toList());
            roleDTO.setMenus(collect);
            List<RolePermissionDO> rolePermissionDOS = rolePermissionMapper.listUnionByRoleId(roleDTO.getId());
            List<PermissionDTO> collect1 = rolePermissionDOS.stream()
                .map(rolePermissionDO -> PermissionConvertor.toPermissionDTO(rolePermissionDO.getPermission()))
                .collect(Collectors.toList());
            roleDTO.setPermissions(collect1);
            return roleDTO;
        }
        return null;
    }

    public UserInfoDTO getRoleByIds(List<Integer> ids) {
        List<RoleDO> roleDOS = roleMapper.selectByPrimaryKeys(ids);
        List<Integer> roleIds = roleDOS.stream().map(RoleDO::getId).collect(Collectors.toList());
        List<RoleMenuDO> roleMenuDOS = roleMenuMapper.listByRoleIds(roleIds);
        List<Integer> menuIds = roleMenuDOS.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
        List<RolePermissionDO> rolePermissionDOS = rolePermissionMapper.listByRoleIds(roleIds);
        List<Integer> permissionIds =
            rolePermissionDOS.stream().map(RolePermissionDO::getPermissionId).collect(Collectors.toList());
        List<MenuDO> menuDOS = menuMapper.listByPrimaryKeys(menuIds);
        List<PermissionDO> permissionDOS = permissionMapper.listByPrimaryKeys(permissionIds);
        UserInfoDTO dto = new UserInfoDTO();
        dto.setRoles(RoleConvertor.toRoleDTOList(roleDOS));
        dto.setMenus(MenuConvertor.toMenuDTOList(menuDOS));
        dto.setPermissions(PermissionConvertor.toPermissionDTOList(permissionDOS));
        return dto;
    }
}
