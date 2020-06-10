package com.bat.flexible.api.exchange.dto;

import java.io.Serializable;

public class ExchangeCodeImportBean implements Serializable {
    private static final long serialVersionUID = -1270454228914717347L;

    private String plainCode;

    private String secretCode;

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
}
