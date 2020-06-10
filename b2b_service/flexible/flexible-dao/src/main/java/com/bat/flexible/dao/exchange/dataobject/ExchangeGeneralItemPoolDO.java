package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeGeneralItemPoolDO {
    private Integer id;

    private String itemCode;

    private Short openFlag;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}