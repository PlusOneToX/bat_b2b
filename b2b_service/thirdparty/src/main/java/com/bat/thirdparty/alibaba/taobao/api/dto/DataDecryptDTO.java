package com.bat.thirdparty.alibaba.taobao.api.dto;

import java.util.List;

public class DataDecryptDTO {

    private String appkey;
    private String secret;
    private String sessionKey;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
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

    public List<ReceiverDataDTO> getList() {
        return list;
    }

    public void setList(List<ReceiverDataDTO> list) {
        this.list = list;
    }

    private List<ReceiverDataDTO> list;
}
