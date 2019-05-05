package com.bat.dubboapi.thirdparty.quanyi.dto;

import java.io.Serializable;
import java.util.Date;

public class ThirdQuanyiRpcCmd implements Serializable {

    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String thirdPhone;

    private String thirdCode;

    private String thirdSkuNo;

    private String thirdUserRemark;

    private Integer exchangeId;

    private String exchangeName;

    private Integer exchangeCodeId;

    private String plainCode;

    private String secretCode;

    private Integer materialId;

    private String materialName;

    private Integer customerId;

    private String customerNo;

    private Integer orderId;

    private Date createTime;

    private Date updateTime;

    private Short dispatchFlag;

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

    public String getThirdPhone() {
        return thirdPhone;
    }

    public void setThirdPhone(String thirdPhone) {
        this.thirdPhone = thirdPhone;
    }

    public String getThirdCode() {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode) {
        this.thirdCode = thirdCode;
    }

    public String getThirdSkuNo() {
        return thirdSkuNo;
    }

    public void setThirdSkuNo(String thirdSkuNo) {
        this.thirdSkuNo = thirdSkuNo;
    }

    public String getThirdUserRemark() {
        return thirdUserRemark;
    }

    public void setThirdUserRemark(String thirdUserRemark) {
        this.thirdUserRemark = thirdUserRemark;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Integer getExchangeCodeId() {
        return exchangeCodeId;
    }

    public void setExchangeCodeId(Integer exchangeCodeId) {
        this.exchangeCodeId = exchangeCodeId;
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

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Short getDispatchFlag() {
        return dispatchFlag;
    }

    public void setDispatchFlag(Short dispatchFlag) {
        this.dispatchFlag = dispatchFlag;
    }
}