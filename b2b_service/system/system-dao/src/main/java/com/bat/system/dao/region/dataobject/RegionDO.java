package com.bat.system.dao.region.dataobject;

public class RegionDO {
    private Integer id;

    private String regionName;

    private Integer parentId;

    private Short level;

    private Short haveNext;

    private String regionNameEn;

    private String erpCode;

    private Short openFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public Short getHaveNext() {
        return haveNext;
    }

    public void setHaveNext(Short haveNext) {
        this.haveNext = haveNext;
    }

    public String getRegionNameEn() {
        return regionNameEn;
    }

    public void setRegionNameEn(String regionNameEn) {
        this.regionNameEn = regionNameEn == null ? null : regionNameEn.trim();
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }
}