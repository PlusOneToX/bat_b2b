package com.bat.dubboapi.system.organization.dto.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/11 15:50
 */
public class DepartmentRpcDTO implements Serializable {
    private Integer id;

    private Integer parentId;

    private Integer organizationId;

    private String departmentName;

    private String departmentNameEn;

    private Integer sort;

    private String erpDepartmentId;

    private String description;

    private Short saleType;

    private Short status;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentNameEn() {
        return departmentNameEn;
    }

    public void setDepartmentNameEn(String departmentNameEn) {
        this.departmentNameEn = departmentNameEn;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getErpDepartmentId() {
        return erpDepartmentId;
    }

    public void setErpDepartmentId(String erpDepartmentId) {
        this.erpDepartmentId = erpDepartmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getSaleType() {
        return saleType;
    }

    public void setSaleType(Short saleType) {
        this.saleType = saleType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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
