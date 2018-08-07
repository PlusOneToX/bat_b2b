package com.bat.system.dao.user.dataobject;

public class RolePermissionDO {
    private Integer id;

    private Integer roleId;

    private Integer permissionId;

    private PermissionDO permission;

    public PermissionDO getPermission() {
        return permission;
    }

    public void setPermission(PermissionDO permission) {
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermissionDO{" + "id=" + id + ", roleId=" + roleId + ", permissionId=" + permissionId
            + ", permission=" + permission + '}';
    }
}