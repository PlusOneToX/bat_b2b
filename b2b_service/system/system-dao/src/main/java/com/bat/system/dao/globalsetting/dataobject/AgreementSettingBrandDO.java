package com.bat.system.dao.globalsetting.dataobject;

public class AgreementSettingBrandDO {
    private Integer id;

    private Integer agreementSettingId;

    private Short agreementArea;

    private Integer brandId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAgreementSettingId() {
        return agreementSettingId;
    }

    public void setAgreementSettingId(Integer agreementSettingId) {
        this.agreementSettingId = agreementSettingId;
    }

    public Short getAgreementArea() {
        return agreementArea;
    }

    public void setAgreementArea(Short agreementArea) {
        this.agreementArea = agreementArea;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
}