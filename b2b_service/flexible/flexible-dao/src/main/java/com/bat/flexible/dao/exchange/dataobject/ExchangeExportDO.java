package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeExportDO {
    private Integer id;

    private Integer distributorId;

    private String exportName;

    private Integer exchangeId;

    private Integer outNum;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private String fileUrl;

    private Short examineFlag;

    private Integer hasUseNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getExportName() {
        return exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName == null ? null : exportName.trim();
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Short getExamineFlag() {
        return examineFlag;
    }

    public void setExamineFlag(Short examineFlag) {
        this.examineFlag = examineFlag;
    }

    public Integer getHasUseNum() {
        return hasUseNum;
    }

    public void setHasUseNum(Integer hasUseNum) {
        this.hasUseNum = hasUseNum;
    }
}