package com.bat.system.dao.user.dataobject;

public class MenuDO {
    private Integer id;

    private String service;

    private String serviceEn;

    private String module;

    private String moduleEn;

    private String menu;

    private String menuEn;

    private Integer sort;

    private Short status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    public String getServiceEn() {
        return serviceEn;
    }

    public void setServiceEn(String serviceEn) {
        this.serviceEn = serviceEn == null ? null : serviceEn.trim();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
    }

    public String getModuleEn() {
        return moduleEn;
    }

    public void setModuleEn(String moduleEn) {
        this.moduleEn = moduleEn == null ? null : moduleEn.trim();
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu == null ? null : menu.trim();
    }

    public String getMenuEn() {
        return menuEn;
    }

    public void setMenuEn(String menuEn) {
        this.menuEn = menuEn == null ? null : menuEn.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MenuDO{" + "id=" + id + ", service='" + service + '\'' + ", serviceEn='" + serviceEn + '\''
            + ", module='" + module + '\'' + ", moduleEn='" + moduleEn + '\'' + ", menu='" + menu + '\'' + ", menuEn='"
            + menuEn + '\'' + ", sort=" + sort + ", status=" + status + '}';
    }
}