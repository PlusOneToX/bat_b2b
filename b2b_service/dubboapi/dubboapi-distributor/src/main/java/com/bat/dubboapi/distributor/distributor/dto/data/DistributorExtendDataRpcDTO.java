package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorExtendDataRpcDTO implements Serializable {
    private Integer distributorId;
    private String certNo;
    private String comment;
    private String language;
    private String currencyType;
    private Short distributionFlag;
    private Short distributionMode;
    private Short distributionPayWay;
    private Short autoFlag;
    private Short customerFlag;
    private Short customerMode;
    private Short distributionPromotionFlag;
    private String distributionQrUrl;
    private Short erpFlag;
    private Integer erpId;
    private String erpNo;

    private Short subAccountFlag;

    public Short getDistributionPayWay() {
        return distributionPayWay;
    }

    public void setDistributionPayWay(Short distributionPayWay) {
        this.distributionPayWay = distributionPayWay;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Short getDistributionFlag() {
        return distributionFlag;
    }

    public void setDistributionFlag(Short distributionFlag) {
        this.distributionFlag = distributionFlag;
    }

    public Short getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(Short distributionMode) {
        this.distributionMode = distributionMode;
    }

    public Short getAutoFlag() {
        return autoFlag;
    }

    public void setAutoFlag(Short autoFlag) {
        this.autoFlag = autoFlag;
    }

    public Short getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Short customerFlag) {
        this.customerFlag = customerFlag;
    }

    public Short getCustomerMode() {
        return customerMode;
    }

    public void setCustomerMode(Short customerMode) {
        this.customerMode = customerMode;
    }

    public Short getDistributionPromotionFlag() {
        return distributionPromotionFlag;
    }

    public void setDistributionPromotionFlag(Short distributionPromotionFlag) {
        this.distributionPromotionFlag = distributionPromotionFlag;
    }

    public String getDistributionQrUrl() {
        return distributionQrUrl;
    }

    public void setDistributionQrUrl(String distributionQrUrl) {
        this.distributionQrUrl = distributionQrUrl;
    }

    public Short getErpFlag() {
        return erpFlag;
    }

    public void setErpFlag(Short erpFlag) {
        this.erpFlag = erpFlag;
    }

    public Integer getErpId() {
        return erpId;
    }

    public void setErpId(Integer erpId) {
        this.erpId = erpId;
    }

    public String getErpNo() {
        return erpNo;
    }

    public void setErpNo(String erpNo) {
        this.erpNo = erpNo;
    }

    public Short getSubAccountFlag() {
        return subAccountFlag;
    }

    public void setSubAccountFlag(Short subAccountFlag) {
        this.subAccountFlag = subAccountFlag;
    }
}
