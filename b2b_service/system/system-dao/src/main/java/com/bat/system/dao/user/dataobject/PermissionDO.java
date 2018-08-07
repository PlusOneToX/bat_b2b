package com.bat.system.dao.user.dataobject;

public class PermissionDO {
    private Integer id;

    private String service;

    private String module;

    private String permissionName;

    private String permissionModule;

    private String urlPath;

    private String method;

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
        this.service = service;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getPermissionModule() {
        return permissionModule;
    }

    public void setPermissionModule(String permissionModule) {
        this.permissionModule = permissionModule;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath == null ? null : urlPath.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
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
        return "PermissionDO{" + "id=" + id + ", service='" + service + '\'' + ", module='" + module + '\''
            + ", permissionName='" + permissionName + '\'' + ", permissionModule='" + permissionModule + '\''
            + ", urlPath='" + urlPath + '\'' + ", method='" + method + '\'' + ", sort=" + sort + ", status=" + status
            + '}';
    }
}