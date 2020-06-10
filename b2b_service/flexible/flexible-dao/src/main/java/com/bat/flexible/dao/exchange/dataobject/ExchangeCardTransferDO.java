package com.bat.flexible.dao.exchange.dataobject;

public class ExchangeCardTransferDO {
    private Integer id;

    private Integer exchangeId;

    private String transferText;

    private String transferImg;

    private String receiveText;

    private String receiveImg;

    private Short switchFlag;

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

    public String getTransferText() {
        return transferText;
    }

    public void setTransferText(String transferText) {
        this.transferText = transferText == null ? null : transferText.trim();
    }

    public String getTransferImg() {
        return transferImg;
    }

    public void setTransferImg(String transferImg) {
        this.transferImg = transferImg == null ? null : transferImg.trim();
    }

    public String getReceiveText() {
        return receiveText;
    }

    public void setReceiveText(String receiveText) {
        this.receiveText = receiveText == null ? null : receiveText.trim();
    }

    public String getReceiveImg() {
        return receiveImg;
    }

    public void setReceiveImg(String receiveImg) {
        this.receiveImg = receiveImg == null ? null : receiveImg.trim();
    }

    public Short getSwitchFlag() {
        return switchFlag;
    }

    public void setSwitchFlag(Short switchFlag) {
        this.switchFlag = switchFlag;
    }
}