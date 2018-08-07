package com.bat.distributor.dao.electricity.dataobject;

public class DistributorElectricityRelationMappingDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String sellerNick;

    private String erpShopName;

    private String orderSourceId;

    private String ePlatfrom;

    private String appKey;

    private String secret;

    private String url;

    private String sessionKey;

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
        this.distributorName = distributorName == null ? null : distributorName.trim();
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick == null ? null : sellerNick.trim();
    }

    public String getErpShopName() {
        return erpShopName;
    }

    public void setErpShopName(String erpShopName) {
        this.erpShopName = erpShopName == null ? null : erpShopName.trim();
    }

    public String getOrderSourceId() {
        return orderSourceId;
    }

    public void setOrderSourceId(String orderSourceId) {
        this.orderSourceId = orderSourceId;
    }

    public String getePlatfrom() {
        return ePlatfrom;
    }

    public void setePlatfrom(String ePlatfrom) {
        this.ePlatfrom = ePlatfrom;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey == null ? null : sessionKey.trim();
    }
}