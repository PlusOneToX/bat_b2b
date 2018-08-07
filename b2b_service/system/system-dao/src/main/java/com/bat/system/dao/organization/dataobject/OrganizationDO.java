package com.bat.system.dao.organization.dataobject;

import java.util.Date;

import lombok.ToString;

@ToString
public class OrganizationDO {
    private Integer id;

    private String name;

    private String erpOrganizationId;

    private String description;

    private Short status;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getErpOrganizationId() {
        return erpOrganizationId;
    }

    public void setErpOrganizationId(String erpOrganizationId) {
        this.erpOrganizationId = erpOrganizationId == null ? null : erpOrganizationId.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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