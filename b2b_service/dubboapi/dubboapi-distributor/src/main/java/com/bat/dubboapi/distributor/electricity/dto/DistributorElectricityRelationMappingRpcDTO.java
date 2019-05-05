package com.bat.dubboapi.distributor.electricity.dto;


import java.io.Serializable;

public class DistributorElectricityRelationMappingRpcDTO implements Serializable {

    private static final long serialVersionUID = 5768923530260043747L;
    private String appKey;

    private String secret;

    private String sessionKey;

    private String url;

    private String ePlatfrom;

    private String sellerNick;

    private Integer distributorId;

    private String orderSourceId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getePlatfrom() {
        return ePlatfrom;
    }

    public void setePlatfrom(String ePlatfrom) {
        this.ePlatfrom = ePlatfrom;
    }

    public void setOrderSourceId(String orderSourceId) {
        this.orderSourceId = orderSourceId;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrderSourceId() {
        return orderSourceId;
    }

}
