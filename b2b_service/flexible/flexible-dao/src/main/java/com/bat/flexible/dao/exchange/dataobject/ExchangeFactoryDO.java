package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeFactoryDO {
    private Integer id;

    private Integer exchangeId;

    private Integer addQuantity;

    private Integer invalidQuantityInit;

    private Integer synchronizedQuantity;

    private Short status;

    private String batchOrderNo;

    private Short factory;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private String fileUrl;

    private String positiveUrl;

    private String reverseUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getAddQuantity() {
        return addQuantity;
    }

    public void setAddQuantity(Integer addQuantity) {
        this.addQuantity = addQuantity;
    }

    public Integer getInvalidQuantityInit() {
        return invalidQuantityInit;
    }

    public void setInvalidQuantityInit(Integer invalidQuantityInit) {
        this.invalidQuantityInit = invalidQuantityInit;
    }

    public Integer getSynchronizedQuantity() {
        return synchronizedQuantity;
    }

    public void setSynchronizedQuantity(Integer synchronizedQuantity) {
        this.synchronizedQuantity = synchronizedQuantity;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getBatchOrderNo() {
        return batchOrderNo;
    }

    public void setBatchOrderNo(String batchOrderNo) {
        this.batchOrderNo = batchOrderNo == null ? null : batchOrderNo.trim();
    }

    public Short getFactory() {
        return factory;
    }

    public void setFactory(Short factory) {
        this.factory = factory;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
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

    public String getPositiveUrl() {
        return positiveUrl;
    }

    public void setPositiveUrl(String positiveUrl) {
        this.positiveUrl = positiveUrl == null ? null : positiveUrl.trim();
    }

    public String getReverseUrl() {
        return reverseUrl;
    }

    public void setReverseUrl(String reverseUrl) {
        this.reverseUrl = reverseUrl == null ? null : reverseUrl.trim();
    }
}