package com.bat.platform.dao.tenant.dataobject;

public class PlatformTenantSmsTemplateDO {
    private Integer id;

    private Integer platformTenantSmsId;

    private Short templateType;

    private String templateCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlatformTenantSmsId() {
        return platformTenantSmsId;
    }

    public void setPlatformTenantSmsId(Integer platformTenantSmsId) {
        this.platformTenantSmsId = platformTenantSmsId;
    }

    public Short getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Short templateType) {
        this.templateType = templateType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }
}