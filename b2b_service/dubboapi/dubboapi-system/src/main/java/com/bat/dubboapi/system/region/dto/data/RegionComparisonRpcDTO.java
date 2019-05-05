package com.bat.dubboapi.system.region.dto.data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/15 22:47
 */
public class RegionComparisonRpcDTO implements Serializable {
    private Integer id;

    private Integer regionId;

    private String regionName;

    private String anotherName;

    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "RegionComparisonRpcDTO{" + "id=" + id + ", regionId=" + regionId + ", regionName='" + regionName + '\''
            + ", anotherName='" + anotherName + '\'' + ", parentId=" + parentId + '}';
    }
}
