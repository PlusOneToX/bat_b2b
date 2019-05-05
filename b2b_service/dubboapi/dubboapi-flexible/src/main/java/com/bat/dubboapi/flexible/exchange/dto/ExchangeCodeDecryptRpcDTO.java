package com.bat.dubboapi.flexible.exchange.dto;

import java.io.Serializable;

public class ExchangeCodeDecryptRpcDTO implements Serializable {

    private String encryptSecretCode;

    private String decryptSecretCode;

    public String getEncryptSecretCode() {
        return encryptSecretCode;
    }

    public void setEncryptSecretCode(String encryptSecretCode) {
        this.encryptSecretCode = encryptSecretCode;
    }

    public String getDecryptSecretCode() {
        return decryptSecretCode;
    }

    public void setDecryptSecretCode(String decryptSecretCode) {
        this.decryptSecretCode = decryptSecretCode;
    }
}
