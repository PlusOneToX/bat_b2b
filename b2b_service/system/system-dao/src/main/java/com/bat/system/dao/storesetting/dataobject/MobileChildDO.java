package com.bat.system.dao.storesetting.dataobject;

public class MobileChildDO {
    private Integer id;

    private Integer parentId;

    private Short appointType;

    private String title;

    private Integer sort;

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

    public Short getAppointType() {
        return appointType;
    }

    public void setAppointType(Short appointType) {
        this.appointType = appointType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}