package com.bat.distributor.dao.platform.dataobject;

public class DyPlatformDistributorDO {
    private Integer id;

    private Integer dyPlatformId;

    private Integer distributorId;

    private String name;

    private String companyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDyPlatformId() {
        return dyPlatformId;
    }

    public void setDyPlatformId(Integer dyPlatformId) {
        this.dyPlatformId = dyPlatformId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }
}