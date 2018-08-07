package com.bat.system.dao.organization.dataobject;

import java.util.Date;

import lombok.ToString;

@ToString
public class DepartmentDO {
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
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getDepartmentNameEn() {
        return departmentNameEn;
    }

    public void setDepartmentNameEn(String departmentNameEn) {
        this.departmentNameEn = departmentNameEn == null ? null : departmentNameEn.trim();
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
        this.erpDepartmentId = erpDepartmentId == null ? null : erpDepartmentId.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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