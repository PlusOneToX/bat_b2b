package com.bat.system.dao.user.dataobject;

public class RoleMenuDO {
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private MenuDO menu;

    public MenuDO getMenu() {
        return menu;
    }

    public void setMenu(MenuDO menu) {
        this.menu = menu;
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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenuDO{" + "id=" + id + ", roleId=" + roleId + ", menuId=" + menuId + ", menu=" + menu + '}';
    }
}