package com.bat.thirdparty.alibaba.taobao.dao.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TaobaoOrderDO implements Serializable {
    private Long oid;

    private String adjustFee;

    private String buyerRate;

    private Integer cid;

    private String discountFee;

    private String divideOrderFee;

    private Date endTime;

    private String isDaixiao;

    private String isOversold;

    private String isShShip;

    private Integer num;

    private Long numIid;

    private String orderFrom;

    private String outerSkuId;

    private String partMjzDiscount;

    private String payment;

    private String picPath;

    private String price;

    private Integer propoint;

    private Long refundId;

    private String refundStatus;

    private String sellerRate;

    private String sellerType;

    private String shipper;

    private String skuId;

    private String skuPropertiesName;

    private String snapshotUrl;

    private String status;

    private String title;

    private BigDecimal totalFee;

    private String customization;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(String adjustFee) {
        this.adjustFee = adjustFee == null ? null : adjustFee.trim();
    }

    public String getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(String buyerRate) {
        this.buyerRate = buyerRate == null ? null : buyerRate.trim();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee == null ? null : discountFee.trim();
    }

    public String getDivideOrderFee() {
        return divideOrderFee;
    }

    public void setDivideOrderFee(String divideOrderFee) {
        this.divideOrderFee = divideOrderFee == null ? null : divideOrderFee.trim();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIsDaixiao() {
        return isDaixiao;
    }

    public void setIsDaixiao(String isDaixiao) {
        this.isDaixiao = isDaixiao == null ? null : isDaixiao.trim();
    }

    public String getIsOversold() {
        return isOversold;
    }

    public void setIsOversold(String isOversold) {
        this.isOversold = isOversold == null ? null : isOversold.trim();
    }

    public String getIsShShip() {
        return isShShip;
    }

    public void setIsShShip(String isShShip) {
        this.isShShip = isShShip == null ? null : isShShip.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom == null ? null : orderFrom.trim();
    }

    public String getOuterSkuId() {
        return outerSkuId;
    }

    public void setOuterSkuId(String outerSkuId) {
        this.outerSkuId = outerSkuId == null ? null : outerSkuId.trim();
    }

    public String getPartMjzDiscount() {
        return partMjzDiscount;
    }

    public void setPartMjzDiscount(String partMjzDiscount) {
        this.partMjzDiscount = partMjzDiscount == null ? null : partMjzDiscount.trim();
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? null : picPath.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public Integer getPropoint() {
        return propoint;
    }

    public void setPropoint(Integer propoint) {
        this.propoint = propoint;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus == null ? null : refundStatus.trim();
    }

    public String getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(String sellerRate) {
        this.sellerRate = sellerRate == null ? null : sellerRate.trim();
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType == null ? null : sellerType.trim();
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper == null ? null : shipper.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName == null ? null : skuPropertiesName.trim();
    }

    public String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl == null ? null : snapshotUrl.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getCustomization() {
        return customization;
    }

    public void setCustomization(String customization) {
        this.customization = customization == null ? null : customization.trim();
    }
}