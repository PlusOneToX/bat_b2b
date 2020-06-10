package com.bat.flexible.api.exchange.dto;


import java.io.Serializable;

public class ExchangeBoxCodeRequest implements Serializable {

    private static final long serialVersionUID = 6573320286187219784L;

    /**
     * 盒码
     */
    private String boxCode;

    /**
     * 明码
     */
    private String plainCode;

    /**
     * 暗码
     */
    private String secretCode;


    private Integer exchangeCodeId;

    private Integer exchangeFactoryId;

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
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


    public Integer getExchangeFactoryId() {
        return exchangeFactoryId;
    }

    public void setExchangeFactoryId(Integer exchangeFactoryId) {
        this.exchangeFactoryId = exchangeFactoryId;
    }

    public Integer getExchangeCodeId() {
        return exchangeCodeId;
    }

    public void setExchangeCodeId(Integer exchangeCodeId) {
        this.exchangeCodeId = exchangeCodeId;
    }
}
