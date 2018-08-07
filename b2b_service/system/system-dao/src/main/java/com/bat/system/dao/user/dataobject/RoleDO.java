package com.bat.system.dao.user.dataobject;

import java.util.Date;
import java.util.List;

public class RoleDO {
    private Integer id;

    private String roleName;

    private String roleNameEn;

    private String roleDescription;

    private Date createTime;

    private Date updateTime;

    List<RoleMenuDO> roleMenus;

    List<RolePermissionDO> rolePermissions;

    public List<RoleMenuDO> getRoleMenus() {
        return roleMenus;
    }

    public void setRoleMenus(List<RoleMenuDO> roleMenus) {
        this.roleMenus = roleMenus;
    }

    public List<RolePermissionDO> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermissionDO> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleNameEn() {
        return roleNameEn;
    }

    public void setRoleNameEn(String roleNameEn) {
        this.roleNameEn = roleNameEn == null ? null : roleNameEn.trim();
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription == null ? null : roleDescription.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}