package com.bat.flexible.dao.exchange.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class ExchangeCardDO {
    private Integer id;

    private Integer itemId;

    private String codeName;

    private Short status;

    private String codeDesc;

    private Short type;

    private Short source;

    private Integer codeQuantity;

    private Integer limitQuantity;

    private Integer saleQuantity;

    private Integer exchangeQuantity;

    private Integer refundQuantity;

    private Integer invalidCount;

    private Long startTime;

    private Long endTime;

    private Short exchangeWay;

    private BigDecimal orderUseThreshold;

    private Short goodsScope;

    private String qrCodeUrl;

    private Short isEntity;

    private Short isUseMall;

    private Short mallType;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private String modelNo;

    private String headImg;

    private Short distributorScope;

    private Short mailSetting;

    private BigDecimal mailFee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName == null ? null : codeName.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc == null ? null : codeDesc.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public Integer getCodeQuantity() {
        return codeQuantity;
    }

    public void setCodeQuantity(Integer codeQuantity) {
        this.codeQuantity = codeQuantity;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Integer getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(Integer saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public Integer getExchangeQuantity() {
        return exchangeQuantity;
    }

    public void setExchangeQuantity(Integer exchangeQuantity) {
        this.exchangeQuantity = exchangeQuantity;
    }

    public Integer getRefundQuantity() {
        return refundQuantity;
    }

    public void setRefundQuantity(Integer refundQuantity) {
        this.refundQuantity = refundQuantity;
    }

    public Integer getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(Integer invalidCount) {
        this.invalidCount = invalidCount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Short getExchangeWay() {
        return exchangeWay;
    }

    public void setExchangeWay(Short exchangeWay) {
        this.exchangeWay = exchangeWay;
    }

    public BigDecimal getOrderUseThreshold() {
        return orderUseThreshold;
    }

    public void setOrderUseThreshold(BigDecimal orderUseThreshold) {
        this.orderUseThreshold = orderUseThreshold;
    }

    public Short getGoodsScope() {
        return goodsScope;
    }

    public void setGoodsScope(Short goodsScope) {
        this.goodsScope = goodsScope;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl == null ? null : qrCodeUrl.trim();
    }

    public Short getIsEntity() {
        return isEntity;
    }

    public void setIsEntity(Short isEntity) {
        this.isEntity = isEntity;
    }

    public Short getIsUseMall() {
        return isUseMall;
    }

    public void setIsUseMall(Short isUseMall) {
        this.isUseMall = isUseMall;
    }

    public Short getMallType() {
        return mallType;
    }

    public void setMallType(Short mallType) {
        this.mallType = mallType;
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

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo == null ? null : modelNo.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public Short getDistributorScope() {
        return distributorScope;
    }

    public void setDistributorScope(Short distributorScope) {
        this.distributorScope = distributorScope;
    }

    public Short getMailSetting() {
        return mailSetting;
    }

    public void setMailSetting(Short mailSetting) {
        this.mailSetting = mailSetting;
    }

    public BigDecimal getMailFee() {
        return mailFee;
    }

    public void setMailFee(BigDecimal mailFee) {
        this.mailFee = mailFee;
    }

}