package com.bat.flexible.api.exchange.dto;


import java.io.Serializable;
import java.util.List;

public class ExchangeOpenRequest extends YCRequest implements Serializable {

    private static final long serialVersionUID = 6573320286187219784L;

    /**
     * 明码
     */
    private String plainCode;

    /**
     * 暗码
     */
    private String secretCode;


    /**
     * 明码列表
     */
    private List<String> plainCodeList;

    /**
     * 暗码列表
     */
    private List<String> secretCodeList;


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

    public List<String> getPlainCodeList() {
        return plainCodeList;
    }

    public void setPlainCodeList(List<String> plainCodeList) {
        this.plainCodeList = plainCodeList;
    }

    public List<String> getSecretCodeList() {
        return secretCodeList;
    }

    public void setSecretCodeList(List<String> secretCodeList) {
        this.secretCodeList = secretCodeList;
    }
}
