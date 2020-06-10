package com.bat.flexible.dao.exchange.dataobject;

public class ExchangeSpecialDistributorDO {
    private Integer id;

    private Integer exchangeSpecialId;

    private Integer distributorId;

    private Integer pageVisits;

    private Integer oneForwardTimes;

    private Integer twoForwardTimes;

    private Integer receiveTimes;

    private String link;

    private Short status;

    private String qrCodeUrl;

    private String shortLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeSpecialId() {
        return exchangeSpecialId;
    }

    public void setExchangeSpecialId(Integer exchangeSpecialId) {
        this.exchangeSpecialId = exchangeSpecialId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getPageVisits() {
        return pageVisits;
    }

    public void setPageVisits(Integer pageVisits) {
        this.pageVisits = pageVisits;
    }

    public Integer getOneForwardTimes() {
        return oneForwardTimes;
    }

    public void setOneForwardTimes(Integer oneForwardTimes) {
        this.oneForwardTimes = oneForwardTimes;
    }

    public Integer getTwoForwardTimes() {
        return twoForwardTimes;
    }

    public void setTwoForwardTimes(Integer twoForwardTimes) {
        this.twoForwardTimes = twoForwardTimes;
    }

    public Integer getReceiveTimes() {
        return receiveTimes;
    }

    public void setReceiveTimes(Integer receiveTimes) {
        this.receiveTimes = receiveTimes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }
}