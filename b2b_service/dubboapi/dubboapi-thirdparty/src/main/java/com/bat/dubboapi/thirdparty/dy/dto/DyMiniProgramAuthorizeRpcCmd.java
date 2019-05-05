package com.bat.dubboapi.thirdparty.dy.dto;

import java.io.Serializable;

/**
 * 沙漠
 */
public class DyMiniProgramAuthorizeRpcCmd implements Serializable {
    private String code;
    private String encryptedData;
    private String iv;
    private String appid;
    private String secret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
