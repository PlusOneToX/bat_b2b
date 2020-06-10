package com.bat.flexible.api.exchange.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class ExchangeCodeExcelDTO {

    @Excel(name = "主键", width = 20, orderNum = "1")
    private Integer id;

    @Excel(name = "兑换活动名称", width = 20, orderNum = "2")
    private String codeName;

    @Excel(name = "兑换卡活动id", width = 20, orderNum = "3")
    private Integer exchangeId;

    @Excel(name = "明码", width = 20, orderNum = "4")
    private String plainCode;

    @Excel(name = "兑换码暗码", width = 20, orderNum = "5")
    private String secretCode;

    @Excel(name = "分销商Id", width = 20, orderNum = "6")
    private Integer distributorId;

    @Excel(name = "分销商名称", width = 20, orderNum = "7")
    private String distributorName;

    @Excel(name = "发卡分销商公司名", width = 20, orderNum = "8")
    private String distributorCompanyName;

    @Excel(name = "创建时间", width = 20, orderNum = "9",format="yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Excel(name = "记录单号", width = 20, orderNum = "9")
    private Integer exchangeExportId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getPlainCode() {
        return plainCode;
    }

    public void setPlainCode(String plainCode) {
        this.plainCode = plainCode;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorCompanyName() {
        return distributorCompanyName;
    }

    public void setDistributorCompanyName(String distributorCompanyName) {
        this.distributorCompanyName = distributorCompanyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getExchangeExportId() {
        return exchangeExportId;
    }

    public void setExchangeExportId(Integer exchangeExportId) {
        this.exchangeExportId = exchangeExportId;
    }
}
