package com.bat.dubboapi.financial.basesetting.dto.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/25 17:03
 */
public class AccountWxDistributorRpcDTO implements Serializable {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String distributorCompanyName;

    private String accountName;

    private String appId;

    private String apiclientKey;

    private String serialNumber;

    private Date certificateInvalidTime;

    private String appKey;

    private Short appType;

    private String accountNo;

    private String version;

    private Date createTime;

    private Date updateTime;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiclientKey() {
        return apiclientKey;
    }

    public void setApiclientKey(String apiclientKey) {
        this.apiclientKey = apiclientKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getCertificateInvalidTime() {
        return certificateInvalidTime;
    }

    public void setCertificateInvalidTime(Date certificateInvalidTime) {
        this.certificateInvalidTime = certificateInvalidTime;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Short getAppType() {
        return appType;
    }

    public void setAppType(Short appType) {
        this.appType = appType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
