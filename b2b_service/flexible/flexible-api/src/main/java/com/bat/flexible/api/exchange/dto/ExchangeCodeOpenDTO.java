package com.bat.flexible.api.exchange.dto;


import java.io.Serializable;


public class ExchangeCodeOpenDTO implements Serializable {

    private static final long serialVersionUID = -5090050760455172940L;


    private Long id;

    /**
     * 明码
     */
    private String plainCode;

    /**
     * 暗码
     */
    private String secretCode;

    /**
     * 物料名称
     */
    private String itemName;


    /**
     * 二维码地址
     */
    private String qrCodeUrl;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
